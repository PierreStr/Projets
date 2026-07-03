

"""########################################
#                                         #
#            Silver to Gold               #
#                                         #
########################################"""


"""
Objectif :

    Créer une modélisation dimensionnelle des données

"""


## Importation des scripts silver ---------------

from DB_link import engine
import pandas as pd

#------------------------------------------------



## Téléchargement des données ##############################################################

accidents = pd.read_sql_table("accidents", schema="silver", con=engine)

population = {}
for year in range(2017, 2025):
    population[f"population_{year}"] = pd.read_sql_table(f"population_{year}", schema="silver", con=engine)

occupation_sol = pd.read_sql_table("occupation_par_commune", schema="silver", con=engine)

revenus = {}
for year in range(2017, 2024):
    revenus[f"revenus_{year}"] = pd.read_sql_table(f"revenus_{year}", schema="silver", con=engine)


## Création des dimensions #################################################################

## utils
def add_id(df, name):
    df["ID_" + name] = range(1, len(df)+1)
    return df

def give_year_id(year):
    return 1 + (year-2017)*288


## Création de la dimension géographie ----------------------------------------------------

columns_kept = ['GEO_commune_REFNIS', 'GEO_commune', 'GEO_province', 'GEO_region']

dim_geo = population["population_2024"][columns_kept]
dim_geo = dim_geo.drop_duplicates()

add_id(dim_geo, "geo")


## Création de la dimension date ---------------------------------------------------------

# Toutes les combinaisons
dim_date = pd.MultiIndex.from_product(
    [
        range(2017, 2025),    # 2017 à 2024 inclus
        range(1, 13),         # Mois
        range(24)             # Heures
    ],
    names=["DT_annee", "DT_mois", "DT_heure"]
).to_frame(index=False)

# Nom du mois
dim_date["DT_nom_mois"] = pd.to_datetime(
    dim_date["DT_mois"], format="%m"
).dt.month_name(locale="fr_FR")

dim_date["DT_date"] = pd.to_datetime(dict(
    year=dim_date["DT_annee"],
    month=dim_date["DT_mois"],
    day=1,
    hour=dim_date["DT_heure"]
))

add_id(dim_date, "date")


## Création de la dimension situation ----------------------------------------------------

dim_situation = accidents[["ACC_etat_victimes", "ACC_vehicule_1", "ACC_vehicule_2", "ACC_collision_type", "ACC_nom_autoroute"]]
dim_situation = dim_situation.drop_duplicates()

add_id(dim_situation, "situation")


## Création de la dimension condition ----------------------------------------------------

dim_condition = accidents[["COND_carrefour", "COND_meteo", "COND_route", "COND_agglomeration", "COND_lumiere", "COND_type_route", "COND_obstacles", "COND_routes_largeur_cat", "COND_densite", "nb_routes_cat"]]
dim_condition = dim_condition.drop_duplicates()

add_id(dim_condition, "condition")


## Création de la dimension population ----------------------------------------------------

dim_population = pd.DataFrame()

group_cols = [
    "GEO_commune_REFNIS",
    "GEO_commune",
    "GEO_province",
    "GEO_region",
]

def add_population_year(new_df, year):

    df = new_df.copy()

    ## Calculer la moyenne, la medianne et le nombre d'individu pour chaque commune
    age_stats = df.groupby(group_cols).agg(
        P_age_moyen=("P_age", "mean"),
        P_age_median=("P_age", "median"),
        P_nbr_habitants=("P_nbr_individus", "sum")
    )

    # Calculer le pourcentage de Femme et d'Homme par commune
    sexe = pd.crosstab(
        [df[col] for col in group_cols],
        df["P_sexe"],
        values=df["P_nbr_individus"],
        aggfunc="sum"
    ).fillna(0)

    # Pondérer par le nombre d'individus
    sexe = sexe.div(sexe.sum(axis=1), axis=0) * 100
    sexe = sexe.add_prefix("P_%_")

    # Calculer le pourcentage des états civils par communes
    etat_civil = pd.crosstab(
        [df[col] for col in group_cols],
        df["P_etat_civil"],
        values=df["P_nbr_individus"],
        aggfunc="sum"
    ).fillna(0)

    # Pondérer sur le nombre d'individus
    etat_civil = etat_civil.div(etat_civil.sum(axis=1), axis=0) * 100
    etat_civil = etat_civil.add_prefix("P_%_")

    # Join les différents calculs
    df = (
        age_stats
        .join(sexe)
        .join(etat_civil)
        .reset_index()
    )

    columns_kept = ["GEO_commune_REFNIS", "P_age_moyen", "P_age_median", "P_nbr_habitants", "P_%_F", "P_%_M", "P_%_Célibataire", "P_%_Divorcé", "P_%_Marié", "P_%_Veuf"]
    df = df[columns_kept].merge(dim_geo[["GEO_commune_REFNIS", "ID_geo"]], on=["GEO_commune_REFNIS"], how="left").drop(columns=["GEO_commune_REFNIS"])

    df["ID_date"] = give_year_id(year)

    return df


for year in range(2017, 2025):
    df = add_population_year(population[f"population_{year}"], year)
    dim_population = pd.concat([dim_population, df], ignore_index=True)

dim_population = dim_population.drop_duplicates()
add_id(dim_population, "population")


## Création de la dimension occupation ----------------------------------------------------

dim_occupation = occupation_sol.merge(dim_geo[["ID_geo", "GEO_commune_REFNIS"]], on="GEO_commune_REFNIS", how="left")
dim_occupation = dim_occupation.drop(columns=["GEO_commune_REFNIS", "GEO_commune"])
dim_occupation["ID_geo"] = dim_occupation["ID_geo"].astype("Int64")

dim_occupation = dim_occupation.rename(columns={"DT_annee" : "ID_date"})
dim_occupation["ID_date"] = dim_occupation["ID_date"].apply(give_year_id)

dim_occupation = dim_occupation.drop_duplicates()

add_id(dim_occupation, "occupation")


## Création de la dimension revenus ----------------------------------------------------

dim_revenus = pd.DataFrame()

def add_revenus_year(new_df, year):

    df = new_df.copy()

    columns_kept = ["GEO_commune_REFNIS", "REV_pourcentage_manquant", "REV_median", "REV_risque_pauvrete"]
    df = df[columns_kept].merge(dim_geo[["GEO_commune_REFNIS", "ID_geo"]], on=["GEO_commune_REFNIS"], how="left").drop(columns=["GEO_commune_REFNIS"])

    df["ID_date"] = give_year_id(year)

    return df

for year in range(2017, 2024):
    df = add_revenus_year(revenus[f"revenus_{year}"], year)
    dim_revenus = pd.concat([dim_revenus, df], ignore_index=True)

dim_revenus = dim_revenus.drop_duplicates()

add_id(dim_revenus, "revenus")


## Création de la table de faits ##################################################################

fact_table = accidents[['GEO_commune_REFNIS',
       'GEO_longitude', 'GEO_latitude', 'COND_carrefour', 'COND_meteo', 'COND_route', 'COND_agglomeration',
       'COND_lumiere', 'COND_type_route', 'ACC_etat_victimes','ACC_vehicule_1', 'ACC_vehicule_2', 'ACC_collision_type',
       'COND_obstacles', 'DT_date', 'ID', 'ACC_nom_autoroute','COND_routes_largeur_cat', 'COND_densite', 'SOIN_min_distance']]

# merge géo
fact_table = fact_table.merge(dim_geo[["GEO_commune_REFNIS", "ID_geo"]], on=["GEO_commune_REFNIS"], how="left").drop(columns=["GEO_commune_REFNIS"])
fact_table = fact_table.drop_duplicates("ID")

# merge date
fact_table = fact_table.merge(dim_date[["DT_date", "ID_date"]], on="DT_date", how="left").drop(columns="DT_date")
fact_table = fact_table.drop_duplicates("ID")

# merge condition
on_columns = ['COND_carrefour', 'COND_meteo', 'COND_route', 'COND_agglomeration', 'COND_lumiere', 'COND_type_route', 'COND_obstacles', 'COND_routes_largeur_cat', 'COND_densite']
fact_table = fact_table.merge(dim_condition, on=on_columns, how="left").drop(columns=on_columns)
fact_table = fact_table.drop_duplicates("ID")

# merge situation
on_columns = ['ACC_etat_victimes', 'ACC_vehicule_1', 'ACC_vehicule_2', 'ACC_collision_type', 'ACC_nom_autoroute']
fact_table = fact_table.merge(dim_situation, on=on_columns, how="left").drop(columns=on_columns)
fact_table = fact_table.drop_duplicates("ID")

# merge occupation
fact_table = fact_table.merge(dim_occupation[["ID_geo", "ID_occupation"]], on="ID_geo", how="left")
fact_table = fact_table.drop_duplicates("ID")

# merge population
fact_table = fact_table.merge(dim_population[["ID_geo", "ID_population"]], on="ID_geo", how="left")
fact_table = fact_table.drop_duplicates("ID")

# merge revenus
fact_table = fact_table.merge(dim_revenus[["ID_geo", "ID_revenus"]], on="ID_geo", how="left")
fact_table = fact_table.drop_duplicates("ID")

## 
dim_geo.to_sql("dim_geo", engine, schema='gold', if_exists='replace', index=False)
dim_date.to_sql("dim_date", engine, schema='gold', if_exists='replace', index=False)
dim_condition.to_sql("dim_condition", engine, schema='gold', if_exists='replace', index=False)
dim_situation.to_sql("dim_situation", engine, schema='gold', if_exists='replace', index=False)
dim_population.to_sql("dim_population", engine, schema='gold', if_exists='replace', index=False)
dim_occupation.to_sql("dim_occupation", engine, schema='gold', if_exists='replace', index=False)
dim_revenus.to_sql("dim_revenus", engine, schema='gold', if_exists='replace', index=False)
fact_table.to_sql("fact_table", engine, schema='gold', if_exists='replace', index=False)

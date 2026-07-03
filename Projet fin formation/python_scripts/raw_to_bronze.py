

"""########################################
#                                         #
#             Raw to Bronze               #
#                                         #
########################################"""


"""
Objectif :

    Rendre les données des excels lisibles.
    (les routes et autoroutes ne sont pas chargées, car SQL ne gère pas les géométries)

"""


## Importation des librairies nécessaires--------

from DB_link import engine
import pandas as pd

#------------------------------------------------


### Chargement des data sets ############################################################################

## Chargement du data set des accidents -----------------------------------------------------
accidents = pd.read_excel("C:\\Projet_perso\\accidents\\OPENDATA_MAP_2017-2024.xlsx")


## Chargement du data set sur les populations -----------------------------------------------

# listing du noms des colonnes de l'excel
variables = list(pd.read_excel("C:\\Projet_perso\\densité_population\\Columns description.xlsx").iloc[:, 0])
variables = [x for x in variables if not x.endswith("_NL")]

def not_NL(my_string):
    return not my_string.endswith("_NL")

population = {}

for year in range(2017, 2025):
    population[f"population_{year}"] = pd.read_excel(f"C:\\Projet_perso\\densité_population\\TF_SOC_POP_STRUCT_{year}.xlsx", usecols=not_NL)
    cols = list(population[f"population_{year}"].columns)

    # On gère les excels qui n'ont pas la variable année, et qui on des noms de colonnes différents
    if cols != variables:
        for col_name in variables:   ## Pour les noms des colonnes manquantes
            if col_name == "CD_YEAR":
                population[f"population_{year}"][col_name] = year   

        for col_name in cols:        ## Pour les noms de colonnes inexistantes dans variables
            if col_name == "CD_REFNIS":
                population[f"population_{year}"].rename(columns={col_name : "CD_MUNTY_REFNIS"}, inplace=True)
            elif col_name == "TX_DESCR_FR":
                population[f"population_{year}"].rename(columns={col_name : "TX_MUNTY_DESCR_FR"}, inplace=True)        


## Chargement du data set sur l'occupation des sols par commune ------------------------------------------------------
dict_occupation_sol = pd.read_excel("C:\\Projet_perso\\occupation_du_sol\\FR_bodemgebruik_statbel_220905_140647.xlsx", sheet_name=["Par commune"], header=None)
occupation_sol = {}
occupation_sol["occupation_par_commune"] = dict_occupation_sol["Par commune"]

# Clean la lecture de l'excel 
def rewrite_column_name(df):
    df = df.drop(columns=[4], errors="ignore")
    colonnes = df.iloc[1]
    annees = df.iloc[0]
    colonnes[4:] = annees[4:]
    df.columns = colonnes
    df = df.iloc[2:].reset_index(drop=True)
    df = df.drop(columns=df.columns[-10:])
    return df

occupation_sol["occupation_par_commune"] = rewrite_column_name(occupation_sol["occupation_par_commune"])


## Chargement du data set sur les revenus -----------------------------------------------------------------------------
dict_revenus = pd.read_excel("C:\\Projet_perso\\revenus\\ADI_T1_STATBEL_FR.xlsx", sheet_name=["2017", "2018", "2019", "2020", "2021", "2022", "2023"], skiprows=3, skipfooter=9)
revenus = {}
for key, value in dict_revenus.items():
    revenus[f"revenus_{key}"] = value


## Chargement du data set sur les points de départ des ambulances ----------------------------------------------------
soins = {}
soins["ambulances"] = pd.read_excel("C:\\Projet_perso\\soins\\ambulances_01042026_fr.xlsx")

#######################################################################################################################

### Exportation dans la DB SQL Server

def export_to_DB(dict_dfs):
    for df_name, df in dict_dfs.items():
        df.to_sql(df_name, engine, schema='bronze', if_exists='replace', index=False)


accidents.to_sql("accidents", engine, schema='bronze', if_exists='replace', index=False)
export_to_DB(population)
export_to_DB(occupation_sol)
export_to_DB(revenus)
export_to_DB(soins)



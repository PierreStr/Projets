

'''##################################################
#                                                   #
#        Partie population pour le Silver           #
#                                                   #
##################################################'''



import pandas as pd


## Fonction de chargement depuis le Bronze ###################################
def load_data(engine):

    population = {}
    
    for year in range(2017, 2025):
        population[f"population_{year}"] = pd.read_sql_table(f"population_{year}", schema="bronze", con=engine)

    return population



## Fonction de production du DataFrame nettoyé et enrichi ####################
def clean(population):

    for year in range(2017, 2025):

        # Drop les colonnes non utiles
        df = population[f"population_{year}"]
        df.drop(columns=["CD_NATLTY", "TX_NATLTY_FR", "CD_DSTR_REFNIS", "TX_ADM_DSTR_DESCR_FR", "CD_CIV_STS",
                         "CD_PROV_REFNIS", "CD_RGN_REFNIS"], inplace=True)

        df.rename(columns={
            'CD_MUNTY_REFNIS': 'GEO_commune_REFNIS',
            'TX_MUNTY_DESCR_FR': 'GEO_commune',
            'TX_PROV_DESCR_FR': 'GEO_province',
            'TX_RGN_DESCR_FR': 'GEO_region',
            'CD_SEX': 'P_sexe',
            'TX_CIV_STS_FR': 'P_etat_civil',
            'CD_AGE': 'P_age',
            'MS_POPULATION': 'P_nbr_individus',
            'CD_YEAR': 'DT_annee'
        }, inplace=True)


        df["GEO_province"] = df["GEO_province"].fillna("Bruxelles")


        ## Uniformiser les noms des entités administratives 
        df["GEO_province"] = (
            df["GEO_province"]
            .str.replace("Province de ", "", regex=False)
            .str.replace("Province d’Anvers", "Anvers", regex=False)
            .str.replace("Province du ", "", regex=False)
        )

        df["GEO_region"] = df["GEO_region"].replace({
            "Région wallonne": "Wallonie",
            "Région flamande": "Flandre",
            "Région de Bruxelles-Capitale": "Bruxelles-Capitale"
        })

        df["GEO_commune"] = df["GEO_commune"].str.lower()

        population[f"population{year}"] = df
    

    ## Certains code REFNIS des communes ayant changé en 2019, les code REFNIS désuets sont remplacés par les plus récents
    def update_refnis(df):

        refnis_2024 = (
            population["population_2024"][["GEO_commune", "GEO_commune_REFNIS"]]
            .drop_duplicates()
            .set_index("GEO_commune")["GEO_commune_REFNIS"]
        )

        df = df.copy()

        df["GEO_commune_REFNIS"] = (
            df["GEO_commune"]
            .str.strip()
            .str.lower()
            .map(refnis_2024)
        )

        return df

    update_refnis(population["population_2017"])
    update_refnis(population["population_2018"])


    return population


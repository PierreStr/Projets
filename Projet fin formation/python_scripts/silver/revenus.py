

'''##################################################
#                                                   #
#          Partie revenus pour le Silver            #
#                                                   #
##################################################'''



import pandas as pd


## Fonction de chargement depuis le Bronze ###################################
def load_data(engine):

    revenus = {}

    for year in range(2017, 2024):
        revenus[f"revenus_{year}"] = pd.read_sql_table(f"revenus_{year}", schema="bronze", con=engine)

    return revenus



## Fonction de production du DataFrame nettoyé et enrichi ####################
def clean(revenus):

    for year in range(2017, 2024):

        # Retirer les colonnes inutiles
        revenus[f"revenus_{year}"] = revenus[f"revenus_{year}"].drop(columns=["Nombre\npersonnes\nprises en\ncompte",
                            "Limite\nsupérieure\n1er quartile",
                            "Limite\nsupérieure\n3ème\nquartile",
                            "Ecart\ninterquartile"])

        # Renommer les colonnes
        revenus[f"revenus_{year}"] = revenus[f"revenus_{year}"].rename(columns={
            'Code\nINS' : "GEO_commune_REFNIS",
            'Commune' : "GEO_commune",
            '%\npersonnes\nnon prises\nen compte' : 'REV_pourcentage_manquant',
            'Médian' : "REV_median",
            'Risque de\npauvreté\nadministratif' : 'REV_risque_pauvrete'
        })

        # Changer le typage
        revenus[f"revenus_{year}"]["REV_median"] = revenus[f"revenus_{year}"]["REV_median"].replace(".", pd.NA).astype("float")
        revenus[f"revenus_{year}"]["REV_risque_pauvrete"] = revenus[f"revenus_{year}"]["REV_risque_pauvrete"].replace(".", pd.NA).astype("float")

        ## Uniformiser les noms des entités administratives 
        revenus[f"revenus_{year}"]["GEO_commune"] = revenus[f"revenus_{year}"]["GEO_commune"].str.lower()

    return revenus
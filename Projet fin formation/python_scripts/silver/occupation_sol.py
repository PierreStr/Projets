

'''##################################################
#                                                   #
#         Partie occupation pour le Silver          #
#                                                   #
##################################################'''



import pandas as pd


## Fonction de chargement depuis le Bronze ###################################
def load_data(engine):
    
    occupation_sol = pd.read_sql_table("occupation_par_commune", schema="bronze", con=engine)
    
    return occupation_sol



## Fonction de production du DataFrame nettoyé et enrichi ####################
def clean(occupation_sol):

    # Dépivotage des années
    occupation_sol = occupation_sol.melt(
        id_vars=occupation_sol.columns[:4],
        var_name="année",
        value_name="surface (ha)"
    )

    
    # Changer typage
    occupation_sol["année"] = occupation_sol["année"].astype("int")
    occupation_sol["surface (ha)"] = occupation_sol["surface (ha)"].replace("-", pd.NA)
    occupation_sol["surface (ha)"] = occupation_sol["surface (ha)"].astype("float")
    occupation_sol["rubrique"] = occupation_sol["rubrique"].astype("float")
    occupation_sol["refnis"] = occupation_sol["refnis"].astype("int")

    # Garder seulement les rubriques qui semblent pertinentes
    rubrique_kept = [1, 2, 3, 3.1, 3.2, 3.3, 3.4, 3.5, 3.6, 3.7, 3.8, 3.9, 7, 9, 10]

    # Garder les lignes qui nous intéressent
    occupation_sol = occupation_sol.loc[occupation_sol.année > 2016]
    occupation_sol = occupation_sol.loc[occupation_sol.rubrique.isin(rubrique_kept)]

    # Renommer le nom des rubriques
    mapping = {
        3.4: 3.3,
        3.3: 3.4,
        3.5: 3.4,
        3.6: 3.4,
        3.7: 3.4,
        3.8: 3.4,
        3.9: 3.4,
        7 : 4,
        9 : 5,
        10 : 6
    }
    occupation_sol["rubrique"] = occupation_sol["rubrique"].replace(mapping)
    
    occupation_sol["rubrique détail"] = (
        occupation_sol["rubrique détail"].str.replace(r"^\d+\.(\d+)?\s+", "", regex=True).str.replace(" (autres que 3.3 ci-dessous)", "")
    )

    occupation_sol.loc[occupation_sol.rubrique == 3.4, "rubrique détail"] = "Terrains bâtis et terrains connexes autres"

    # Groupby pour regrouper les rubriques de type "autres"
    group_cols = [col for col in occupation_sol.columns if col != "surface (ha)"]
    occupation_sol = occupation_sol.groupby(group_cols, as_index=False)["surface (ha)"].sum()

    # Retirer rubrique pcq en fait on s'en fou
    occupation_sol = occupation_sol.drop(columns=["rubrique"])

    # Repivoter la table pour avoir les rubriques en colonnes
    occupation_sol = occupation_sol.pivot_table(
        index=["refnis", "localité", "année"],
        columns="rubrique détail",
        values="surface (ha)",
        aggfunc="first"
    ).reset_index()

    # Encore renommer les colonnes
    occupation_sol.rename(columns={
        'refnis' : 'GEO_commune_REFNIS',
        'localité' : 'GEO_commune',
        'année' : 'DT_annee',
        'surface (ha)' : 'SOL_surface_ha',
        "Autres terrains" : "OCC_% Autres",
        "Superficie continentale" : "OCC_Total",
        "Terrains bâtis et terrains connexes autres" : 	"OCC_% Autres terrains bâtis",
        "Terres agricoles totales (excl. serres)" : "OCC_% Terrains agricoles",
        "Total des forêts et autres terres boisés" : "OCC_% Terrains forestiers",
        "Terrains bâtis et terrains connexes" : "OCC_% Total terrains bâtis"
    }, inplace=True)

    occupation_sol.columns.name = None

    # Faire des proportions en fonction de superficie continentales
    def make_proportion(col_name):
        occupation_sol[col_name] = occupation_sol[col_name] / occupation_sol["OCC_Total"] * 100

    make_proportion("OCC_% Autres")
    make_proportion("OCC_% Autres terrains bâtis")
    make_proportion("OCC_% Terrains agricoles")
    make_proportion("OCC_% Terrains forestiers")
    make_proportion("OCC_% Total terrains bâtis")

    # Uniformiser les noms des entités administratives
    occupation_sol["GEO_commune"] = occupation_sol["GEO_commune"].str.lower()

    return occupation_sol


'''##################################################
#                                                   #
#         Partie accidents pour le Silver           #
#                                                   #
##################################################'''


## Importation des librairies nécessaires
import pandas as pd
import geopandas as gpd
import networkx as nx
from scipy.spatial import cKDTree
import math as m
import pickle

# Utils #######################################
def encode_str_ID(x: str) -> int:
    parts = x.split("/")

    i = int(parts[0])

    if i == 9:
        return 0

    if len(parts) == 1:
        return 2 ** i

    j = int(parts[1])
    return (2 ** i) + (2 ** j)

def encode_int_ID(x: int, inconnu = 9) -> int:
    if x == inconnu:
        return 0
    return 2**(x-1)

def encode_obstacles_ID(x: int) -> int:
    if x < 0 or x == 99:
        return 0
    if x == 0:
        return 1
    if x == 71:
        return 2**3
    else:
        return 2**(x-49)
    
def encode_collision_type_ID(x: str) -> int:
    if x == "A":
        return 2**8
    if x == "9":
        return 0
    
    return 2**(int(x)-1)

#################################################


## Fonction de chargement depuis le Bronze ###################################
def load_data(engine):
    accidents = pd.read_sql_table("accidents", schema="bronze", con=engine)
    autoroutes = gpd.read_file("C:\\Projet_perso\\geographie\\Autoroutes_72\\autoroutes_72.shp")
    routes = gpd.read_file("C:\\Projet_perso\\geographie\\TN_RO.gml", layer="RoadLink")
    largeur_routes = gpd.read_file("C:\\Projet_perso\\geographie\\TN_RO.gml", layer="RoadWidth")
    ambulances = pd.read_sql_table("ambulances", schema="bronze", con=engine)

    return accidents, autoroutes, routes, largeur_routes, ambulances



## Fonction de production du DataFrame nettoyé et enrichi ####################
def clean_and_enrich(accidents, autoroutes, routes, largeur_routes, ambulances):


    '''------------------------
      Nettoyage des données
    ------------------------'''

    # Drop les colonnes non pertinentes (les colonnes traduites)
    cols_to_drop = [cols for cols in accidents.columns if cols.endswith("_NL")]
    accidents.drop(columns=cols_to_drop, inplace=True)

    # Renommer les colonnes
    accidents.rename(columns={
        'DT_YEAR_COLLISION': 'DT_annee',
        'DT_MONTH_COLLISION': 'DT_mois',
        'DT_TIME': 'DT_heure',
        'CD_NIS': 'GEO_commune_REFNIS',
        'TX_RGN_COLLISION_FR': 'GEO_region',
        'TX_PROV_COLLISION_FR': 'GEO_province',
        'TX_MUNTY_COLLISION_FR': 'GEO_commune',
        'MS_X_COORD': 'GEOM_coord_X',
        'MS_Y_COORD': 'GEOM_coord_Y',
        'CD_CROSSWAY': 'COND_carrefour_ID',
        'TX_CROSSWAY_FR': 'COND_carrefour',
        'CD_WEATHER': 'COND_meteo_ID',
        'TX_WEATHER_FR': 'COND_meteo',
        'CD_ROAD_CONDITION': 'COND_route_ID',
        'TX_ROAD_CONDITION_FR': 'COND_route',
        'CD_BUILD_UP_AREA': 'COND_agglomeration_ID',
        'TX_BUILD_UP_AREA_FR': 'COND_agglomeration',
        'CD_LIGHT_CONDITION': 'COND_lumiere_ID',
        'TX_LIGHT_CONDITION_FR': 'COND_lumiere',
        'CD_ROAD_TYPE': 'COND_type_route_ID',
        'CD_ROAD_TYPE_FR': 'COND_type_route',
        'CD_CLASS_ACCIDENTS': 'ACC_etat_victimes_ID',
        'TX_CLASS_ACCIDENTS_FR': 'ACC_etat_victimes',
        'CD_ROAD_USR_TYPE1': 'ACC_vehicule_1_ID',
        'TX_ROAD_USR_TYPE1_FR': 'ACC_vehicule_1',
        'CD_ROAD_USR_TYPE2': 'ACC_vehicule_2_ID',
        'TX_ROAD_USR_TYPE2_FR': 'ACC_vehicule_2',
        'CD_COLLISION_TYPE': 'ACC_collision_type_ID',
        'TX_COLLISON_TYPE_FR': 'ACC_collision_type',
        'CD_OBSTACLES': 'COND_obstacles_ID',
        'TX_OBSTACLES_FR': 'COND_obstacles'
    }, inplace=True)



    def set_to_inconnu(col):
        accidents.loc[accidents[col + "_ID"] == 0, col] = "inconnu"

    def apply_and_convert(col, func, new_type):
        accidents[col] = accidents[col].apply(func).astype(new_type)


    ## Vérifier le typage des colonnes et uniformiser les valeurs inconnues

    # Dates
    accidents["DT_date"] = pd.to_datetime(dict(
        year=accidents["DT_annee"],
        month=accidents["DT_mois"],
        day=1,
        hour=accidents["DT_heure"]
    ))
    accidents.drop(columns=["DT_annee", "DT_mois", "DT_heure"], inplace=True)

    # Coordonnées Lambert72
    accidents = gpd.GeoDataFrame(
        accidents,
        geometry=gpd.points_from_xy(accidents.GEOM_coord_X, accidents.GEOM_coord_Y),
        crs="EPSG:31370"
    )

    # carrefour_ID
    apply_and_convert("COND_carrefour_ID", encode_int_ID, "int8")
    set_to_inconnu("COND_carrefour")

    # meteo_ID
    apply_and_convert("COND_meteo_ID", encode_str_ID, "int8")
    set_to_inconnu("COND_meteo")

    # route_ID
    apply_and_convert("COND_route_ID", encode_str_ID, "int8")
    set_to_inconnu("COND_route")

    # agglo_ID
    apply_and_convert("COND_agglomeration_ID", encode_int_ID, "int8")
    set_to_inconnu("COND_agglomeration")

    # lumière_ID
    apply_and_convert("COND_lumiere_ID", encode_int_ID, "int8")
    set_to_inconnu("COND_lumiere")

    # type_route_ID
    apply_and_convert("COND_type_route_ID", encode_int_ID, "int8")
    set_to_inconnu("COND_type_route")

    # Etat victimes_ID
    apply_and_convert("ACC_etat_victimes_ID", encode_int_ID, "int8")
    set_to_inconnu("ACC_etat_victimes")

    # vehicule 1 et 2_ID
    apply_and_convert("ACC_vehicule_1_ID", lambda x: encode_int_ID(x, inconnu=99), "int16")
    apply_and_convert("ACC_vehicule_2_ID", lambda x: encode_int_ID(x, inconnu=99), "int16")
    set_to_inconnu("ACC_vehicule_1")
    set_to_inconnu("ACC_vehicule_2")

    # collision type_ID
    apply_and_convert("ACC_collision_type_ID", encode_collision_type_ID, "int16")
    set_to_inconnu("ACC_collision_type")

    # obstacle_ID
    apply_and_convert("COND_obstacles_ID", encode_obstacles_ID, "int32")
    set_to_inconnu("COND_obstacles")

    ## Les ID n'étant utiles qu'à sélectionner les valeurs "inconnues", on les supprime
    accidents = accidents.drop(columns=[cols for cols in accidents.columns if cols.endswith("_ID")])

    ## On supprime les éventuels doublons
    accidents = accidents.drop_duplicates()

    # Uniformiser avec les autres datasets
    accidents["GEO_commune"] = accidents["GEO_commune"].str.lower()




    '''---------------------------
      Enrichissement des données
    ---------------------------'''
    
    ## GEO_province possède des nulls. En effet Bruxelles n'est pas considérer comme tel. Dans ce projet je ferai ce raccourci
    accidents["GEO_province"] = accidents["GEO_province"].fillna("Bruxelles")

    ## Création d'un identifiant
    accidents["ID"] = range(1, len(accidents) + 1)

    ## Enrichissement avec les données des autouroutes et des routes belges

    # Objectifs : 
    # * ajouter un indicateur de la densité du réseau routier à proximité;
    # * ajouter la largeur de la route supposée;
    # * ajouter le nom de l'autoroute le cas échéan. 

    # Retirer les routes 'fictives'
    routes = routes[routes["fictitious"] != True]

    # Catégoriser la largeur des routes
    largeur_routes["COND_routes_largeur_cat"] = pd.cut(
        largeur_routes["width"],
        bins=[0, 3, 5, 7, 10, float("inf")],
        labels=["très étroit", "étroit", "moyen", "large", "très large"],
        include_lowest=True
    )

    # L'identifiant des routes change entre 'RoadLink' et 'RoadWidth' on modifie de sorte à avoir les même IDs
    largeur_routes["localId"] = largeur_routes["localId"].str.replace(
        "-RW-",
        "-RL-",
        regex=False
    )

    # Vérification / Transformation dans un système de coordonnées compatibles
    routes = routes.set_crs(epsg=3812, allow_override=True)
    routes = routes.to_crs(epsg=31370)
    autoroutes = autoroutes.to_crs(epsg=31370)
    autoroutes = autoroutes.set_crs(epsg=31370)

    # Data frame regroupant largeur des routes et les routes mêmes
    reseau_routes = routes[["localId", "geometry"]].merge(largeur_routes[["localId", "COND_routes_largeur_cat"]], on=["localId"], how="left")[["geometry", "COND_routes_largeur_cat"]]

    # Masque sur toutes les géométries valides de accidents, un autre sur les autoroutes
    mask_coord_acc_valid = accidents.geometry.is_valid
    mask_acc_sur_autoroute = accidents.COND_type_route == "Autoroute"

    # Jointure sur l'autoroutes la plus proche (à 20m d'erreur près), uniquement depuis les géométries valides
    joined = gpd.sjoin_nearest(
        accidents[["ID", "geometry"]].loc[mask_coord_acc_valid & mask_acc_sur_autoroute],
        autoroutes[["ROAD", "geometry"]],
        how="left",
        max_distance=20,
    ).drop(columns=["index_right", "geometry"]).drop_duplicates()

    accidents = accidents.merge(joined, on="ID", how="left")

    # On s'assure de ne pas avoir de nom d'autoroute là ou ce n'est pas indiqué sur une autoroute
    # (Possible avec un accident sous une autoroute par exemple, celui-ci aurait été join précédemment)
    accidents["ROAD"] = accidents["ROAD"].where(
        accidents["COND_type_route"] != "Autoroutes",
        None
    )

    # Redéfinition du mask de validité
    mask_coord_acc_valid = accidents.geometry.is_valid

    # Joindre la largeur de la route au data frame (encore une fois basé sur la route la plus proche)
    joined = gpd.sjoin_nearest(
        accidents[["ID", "geometry"]].loc[mask_coord_acc_valid],
        reseau_routes,
        how="left",
        max_distance=10
    ).drop(columns=["index_right", "geometry"]).drop_duplicates()

    accidents = accidents.merge(joined, on="ID", how="left")

    ## on garde une trace du nombre de routes par catégorie de largeur
    nb_routes_par_cat = (
        reseau_routes
        .groupby("COND_routes_largeur_cat")
        .size()
        .reset_index(name="nb_routes_cat")
    )

    accidents = accidents.merge(
        nb_routes_par_cat,
        on="COND_routes_largeur_cat",
        how="left"
    )

    # On récupère le graphe complet du réseau routier belge avec le cKDTree permettant une recherche rapide près d'un point donné
    with open("silver\\reseau_data.pkl", "rb") as f:
        data = pickle.load(f)

    G = data["graph"]
    intersection_nodes = data["nodes"]
    intersection_tree = data["tree"]

    def surface_disque(r):
        return m.pi * r * r

    # définition de la métrique pour la densité.
    # Il s'agit ici de la somme des intersections pondérées par la taille des routes concernées / surface autour du point
    def density_metrics(point, dist=1000):

        if not point.is_valid:
            return pd.NA
        
        indices = intersection_tree.query_ball_point((point.x, point.y), r=dist)

        total = sum(
            G.nodes[intersection_nodes[i]]["weight"]
            for i in indices
        )

        return total / surface_disque(dist)

    # Appliquer la métrique au jeu de donnée
    accidents["COND_densite"] = accidents.geometry.apply(density_metrics)

    ## Rendre la variable densité plus lisible en l'étirant au centre 
    low = accidents["COND_densite"].quantile(0.05)
    high = accidents["COND_densite"].quantile(0.95)

    accidents["COND_densite"] = (accidents["COND_densite"]) / (high - low)



    ## Enrichissement avec les données des départ d'ambulances

    # Objectifs : 
    # * Ajouter la plus petite distance à un point de départ d'ambulance.

    # Convertir les données dans un Geodataframe
    gdf = gpd.GeoDataFrame(
        ambulances,
        geometry=gpd.points_from_xy(ambulances["Longitude"], ambulances["Latitude"]),
        crs="EPSG:4326"
    )

    # Convertir les coordonnées pour en avoir des compatibles
    gdf = gdf.to_crs(epsg=31370)

    ambulances["X_Lambert72"] = gdf.geometry.x
    ambulances["Y_Lambert72"] = gdf.geometry.y

    ambulances = gpd.GeoDataFrame(
        ambulances,
        geometry=gpd.points_from_xy(
            ambulances["X_Lambert72"],
            ambulances["Y_Lambert72"]
        ),
        crs="EPSG:31370"
    )

    # On garde seulement la géométrie en ajoutant un ID. On s'intéresse seulement à l'existence d'un point  
    ambulances = ambulances.drop(columns=[cols for cols in ambulances.columns if cols != "geometry"])
    ambulances["ID"] = range(1, len(ambulances)+1)

    accidents = accidents.drop_duplicates("ID")

    # Redéfinition du mask de validité
    mask_coord_acc_valid = accidents.geometry.is_valid

    # Joindre les ambulances les plus proches, on affiche cette fois la distance dans "SOIN_min_distance"
    joined = gpd.sjoin_nearest(
        accidents.loc[mask_coord_acc_valid][["geometry", "ID"]],
        ambulances,
        how="left",
        distance_col="SOIN_min_distance"
    ).drop(columns=["geometry", "index_right", "ID_right"]).rename(columns={"ID_left" : "ID"})

    accidents = accidents.merge(joined, on="ID", how="left")


    # On retransforme les coordonnées Lambert72 en latitude et longitude
    gdf = gpd.GeoDataFrame(
        accidents,
        geometry=gpd.points_from_xy(accidents["GEOM_coord_X"], accidents["GEOM_coord_Y"]),
        crs="EPSG:31370"
    )

    gdf = gdf.to_crs(epsg=4326)

    accidents["GEO_longitude"] = gdf.geometry.x
    accidents["GEO_latitude"] = gdf.geometry.y

    # retirer les colonnes devenues inutiles
    accidents = accidents.drop(columns=["geometry", "GEOM_coord_X", "GEOM_coord_Y"])

    accidents = accidents.rename(columns={"ROAD" : "ACC_nom_autoroute"})


    return accidents
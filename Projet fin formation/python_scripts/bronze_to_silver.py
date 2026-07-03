

"""########################################
#                                         #
#           Bronze to Silver              #
#                                         #
########################################"""


"""
Objectif :

    Rendre les données pertinentes pour nos objectifs.

"""


## Importation des scripts silver ---------------

from DB_link import engine

import time as t

import silver.accidents as acc
import silver.occupation_sol as occ
import silver.population as pop
import silver.revenus as rev

#------------------------------------------------


## Importation depuis le Bronze puis cleaning vers le Silver #####################################

## Accidents -------------------------------------------------------------------------------------
print("Start accidents")
start_accidents = t.time()
accidents, autoroutes, routes, largeur_routes, ambulances = acc.load_data(engine)
accidents = acc.clean_and_enrich(accidents, autoroutes, routes, largeur_routes, ambulances)
end_accidents = t.time()
print(f"End accidents in {end_accidents - start_accidents}")

## Occupation du sol -----------------------------------------------------------------------------
print("Start occupation")
occupation_sol = occ.load_data(engine)
occupation_sol = occ.clean(occupation_sol)
end_occupation = t.time()
print(f"End occupation_sol in {end_occupation - end_accidents}")

## Population ------------------------------------------------------------------------------------
print("Start population")
population = pop.load_data(engine)
population = pop.clean(population)
end_population = t.time()
print(f"End population in {end_population - end_occupation}")

## Revenus ---------------------------------------------------------------------------------------
print("Start revenus")
revenus = rev.load_data(engine)
revenus = rev.clean(revenus)
end_revenus = t.time()
print(f"End revenus in {end_revenus - end_population}")



## Exportation dans la DB SQL Server #############################################################

def export_to_DB(dict_dfs):
    for df_name, df in dict_dfs.items():
        df.to_sql(df_name, engine, schema='silver', if_exists='replace', index=False)

print("Start exporting")
accidents.to_sql("accidents", engine, schema='silver', if_exists='replace', index=False)
occupation_sol.to_sql("occupation_par_commune", engine, schema='silver', if_exists='replace', index=False)
export_to_DB(population)
export_to_DB(revenus)

print(f"\nEnds all task in {end_revenus - start_accidents}")
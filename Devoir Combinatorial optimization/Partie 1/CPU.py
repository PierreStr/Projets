import os
import time
import TabuSearch as ts
import Data as data



# Boucle sur tous les fichiers du dossier
for filename in os.listdir("Instances"):
    file_path = os.path.join("Instances", filename)  # Chemin complet du fichier
    if os.path.isfile(file_path):  # Vérifie que c'est un fichier
        if str(filename)[5] == '5':
            instance = data.InstanceData(filename) 
            # Mesurer le temps d'exécution
            start_time = time.time()
            _, _, result = ts.best_VC_tabu_search(instance, 100)
            end_time = time.time()

            print(f"Fichier : {filename}")
            print(f"Résultat : {result}")
            print(f"Temps d'exécution : {end_time - start_time:.6f} secondes\n")
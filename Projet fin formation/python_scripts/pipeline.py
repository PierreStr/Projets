from datetime import datetime as dt


"""########################################
#                                         #
#                PIPELINE                 #
#                                         #
########################################"""


"""
Objectif :

    Lancer l'entièreté du modèle médaillion

"""

print("##############################")
print("   Construction du bronze     ")
print("##############################")
start_bronze = dt.now()
print(f"\n\nDate et heure de lancement : {start_bronze:%Y-%m-%d %Hh%M %Ssec}")

import raw_to_bronze

end_bronze = dt.now()
print(f"Date et heure de fin : {end_bronze:%Y-%m-%d %Hh%M %Ssec}")
total = int((end_bronze - start_bronze).total_seconds())

print(f"Durée totale : "
      f"{total // 3600:02d}h"
      f"{(total % 3600) // 60:02d} "
      f"{total % 60:02d}sec")


print("\n")
print("##############################")
print("   Construction du silver     ")
print("##############################")
print(f"\n\nDate et heure de lancement : {end_bronze:%Y-%m-%d %Hh%M %Ssec}")

import bronze_to_silver

end_silver = dt.now()
print(f"Date et heure de fin : {end_silver:%Y-%m-%d %Hh%M %Ssec}")
total = int((end_silver - end_bronze).total_seconds())

print(f"Durée totale : "
      f"{total // 3600:02d}h"
      f"{(total % 3600) // 60:02d} "
      f"{total % 60:02d}sec")


print("\n")
print("##############################")
print("    Construction du gold      ")
print("##############################")
print(f"\n\nDate et heure de lancement : {end_silver:%Y-%m-%d %Hh%M %Ssec}")

import silver_to_gold

end_gold = dt.now()

print(f"Date et heure de fin : {end_gold:%Y-%m-%d %Hh%M %Ssec}")
total = int((end_gold - end_silver).total_seconds())

print(f"Durée totale : "
      f"{total // 3600:02d}h"
      f"{(total % 3600) // 60:02d} "
      f"{total % 60:02d}sec")


print("\n")
print("##############################")
print("            Total             ")
print("##############################")
print(f"\n\nDate et heure de lancement : {start_bronze:%Y-%m-%d %Hh%M %Ssec}")
print(f"Date et heure de fin : {end_gold:%Y-%m-%d %Hh%M %Ssec}")

total = int((end_gold - start_bronze).total_seconds())
print(f"Durée totale : "
      f"{total // 3600:02d}h"
      f"{(total % 3600) // 60:02d} "
      f"{total % 60:02d}sec")
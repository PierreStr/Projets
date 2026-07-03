

"""########################################
#                                         #
#             Data Base Link              #
#                                         #
########################################"""


"""
Objectif :

    Création de la connection entre python et la Data Base dans SQL Server. 
    Initialisation des schémas de la DB.

"""


## Importations nécessaires
from sqlalchemy import create_engine
from sqlalchemy import text


## Création du lien vers la data base

# Use Windows Authentication
server_name = 'GOSVDI511\\TFTIC'
db_name = 'Accidents_in_Belgium'

# Define the connection string for Windows Authentication
conn_str = f'DRIVER={{ODBC Driver 17 for SQL Server}};SERVER={server_name};DATABASE={db_name};Trusted_Connection=yes;'

# création du lien entre la DB et les dataframes d'ici
engine = create_engine(f'mssql+pyodbc:///?odbc_connect={conn_str}', fast_executemany=True)

# connection 
conn = engine.connect()


## Initialisation des schémas
with engine.begin() as conn:
    conn.execute(text("IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = 'bronze') EXEC('CREATE SCHEMA bronze')"))
    conn.execute(text("IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = 'silver') EXEC('CREATE SCHEMA silver')"))
    conn.execute(text("IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = 'gold') EXEC('CREATE SCHEMA gold')"))

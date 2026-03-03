import math



# Récupère les données
def parse_instance(path):
    # Open the file in reading mode
    with open("Instances//" + path, 'r') as file:
        # Reading the first variables to get the dimension of the problem
        # Number of localities
        n = int(file.readline().strip())

        # Number of VC
        m = int(file.readline().strip())

        # Trick: Empty line --> Going to the next line
        file.readline()

        # Declaring and pre-allocating data variables
        # Coordinates for the VC
        x_VC, y_VC = [0.0] * m, [0.0] * m
        # Coordinates for the localities
        x_loc, y_loc = [0.0] * n, [0.0] * n
        # Vehicles capacity
        Q = None
        # Depot capacities
        C = [0] * m
        # Population for each locality
        q = [0] * n
        # Opening costs for the VC
        f = [0.0] * m
        # Opening cost of a route / cost of a vehicle
        p = None
        # Type of cost: 0 or 1 (0 means that the costs are integer, 1 means real)
        tc = None
        # Budget
        B = None
        # Radius for each VC
        R = [0.0] * m
        # Localities where patients should be immunized in priority
        localities_with_high_priorities = []
        # Maximum number of vehicles
        M = None

        # Coordinates for the VC (x and y)
        for i in range(m):
            x, y = map(float, file.readline().strip().split())
            x_VC[i], y_VC[i] = x, y
        file.readline()

        # Coordinates for the localities
        for i in range(n):
            x, y = map(float, file.readline().strip().split())
            x_loc[i], y_loc[i] = x, y
        file.readline()

        # Vehicle capacity
        Q = int(file.readline().strip())
        file.readline()

        # Depot capacities
        for i in range(m):
            C[i] = int(file.readline().strip())
        file.readline()

        # Number of people in localities
        for i in range(n):
            q[i] = int(file.readline().strip())
        file.readline()

        # Opening costs for the VC
        for i in range(m):
            f[i] = float(file.readline().strip())
        file.readline()

        # Opening costs of a route (cost of a vehicle)
        p = float(file.readline().strip())
        file.readline()

        # 0 or 1 (0 means that the costs are integer, 1 means real)
        tc = int(file.readline().strip())
        file.readline()
        file.readline()  # Two line breaks

        # Budget
        B = float(file.readline().strip())
        file.readline()

        # Radius for each VC
        for k in range(m):
            R[k] = float(file.readline().strip())
        file.readline()

        # Localities where patients should be immunized in priority
        while True:
            line = file.readline().strip()
            if not line:
                break
            localities_with_high_priorities.append(int(line))

        # Maximum number of vehicles
        M = int(file.readline().strip())

    # Return the parsed data
    if tc == 1:
        return (n, m, x_VC, y_VC, x_loc, y_loc, Q, C, q, f, p, tc, B, R, localities_with_high_priorities, M)
    else:  # tc == 0
        return (n, m, x_VC, y_VC, x_loc, y_loc, Q, C, q, list(map(int, f)), int(p), tc, B, R, localities_with_high_priorities, M)




class InstanceData:
    def __init__(self, file_name, C_km=1):
        # Charger les données de l'instance
        self.J, self.I, self.x_VC, self.y_VC, self.x_loc, self.y_loc, self.Q, self.C, self.q, self.f, self.p, self.tc, self.B, self.R, self.localities_with_high_priorities, self.M = parse_instance(file_name)

        # Corriger les priorités
        self.localities_with_high_priorities = [i - 1 for i in self.localities_with_high_priorities]
        
        # Calculer la matrice de distances
        self.d = self._compute_distance_matrix()

        # Paramètres additionnels
        self.C_km = C_km

    def _compute_distance_matrix(self):
        """Calculer et stocker la matrice de distances"""
        d = [[0.0] * (self.I + self.J) for _ in range(self.I + self.J)]

        # Distance entre VCs
        for i in range(self.I):
            for j in range(self.I):
                d[i][j] = math.sqrt((self.x_VC[i] - self.x_VC[j])**2 + (self.y_VC[i] - self.y_VC[j])**2)

        # Distance entre VC et localité
        for i in range(self.I):
            for j in range(self.J):
                d[i][self.I + j] = math.sqrt((self.x_VC[i] - self.x_loc[j])**2 + (self.y_VC[i] - self.y_loc[j])**2)
                d[self.I + j][i] = d[i][self.I + j]

        # Distance entre localités
        for i in range(self.J):
            for j in range(self.J):
                d[self.I + i][self.I + j] = math.sqrt((self.x_loc[i] - self.x_loc[j])**2 + (self.y_loc[i] - self.y_loc[j])**2)

        return d
    
    def as_tuple(self):
        """Retourner toutes les données sous forme de tuple"""
        return (self.J, self.I, self.x_VC, self.y_VC, self.x_loc, self.y_loc,
                self.Q, self.C, self.q, self.f, self.p, self.tc, self.B, self.R,
                self.localities_with_high_priorities, self.M, self.C_km, self.d)

    def display(self):
        """Afficher les données"""
        print("Number of localities:", self.J)
        print("Number of VCs:", self.I)
        print("x coord for VCs:", self.x_VC)
        print("y coord for VCs:", self.y_VC)
        print("x coord for localities:", self.x_loc)
        print("y coord for localities:", self.y_loc)
        print("Max capacity for one MMT:", self.Q)
        print("Number of doses available:", self.C)
        print("Populations of localities:", self.q)
        print("Cost for opening VC:", self.f)
        print("Cost for opening a route:", self.p)
        print("tc:", self.tc)
        print("Budget available:", self.B)
        print("Radius of each VC:", self.R)
        print("List of localities with high priorities:", self.localities_with_high_priorities)
        print("Number of MMT available:", self.M)



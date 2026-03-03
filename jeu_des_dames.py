from tkinter import *
import math as m
import numpy as np



'''#####################################
         fonctions utiles
#####################################'''



def sign(x):

    return x/abs(x)


def distance (p1, p2):

    return (p1[0] - p2[0])**2 + (p1[1] - p2[1])**2


# return les 4 centres les plus proches de x et y (on commence par celui en haut G puis haut D puis bas G puis bas D)
def round_n_unite(x, y, n, width):

    r = n
    s = n
    while x-r > 0:
        r += 2*n 

    while y-s > 0:
        s += 2*n

    if r < width and s < width and s-2*n > 0 and r-2*n > 0:
        return [(r-2*n, s-2*n), (r, s-2*n), (r-2*n, s), (r, s)]

    elif r > width and s < width:
        return [(r-2*n, s), (r-2*n, s-2*n)]

    elif s > width and r < width:
        return [(r-2*n, s-2*n), (r, s-2*n)]

    elif s > width and r > width:
        return[(r-2*n, s-2*n)]

    elif r-2*n < 0 and s-2*n > 0 : 
        return [(r, s-2*n), (r, s)]

    elif r-2*n < 0 and s-2*n < 0:
        return [(r, s)]
    
    elif s-2*n < 0 and r-2*n > 0:
        return [(r-2*n, s), (r, s)]

    elif s-2*n < 0 and r > width:
        return [(r-2*n, s)]

    elif s > width and r-2*n < 0:
        return [(r, s-2*n)]


# return les coord du clique
def get_coordonate(event):
    x = event.x
    y = event.y
    return x, y


# return le centre du carré en fonction des coord
def get_center(x, y, width):

    centers = round_n_unite(x, y, 35, width)
    minimum = distance(centers[0], (x, y))
    sol = centers[0]

    for i in centers:
        if distance(i, (x, y)) < minimum:
            sol = i
            minimum = distance(i, (x, y))

    return sol



'''########################################
    Définition des classes Pion et Dame
########################################'''


class Pion: 

    BLACK = 1
    WHITE = -1

    def __init__(self, x, y, color):
        
        self.coord = (x, y)
        self.position = (self.coord[0]//70, self.coord[1]//70)
        self.color = color
        self.deplacement_matrix = self.upload_deplacement_matrix()
        self.id = []
        matrix_board[self.position[0]][self.position[1]] = self
        self.display()

    # print les infos du pion
    def __str__(self):
        
        if self.color == 1:
            couleur = "Noir"
        else:
            couleur = 'Blanc'
        return "Type: Pion\nPosition sur le plateau: {}\nCouleur: {}\n".format(self.position, couleur)

    # affiche le pion 
    def display(self):

        if self.color == Pion.BLACK:
            self.id.append(can1.create_oval(self.coord[0] - 31, self.coord[1] - 31, self.coord[0] + 31, self.coord[1] + 31, fill='black', width = 2))
            self.id.append(can1.create_oval(self.coord[0] - 18, self.coord[1] - 18, self.coord[0] + 18, self.coord[1] + 18, outline="grey", width = 3))
        else:
            self.id.append(can1.create_oval(self.coord[0] - 31, self.coord[1] - 31, self.coord[0] + 31, self.coord[1] + 31, fill='burlywood2', width = 2))
            self.id.append(can1.create_oval(self.coord[0] - 15, self.coord[1] - 15, self.coord[0] + 15, self.coord[1] + 15, outline="grey", width = 2))   

    # déplace le pion aux coordonnées x, y et l'affiche
    def move_to(self, x, y):

        self.withdraw()
        self.coord = (x, y)
        self.position = (x//70, y//70)
        matrix_board[self.position[0]][self.position[1]] = self
        self.display()

    # efface le pion
    def withdraw(self):
        
        matrix_board[self.position[0]][self.position[1]] = False

        for i in self.id:
            can1.delete(i)

        self.coord = (-1, -1)
        self.id = []

    # supprime le pion
    def delete(self):

        self.withdraw()
        self.position = (-1, -1)
        self.color = 0
        list_of_pions.remove(self)
        del self

    # return True (en [0]) si le pion peut en prendre un autre. Si oui en [1] on a la liste des pions prenables
    def can_take_a_pion(self, to_remove):

        can_take = False
        prenables = []

        for i in self.deplacement_matrix:
            for j in i:
                if isinstance(j[1], Pion) and j[1] not in to_remove:
                    can_take = True
                    prenables.append(j[1])

        return can_take, prenables

    # met à jour la matrice des déplacements possibles
    def upload_deplacement_matrix(self):
        i, j = self.position[0], self.position[1]
        d = self.color

        matrix = np.empty((10, 10, 2), dtype=object)
        for k in range(10):
            for l in range(10):
                matrix[k][l][0] = 0
                matrix[k][l][1] = False


        if 0 <= i + 1 < 10 and 0 <= j - d*1 < 10 and not matrix_board[i+1][j-d*1]:
            matrix[i+1][j-d*1][0] = 1

        if 0 <= i - 1 < 10 and 0 <= j - d*1 < 10 and not matrix_board[i-1][j-d*1]:
            matrix[i-1][j-d*1][0] = 1

        def upload_matrix(inc_i, inc_j):
            
            nonlocal matrix
            
            if (0 <= i + 2*inc_i < 10 and 0 <= j + 2*inc_j < 10 and not matrix_board[i+2*inc_i][j+2*inc_j] and matrix_board[i+inc_i][j+inc_j]
                and self.color != matrix_board[i+inc_i][j+inc_j].color):
                
                matrix[i+2*inc_i][j+2*inc_j][0] = 1
                matrix[i+2*inc_i][j+2*inc_j][1] = matrix_board[i+inc_i][j+inc_j]
        
        upload_matrix(1, 1)
        upload_matrix(-1, -1)
        upload_matrix(-1, 1)
        upload_matrix(1, -1)

        return matrix

    # met une dame si besoin et retourne True si le joueur peut toujours jouer, False si non:
    def make_dame(self, pos_x, pos_y, to_remove, taking_pion = False):

        if ((pos_y == 0 and not isinstance(self, Dame) and self.color == Pion.BLACK and not taking_pion)
            or (pos_y == 9 and not isinstance(self, Dame) and self.color == Pion.WHITE and not taking_pion)):

            couleur = self.color
            self.delete()
            dame = Dame(35+70*pos_x, 35+70*pos_y, couleur)
            matrix_board[dame.position[0]][dame.position[1]] = dame
            dame.display()
            list_of_pions.add(dame)

            return False 

        return self.can_take_a_pion(to_remove)[0]

    # déplace le pion aux coordonnées cliquées. (to_remove est une liste des pions à retirer à la fin du tour)
    def displace(self, event):

        global to_remove

        new_x, new_y = get_center(event.x, event.y, 700)
        pos_x, pos_y = new_x//70, new_y//70

        i = self.position[0]
        j = self.position[1]
        # deplacement en diagonale
        if self.deplacement_matrix[pos_x][pos_y][0] and not self.deplacement_matrix[pos_x][pos_y][1] and not self.can_take_a_pion(to_remove)[0]:
            self.move_to(new_x, new_y)
            self.deplacement_matrix = self.upload_deplacement_matrix()
            self.make_dame(pos_x, pos_y, to_remove)

            return False

        # prise d'un pion dans une des 4 directions
        elif (self.deplacement_matrix[pos_x][pos_y][0] and self.deplacement_matrix[pos_x][pos_y][1] 
              and self.deplacement_matrix[pos_x][pos_y][1] not in to_remove):
            
            to_remove.add(self.deplacement_matrix[pos_x][pos_y][1])
            self.move_to(new_x, new_y)

            # après avoir pris un pion la dame doit changer de direction
            if (isinstance(self, Dame) and ((sign(pos_x-i) == 1  and sign(pos_y-j) == 1) 
                                         or (sign(pos_x-i) == -1 and sign(pos_y-j) == -1))):
                self.directions = [[1, -1], [-1, 1]]

            elif isinstance(self, Dame):
                self.directions = [[1, 1], [-1, -1]]

            self.deplacement_matrix = self.upload_deplacement_matrix()
            
            return self.make_dame(pos_x, pos_y, to_remove, taking_pion=self.can_take_a_pion(to_remove)[0])
        
        return True



class Dame (Pion):


    def __init__(self, x, y, color, fictif = False):

        self.directions = [[1, 1], [-1, -1], [1, -1], [-1, 1]]
        super().__init__(x, y, color)


    # print les infos du pion
    def __str__(self):
        
        if self.color == 1:
            couleur = "Noir"
        else:
            couleur = 'Blanc'
        return "Type: Dame\nPosition sur le plateau: {}\nCouleur: {}\n".format(self.position, couleur)

    # affiche la dame
    def display(self):

        if self.color == Pion.BLACK:
            self.id.append(can1.create_oval(self.coord[0] - 31, self.coord[1] - 31, self.coord[0] + 31, self.coord[1] + 31, fill='black', width = 2))
            self.id.append(can1.create_oval(self.coord[0] - 18, self.coord[1] - 18, self.coord[0] + 18, self.coord[1] + 18, outline="grey", width = 3))
            
        else:
            self.id.append(can1.create_oval(self.coord[0] - 31, self.coord[1] - 31, self.coord[0] + 31, self.coord[1] + 31, fill='burlywood2', width = 2))
            self.id.append(can1.create_oval(self.coord[0] - 15, self.coord[1] - 15, self.coord[0] + 15, self.coord[1] + 15, outline="grey", width = 2))

        self.id.append(can1.create_rectangle(self.coord[0]-1, self.coord[1]-17, self.coord[0]+1, self.coord[1]+7, fill = 'goldenrod2', outline='goldenrod2'))
        self.id.append(can1.create_rectangle(self.coord[0]-3, self.coord[1]-15, self.coord[0]+3, self.coord[1]+7, fill = 'goldenrod2', outline='goldenrod2'))
        self.id.append(can1.create_rectangle(self.coord[0]-5, self.coord[1]-11, self.coord[0]-3, self.coord[1]+9, fill = 'goldenrod2', outline='goldenrod2'))
        self.id.append(can1.create_rectangle(self.coord[0]+3, self.coord[1]-11, self.coord[0]+5, self.coord[1]+9, fill = 'goldenrod2', outline='goldenrod2'))
        self.id.append(can1.create_rectangle(self.coord[0]-7, self.coord[1]-7, self.coord[0]-5, self.coord[1]+9, fill = 'goldenrod2', outline='goldenrod2'))
        self.id.append(can1.create_rectangle(self.coord[0]+5, self.coord[1]-7, self.coord[0]+7, self.coord[1]+9, fill = 'goldenrod2', outline='goldenrod2'))
        self.id.append(can1.create_rectangle(self.coord[0]-9, self.coord[1]+3, self.coord[0]-7, self.coord[1]+11, fill = 'goldenrod2', outline='goldenrod2'))
        self.id.append(can1.create_rectangle(self.coord[0]-11, self.coord[1]+7, self.coord[0]-9, self.coord[1]+13, fill = 'goldenrod2', outline='goldenrod2'))
        self.id.append(can1.create_rectangle(self.coord[0]-13, self.coord[1]+9, self.coord[0]-11, self.coord[1]+13, fill = 'goldenrod2', outline='goldenrod2'))
        self.id.append(can1.create_rectangle(self.coord[0]+7, self.coord[1]+3, self.coord[0]+9, self.coord[1]+11, fill = 'goldenrod2', outline='goldenrod2'))
        self.id.append(can1.create_rectangle(self.coord[0]+9, self.coord[1]+7, self.coord[0]+11, self.coord[1]+13, fill = 'goldenrod2', outline='goldenrod2'))
        self.id.append(can1.create_rectangle(self.coord[0]+11, self.coord[1]+9, self.coord[0]+13, self.coord[1]+13, fill = 'goldenrod2', outline='goldenrod2'))
        self.id.append(can1.create_rectangle(self.coord[0]-15, self.coord[1]-7, self.coord[0]+15, self.coord[1]-5, fill = 'goldenrod2', outline='goldenrod2'))
        self.id.append(can1.create_rectangle(self.coord[0]-13, self.coord[1]-5, self.coord[0]+13, self.coord[1]-3, fill = 'goldenrod2', outline='goldenrod2'))
        self.id.append(can1.create_rectangle(self.coord[0]-9, self.coord[1]-3, self.coord[0]+9, self.coord[1]-1, fill = 'goldenrod2', outline='goldenrod2'))

        self.id.append(can1.create_oval(self.coord[0] - 31, self.coord[1] - 31, self.coord[0] + 31, self.coord[1] + 31, outline="goldenrod2", width = 3))

    # met à jour la matrice des déplacements possibles
    def upload_deplacement_matrix(self):

        i, j = self.position[0], self.position[1]

        matrix = np.empty((10, 10, 2), dtype=object)
        for k in range(10):
            for l in range(10):
                matrix[k][l][0] = 0
                matrix[k][l][1] = False

        def update_matrix(inc_i, inc_j):

            nonlocal matrix

            inc = 1
            while 0 <= i + inc * inc_i < 10 and 0 <= j + inc * inc_j < 10 and not matrix_board[i + inc * inc_i][j + inc * inc_j]:

                matrix[i + inc * inc_i][j + inc * inc_j][0] = 1
                inc += 1

            if 0 <= i + inc * inc_i < 10 and 0 <= j + inc * inc_j < 10:

                pion_voisin = matrix_board[i + inc * inc_i][j + inc * inc_j]
                inc += 1

                while (pion_voisin.color != self.color 
                    and 0 <= i + inc * inc_i < 10
                    and 0 <= j + inc * inc_j < 10
                    and not matrix_board[i + inc * inc_i][j + inc * inc_j]):

                    matrix[i + inc * inc_i][j + inc * inc_j][0] = 1
                    matrix[i + inc * inc_i][j + inc * inc_j][1] = pion_voisin
                    inc += 1

        for dir in self.directions:

            update_matrix(dir[0], dir[1])

        return matrix
    


'''##############################################
     Création de la grille et de la partie
##############################################'''



def ligne_vert():
    c_x = 0
    while c_x != width+c:
        can1.create_line(c_x,0,c_x,width,width=1,fill='black')
        c_x+=c


def ligne_hor():
    c_y = 0
    while c_y != width+c:
        can1.create_line(0,c_y,width,c_y,width=1,fill='black')
        c_y+=c


def damier(): #fonction dessinant le tableau
    ligne_vert()
    ligne_hor()


def selectionner(event):

    global turn
    global to_remove

    x = event.x
    y = event.y

    coord = get_center(x, y, 700)

    must_take_pion = False
    for i in list_of_pions:
        i.deplacement_matrix = i.upload_deplacement_matrix()
        if i.can_take_a_pion(to_remove)[0] and i.color == turn:
            must_take_pion = True
            break

    for i in list_of_pions:
        if i.color == turn and i.coord == coord:
            i.deplacement_matrix = i.upload_deplacement_matrix()
            if not must_take_pion or i.can_take_a_pion(to_remove)[0]:
                return i
            
    return current

def bouger(event, current):

    global turn

    continuer = current.displace(event)
    
    if not continuer:
        turn -= 2*turn

        for i in to_remove:
            i.delete()

        to_remove.clear()

        if isinstance(current, Dame):
            current.directions = [[1, 1], [-1, -1], [1, -1], [-1, 1]]


def clique(event):

    x = event.x
    y = event.y

    global current
    global turn

    # si il y a un pion sur le clique -> sélection du pion
    if matrix_board[x//70][y//70]:
        current = selectionner(event)

    # si non et que c'est la bonne couleur qui joue -> déplacer le pion
    elif isinstance(current, Pion) and current.color == turn:
        bouger(event, current)



# taille de la grille et des cases 
width = 700
c = width//10

fen1 = Tk()
fen1.title("Dames")

# Création du canevas (zone de dessin)
can1 = Canvas(fen1, width=width, height=width, bg='lemonchiffon1')
can1.grid(padx=5, pady=5, columnspan=2)

# Grillage de la zone
damier()

# définition de la matrice des positions des pions et de la liste des pions
matrix_board = np.empty((10, 10), dtype = object)
list_of_pions = set()

for i in range(10):
    for j in range(10):
        matrix_board[i][j] = False

        if (i+j) % 2 == 0:
            carre = can1.create_rectangle(i*70, j*70, (i+1)*70, (j+1)*70, fill = 'brown')

        if j < 4 and (i+j) % 2 == 0:
            pion = Pion(i*70 + 35, j*70 + 35, Pion.WHITE)
            list_of_pions.add(pion)

        if j > 5 and (i+j) % 2 == 0:
            pion = Pion(i*70 + 35, j*70 + 35, Pion.BLACK)
            list_of_pions.add(pion)

# pion courrant/sémectionné
current = None

# variable définissant la main (aux noirs ou blancs)
turn = Pion.BLACK

# set des pions à enlever 
to_remove = set()

# action définie par le clique gauche
can1.bind('<Button-1>', clique)

# Démarrage de la boucle principale Tkinter
fen1.mainloop()
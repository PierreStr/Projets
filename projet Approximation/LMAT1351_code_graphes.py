import numpy as np
import matplotlib.pyplot as mp
import LMAT1351_code_fonctions as p

import math as m

"""--------------------------------------------------------

LMAT1351 : Approximation : méthodes et théorie, projet 2023
Partie code, implémentation des fonctions des graphes

--------------------------------------------------------"""

#-----------------------------------------------------

#QUESTION 1 : Interpolation polynomiale  
#-----------------------------------------------------


# Argument: entrée x
# Return: l'évaluation de la fonction 1 / 1+25x**2

def quotient (x):
    return 1 / (1 + 25*x**2)


# Arguments: une fonction de x, liste des degrés d'interpolation désirés
# Affiche: UN graph avec les interpolations de la fonction donnée pour les degrés demandés, ansi que la fonction initiale

def graph_interpolation (funct, list_degre):
  
    figure, axes = mp.subplots()
    axes.set_xlabel("x")
    axes.set_ylabel("y")

    x1 = np.linspace(-1, 1, 1000)
    y1 = []
    for i in x1:
        y1.append(funct(i))

    axes.plot(x1, y1, color = 'red', label = "fonction interpolée")

    for j in list_degre:

        if j == 0:
            nodesList = [0]
        
        else:
            nodesList = np.linspace(-1, 1, j+1)

        values = []
        for i in nodesList:
            values.append(funct(i))

        y2 = []
        for k in x1:
            y2.append(p.interpolation(nodesList, values)(k))

        axes.plot(x1, y2, label = str(j+1)+" noeuds")

        y2.clear()
        values.clear()

    axes.legend()
    mp.show()


# Arguments: le nombre de noeuds désiré, la fonction ('quotient' pour le quotient et 'exp' pour l'exponentielle)
#           difference = False si on ne veut pas montrer la distance entre l'interpolation et la fonction
#           reste = False si on ne veut pas montrer le graphe du reste de Lagrange
# Affiche: Un graphe avec la distance entre l'interpolation et la fonction plus le reste de Lagrange

def comparaison_reste_Lagrange (nbr, fonction, difference = True, reste = True,):
    
    figure, axes = mp.subplots()
    axes.set_xlabel("x")
    axes.set_ylabel("y")

    nodesList = np.linspace(-1, 1, nbr)
    if difference:
        x1 = np.linspace(-1, 1, 10000)

        # construction des noeuds et de leurs évaluations
        values = []
        for i in nodesList:
            if fonction == "quotient":
                values.append(quotient(i))
            elif fonction == "exp":
                values.append(m.exp(i))
            else:
                print("Oups ! Try with 'exp' if you want with the exponential or 'quotient' if you want 1/1+25x**2.")
                break

        # différence entre la fonction interpolée et son interpolation
        y1 = []
        for k in x1:
            if fonction == "quotient":
                y1.append(abs(quotient(k) - p.interpolation(nodesList, values)(k)))
            elif fonction == "exp":
                y1.append(abs(m.exp(k) - p.interpolation(nodesList, values)(k)))
            else:
                break

        axes.plot(x1, y1, color = 'red', label = "distance à la fonction interpolée")

    if reste:
        x2 = np.linspace(-1, 1, 1000)

        reste_Lagrange = p.reste_Lagrange(nodesList, fonction)

        y2 = []
        for i in x2:
            y2.append(reste_Lagrange(i))

        axes.plot(x2, y2, color = 'blue', label = "reste de Lagrange")

    axes.legend()
    mp.show()
    
# Graphes demandés dans la question 1

#comparaison_reste_Lagrange(5, "exp")
#comparaison_reste_Lagrange(5, "quotient", reste = False)

#graph_interpolation(m.exp, range(0, 5))
#graph_interpolation(quotient, range(15, 16))
#graph_interpolation(abs, [5, 7, 9, 11])


#-----------------------------------------------------


#QUESTION 2 : Courbes de Bézier
#-----------------------------------------------------

"""!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

ATTENTION : je ne compte pas soumettre cette partie, je l'ai faite pour le plaisir

!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"""


# Arguments: la liste des points représentés par des numpy.array de taille 2
# Return: un graph représentant la courbe de Bézier associée aux points donnés

def graph_bezier(list):

    funct = p.bezier(list)

    figure, axes = mp.subplots()
    axes.set_xlabel("x")
    axes.set_ylabel("y")

    x = np.linspace(0, 1, 100)
    y = []
    for i in x:
        y.append(funct(i))
    x1 = []
    x2 = []
    for j in y:
        x1.append(j[0])
        x2.append(j[1])


    for i in range(len(list)):

        axes.scatter(list[i][0], list[i][1], label = "Pts"+str(i))


    axes.plot(x1, x2)
    axes.legend()
    mp.show()


# Quelques courbes de Bézier

#pts = []
#pts.append(np.array([3, -1]))
#pts.append(np.array([7, 5]))
#pts.append(np.array([3, 5]))
#pts.append(np.array([7, 5]))
#pts.append(np.array([1, 2]))

#graph_bezier(pts)


#-----------------------------------------------------


#QUESTION 3 : Splines d’interpolation
#-----------------------------------------------------


# Arguments: La fonction à interpolée ainsi que la liste des nombres de noeuds désirés ATTENTION: minimum 3 noeuds
# Affiche: Le graphe des splines d'interpolation constuites sur les nombres de noeuds voulus, ainsi que la fonction initiale

def graph_spline(funct, list_nbr_noeuds):

    figure, axes = mp.subplots()
    axes.set_xlabel("x")
    axes.set_ylabel("y")

    x1 = np.linspace(-1, 1, 1000)
    y1 = []
    for i in x1:
        y1.append(funct(i))

    axes.plot(x1, y1, color = 'red', label = "fonction interpolée")

    for j in list_nbr_noeuds:
        if j == 1:
            list = [0]

        else:
            list = np.linspace(-1, 1, j)

        values = []
        for i in list:
            values.append(funct(i))

        spline = p.splineinterpol(list, values)

        x = np.linspace(-1, 1, 1000)
        y = []

        for i in x:
            y.append(spline(i))

        axes.plot(x, y, label = "spline à "+str(j)+ " noeuds")

    axes.legend()
    mp.show()

def f (x):
    return m.exp(x)

#graph_spline(m.exp, range(3, 6))
#graph_spline(quotient, range(15, 17))
#graph_spline(quotient, range(4, 8))
#graph_spline(abs, range(15, 17))


#-----------------------------------------------------

#QUESTION 4 : Polynômes orthogonaux
#-----------------------------------------------------


# Arguments: Une liste des degrés des polynômes que l'on souhaite afficher
# Affiche: Le polynôme orthogonale tel que L(1) = 1 de degré n

def graph_polynome_legendre (list_degre):

    figure, axes = mp.subplots()
    axes.set_xlabel("x")
    axes.set_ylabel("y")

    x = np.linspace(-1, 1, 100)

    for i in list_degre:

        function = p.make_function_with_coeffs(p.polynome_orthogonaux(i))

        y = function(x)

        axes.plot(x, y, label = "Polynôme orthogonal de Legendre de degré " + str(i))

    axes.legend()
    mp.show()


# Arguments: n le degré du polynôme dont on calcule les racines ATTENTION: degré max = 18, sinon cela ne converge pas assez rapidement dans le calcule des racines (1000 steps)
#           hist = True si on veut montrer l'histograme 
#           subdivsion est le nombre de batonnet de l'histogramme
#           double = True si on veut montrer les racines du polynômes de degré n+1 en plus (entrelacement)
#
# Affiche: Soit les racines du polynômes de dergé n
#          Soit l'histogramme des racines du polynôme orthogonal de Legendre (L(1) = 1) de degré n avec 'subdivision' batonnets
#          Soit les racines des polynômes de degrés n et n+1 pour montrer la propriété d'entrelacement

def graph_racine_polynome_legendre(n, hist = False, subdivision = 9, double = False):


    figure, axes = mp.subplots()

    x = p.racine_polynome_legendre(n)
    y = [0]*n

    if hist:
        axes.set_xlabel("x")
        axes.set_ylabel("nombre de racines")
        axes.hist(x, range = (-1,1), bins = subdivision, rwidth = 0.5, label = "Distribution des racines du polynôme orthogonal de degré "+str(n))

    elif double:
        axes.set_xlabel("x")
        mp.yticks([0])
        x2 = p.racine_polynome_legendre(n+1)
        y2 = [0]*(n+1)
        axes.scatter(x, y, label = "Racines du polynôme orthogonal de degré "+str(n))
        axes.scatter(x2, y2, label = "Racines du polynôme orthogonal de degré "+str(n+1))

    else:
        axes.set_xlabel("x")
        mp.yticks([0])
        axes.scatter(x, y)

    axes.legend()
    mp.show()


# Arguments: /
# Affiche: Le graphe en 3D de 625|X+Y| / 1 + 25(X**2) + 25(Y**2) + 625(X**2)*(Y**2)

def borne_module_quotient():


    fig = mp.figure()
    ax = mp.axes(projection='3d')

    ax.set_xlabel("x")
    ax.set_ylabel("y")
    ax.set_zlabel("z")

    x = np.linspace(-1, 1, 1000)
    y = np.linspace(-1, 1, 1000)
    X, Y = np.meshgrid(x, y)
    Z = 625*(abs(X+Y))/(1 + 25*(X**2) + 25*(Y**2) + 625*(X**2)*(Y**2))

    ax.plot_surface(X, Y, Z)

    mp.show()
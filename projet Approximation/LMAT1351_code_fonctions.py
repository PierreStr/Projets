import math as m
import sympy as sp
import numpy as np
import mpmath as mat
from scipy.optimize import minimize
from numpy.linalg import inv, det
from numpy.linalg import eig

"""--------------------------------------------------------

LMAT1351 : Approximation : méthodes et théorie, projet 2023
Partie code, implémentation des fonctions demandées

--------------------------------------------------------"""

#-----------------------------------------------------

#QUESTION 1 : Interpolation polynomiale  
#-----------------------------------------------------


# Arguments: une variable x
# Return: L'évaluation de x en la fonction quotient: 1/1+25x**2

def quotient (x):

    return 1 / (1 + 25*x**2)

# Arguments: des coefficients trouvés préalablement, le noeud evalué, la liste des autres noeuds
# Return: l'evaluation de " C_0 + C_1(node - x_0) + C_2(node - x_0)(node - x_1) + ... "

def evaluation (coeff, node, nodesList):

    eval = coeff[0]

    if len(nodesList) > 0:
        eval += (node - nodesList[0]) * evaluation(coeff[1:], node, nodesList[1:])

    return eval


# Arguments: la liste des noeuds, l'indice auquel on est arrivé
# Return: le produit " (x_i - x_0)(x_i - x_1)..."

def prod (nodesList, i):

    prod = 1

    for j in range (i):
        prod *= (nodesList[i] - nodesList[j])

    return prod


# Arguments: une liste de noeuds et leur évaluation en la fonction interpolée
# Return: une fonction qui est le polinôme d'interpolation

def interpolation (nodesList, values):

    coeff = []

    for i in range (len(nodesList)):

        if i == 0:
            coeff.append(values[0])

        else:
            coeff.append((values[i] - evaluation(coeff, nodesList[i], nodesList[:i-1]))/prod(nodesList, i))

    # Arguments: Une variable x
    # Return: L'évaluation du polynôme calculer en x
    def polynome (x):
        return evaluation(coeff, x, nodesList[:-1])
    
    return polynome


# Arguments: la liste des noeuds d'interpolation
# Return: la fonction W_n+1 du reste de Lagrange: (x - x0) ... (x - xn)

def w_n1 (nodesList):

    def function (x):
        prod = 1

        for i in range (len(nodesList)):
            prod *= abs(x - nodesList[i])

        return prod

    return function


# Arguments: la liste des noeuds d'interpolation et la fonction souhaitée ('quotient' pour 1/1+25x**2, 'exp' pour l'exponentielle)
# Return: une fonction de x représentant la borne théorique de du reste de Lagrange

def reste_Lagrange (nodesList, function):

    # Ici n représente la taille de la liste des noeuds et donc le degré de l'interpolation + 1
    n = len(nodesList)

    coeff_facto = 1/(m.factorial(n))

    x = sp.symbols('x')

    # Calcule de la norme infini du quotient ou de l'exponentielle
    if function == "quotient":

        # n ème dérivée du quotient
        n1_deriv = sp.diff(-1/(1+25*x**2), x, n)
        f = sp.lambdify(x, n1_deriv)

        # Pour prendre le max on minimise l'opposé, (on a pris l'opposé lors du calcul de la dérivée) 
        # Notons que le max se trouve toujours dans [-1, 1] nous n'avons donc pas besoin de nous en occuper
        maximum = - (minimize(f, 0).fun)

    elif function == "exp":
        maximum = m.exp(1)
    
    else:
        print("Oups ! Try with 'exp' if you want with the exponential or 'quotient' if you want 1/1+25x**2.")

    w = w_n1(nodesList)

    # fonction évaluant la borne théorique
    def function (x):
        return coeff_facto * maximum * w(x)
    
    return function


#-----------------------------------------------------

#QUESTION 2 : Courbes de Bézier
#-----------------------------------------------------

"""!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

ATTENTION : je ne compte pas soumettre cette partie

!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"""


# Arguments: la liste des points, ceux-ci doivent être représentés par des numpy.array de taille 2
# Return: une fonction qui interprète la courbe de Bézier associée à ces points

def bezier (liste_pts):

    def function (t):

        sum = 0
        k = len(liste_pts)
        for i in range (k):
            sum += (m.factorial(k-1)/(m.factorial(i)*m.factorial(k-1-i))) * ((1-t)**(k-1-i)) * (t**i) * liste_pts[i]
        return sum
    
    return function



#-----------------------------------------------------

#QUESTION 3 : Splines d’interpolation
#-----------------------------------------------------


# Arguments: une variable x et la liste des noeuds d'interpolation
# Return: L'indice de l'emplacement de x en fonction des noeuds (si x < x_j, alors x est à l'indice j)
# Utilisations: dans la définition de la fonction spline

def FindIndice (x, nodes):
    
    for i in range(len(nodes)):
        if x < nodes[i]:
            return i
    return len(nodes)-1


# Arguments: une variable x, les lists des noeuds et des coefficients recherchés z, c et d, ainsi que l'indice actuel (S change aux bords)
# Return: La fonction S(x) donnant la spline d'interpolation

def S(x, listx, listz, listc, listd, i):

    # si nous sommes au tout début, on a z_0 = 0
    if i-1 == 0:
        sum = ((x - listx[i-1])**3 / (6*(listx[i] - listx[i-1]))) * listz[i-1]
        sum += listc[i-1]*(x - listx[i - 1]) + listd[i-1]*(listx[i] - x)

    elif i == len(listx)-1:
        sum = ((listx[i] - x)**3 / (6*(listx[i] - listx[i-1]))) * listz[i-2]
        sum += listc[i-1]*(x - listx[i - 1]) + listd[i-1]*(listx[i] - x)
    
    # A la fin on a z_n = 0
    else:
        sum = ((listx[i] - x)**3 / (6*(listx[i] - listx[i-1]))) * listz[i-2]
        sum += ((x - listx[i-1])**3 / (6*(listx[i] - listx[i-1]))) * listz[i-1]
        sum += listc[i-1]*(x - listx[i - 1]) + listd[i-1]*(listx[i] - x)
    
    return sum


# Arguments: une variable x, les lists des noeuds et des coefficients recherchés z, c et d, ainsi que l'indice actuel (S change aux bords)
# Return: La dérivée en x de la fonction S(x) donnant la spline d'interpolation

def Sprim(x, listx, listz, listc, listd, i):

    # si nous sommes au tout début, on a z_0 = 0
    if i-1 == 0:
        sum = (listz[i-1]*(x - listx[i-1])**2) / (2*(listx[i] - listx[i-1]))
        sum += listc[i-1] - listd[i-1]

    elif i == len(listx)-1:
        sum = (-listz[i-2]*(listx[i] - x)**2) / (2*(listx[i] - listx[i-1]))
        sum += listc[i-1] - listd[i-1]

    # A la fin on a z_n = 0
    else:
        sum = (-listz[i-2]*(listx[i] - x)**2) / (2*(listx[i] - listx[i-1]))
        sum += (listz[i-1]*(x - listx[i-1])**2) / (2*(listx[i] - listx[i-1]))
        sum += listc[i-1] - listd[i-1]

    return sum


# Arguments: la liste des noeuds d'interpolation ainsi que leur évaluation en la fonction interpolée
# Return: Une fonction de x donnant la spline d'interpolation

def splineinterpol(nodes, values):

    # Initialisation des variables/symbols sympy
    z_var = []
    c_var = []
    d_var = []

    for i in range(1, len(nodes)):
        if i != len(nodes)-1:
            z_var.append('z'+str(i))
        c_var.append('c'+str(i))
        d_var.append('d'+str(i))
    
    listz = sp.symbols(z_var)
    listc = sp.symbols(c_var)
    listd = sp.symbols(d_var)

    # mise en équation comme décrit dans le rapport
    equations = []

    # conditions S_(x_j) et S+(x_j) = f(x_j)
    equations.append(S(nodes[0], nodes, listz, listc, listd, 1) - values[0])

    for i in range (1, len(nodes)-1):
        equations.append(S(nodes[i], nodes, listz, listc, listd, i) - values[i])
        equations.append(S(nodes[i], nodes, listz, listc, listd, i+1) - values[i])
    
    equations.append(S(nodes[len(nodes)-1], nodes, listz, listc, listd, len(nodes)-1) - values[len(nodes)-1])

    # conditions S_'(x_j) = S+'(x_j)
    for i in range (1, len(nodes)-1):
        equations.append(Sprim(nodes[i], nodes, listz, listc, listd, i) - Sprim(nodes[i], nodes, listz, listc, listd, i+1))

    solutions = sp.solve(equations, dict = True)

    # Arguments: une variable x
    # Return: l'évaluation de x en la spline d'interpolation
    def spline(x):
        j = FindIndice(x, nodes)
        coeff_c = []
        coeff_d  =[]
        coeff_z = []

        for k in range (len(nodes)-1):
            coeff_c.append(solutions[0][listc[k]])
            coeff_d.append(solutions[0][listd[k]])

            if k != len(nodes)-2:
                coeff_z.append(solutions[0][listz[k]])

        return S(x, nodes, coeff_z, coeff_c, coeff_d, j)

    return spline



#-----------------------------------------------------

#QUESTION 4 : Polynômes orthogonaux
#-----------------------------------------------------
#
# Dans cette section, tout les polynômes sont représentés par la liste des coefficients du plus haut degré au plus petit


# Arguments: 2 listes, où list1 est plus grande que list2 (ou égales)
# Return: la soustraction éléments par éléments de list1 et list2 en partant de la droite
#       c'est à dire: l1 = [a, b, c, d] - l2 = [e, f, g] == [a, b-e, c-f, d-g]
# Utilisations: Dans la construction du polynôme avec les coefficients de récurrence 

def list_soustraction (list1, list2):

    newlist = list1
    difference = len(list1) - len(list2)

    for i in range (len(list2)):
        newlist[difference + i] -= list2[i]

    return newlist


# Arguments: Une list et un scalaire
# Return: La liste dont tout les éléments ont été multipliés par x
# Utilisations: Dans la normalisation des polynômes 
#            + dans la construction du polynôme avec les coefficients de récurrence 

def list_multiply_by_scalar (list, x):

    newlist = []

    for i in range(len(list)):
        newlist.append(x*list[i])
    
    return newlist


# Arguments: 2 polynômes
# Return: Une liste des coefficients du produit de ces polynômes
# Utilisations: Dans la construction des coefficients de récurrence
#              + dans la construction du polynôme avec les coefficients de récurrence 

def multiply_polynome (polynome1, polynome2):

    product = [0]*(len(polynome1)+len(polynome2)-1)

    for i in range(len(polynome1)):
        for j in range(len(polynome2)):
            product[i+j] += polynome1[i]*polynome2[j]

    return product


# Arguments: les coefficients d'un polynôme
# Return: L'évaluation de son intégrale sur l'intervalle [-1, 1] (poids de Legendre)
# Utilisations: Dans la normalisation des polynômes
#              + dans la construction des coefficients de récurrence

def integrale_poids_Legendre(coeff):

    # Travaillant avec des polynômes, on peut calculer explicitement leur intégrale sur [-1, 1] par une somme
    integrale = 0

    for i in range(len(coeff)):
        integrale += (coeff[i] * 2 * ((len(coeff)-i)%2)) / (len(coeff)-i)
    
    return integrale


# Arguments: n le nombre de coefficients du polynôme recherché et un polynôme connu de degré inférieur ou égale à n
# Return: Une liste de coefficients repésentant ceux de l'équation recherchée.

# Cette équation est définie en réalisant le produit du p_n inconnu avec polynome et en intègrant le tout sur l'intervalle [-1, 1]
# les inconnues sont donc les coefficients de p_n.

def mise_en_equation (polynome, n):

    # de nouveau, travaillant avec des polynômes, on peut calculer leur intégrale explicitement. 
    # Dans ce cas on en extrait une expression linéaire des coefficients du polynôme inconnu
    coeffs = [0]*n

    for i in range(len(polynome)):
        for j in range(n):
            coeffs[j] += 2 * polynome[i] * 1/(len(polynome)-i + n-j-1) *((len(polynome)-i + n-j-1)%2)

    return np.array(coeffs)


# Arguments: n le nombre de coefficients du polynôme recherché et une liste de polynômes connu de degré inférieur ou égale à n
# Return: Une matrice et un vecteur donnant un système linéaire avec toutes les équations interprétant l'orthogonalité des polynômes 
#         de la liste avec p_n, plus l'équation représentant la condition L(1) = 1

def systeme_lineaire (list_of_polynomes, n):

    matrice = []

    # Equations représentant les produits scalaires
    for i in list_of_polynomes:
        matrice.append(mise_en_equation(i, n))

    # Evaluation du polynôme en 1
    matrice.append(np.array([1]*n))

    # Définition du vecteur, on veut que leurs produits scalaires valent 0 et que L(1) = 1
    vecteur = [0]*n
    vecteur[-1] = 1

    return (np.array(matrice), np.array(vecteur))


# Arguments: n le degré du polynôme orthogonal tel que L(1) = 1
# Return: Le polynôme orthogonal recherché

def polynome_orthogonaux (n):

    if n == -1:
        return [0]

    elif n == 0:
        return [1]
    
    else:

        # Défini par récurrence, il nous faut calculer les n-1 premiers polynômes d'abord
        list_of_polynomes = []

        for i in range (n):
            list_of_polynomes.append(polynome_orthogonaux(i))
        
        # On réalise le système linéaire puis on le résoud
        systeme = systeme_lineaire(list_of_polynomes, n+1)
        solution = np.linalg.solve(systeme[0], systeme[1])

        return list(solution)


# Arguments: n le degré du polynôme orthogonal normé
# Return: Le polynôme orthogonal normé recherché

def polynome_orthonormaux (n):

    pn = polynome_orthogonaux(n)

    # Calcule de la norme 
    norme_pn = m.sqrt(integrale_poids_Legendre(multiply_polynome(pn, pn)))

    # Si celle-ci vaut 0 on évite une erreur 'division by 0'
    if norme_pn != 0:
        return list_multiply_by_scalar(pn, 1/norme_pn)

    else:
        return pn


# Arguments: n le nombre de coefficients de récurrence voulu, normaux = True pour les coefficients dans le cas normaux
# Return: Un triplet de listes des coefficients de récurrence alpha, beta, gamma dans cet ordre

def coefficient_recurrence_Legendre (n, normaux = False):

    alpha = []
    beta = []
    gamma = []

    for i in range(n):

        # Si on veut les coefficients de récurrence pour les polynômes normés
        if normaux:
            pn_1 = polynome_orthonormaux(i-1)
            pn = polynome_orthonormaux(i)
            pn1 = polynome_orthonormaux(i+1)

            x_pn = multiply_polynome(([1, 0]), pn)

            prod_n_1 = multiply_polynome(x_pn, pn_1)        
            prod_n = multiply_polynome(x_pn, pn)        
            prod_n1 = multiply_polynome(x_pn, pn1)

            # les coeficients sont définits par les produits scalaires
            # gamma : <x_pn, pn-1>
            gamma.append(integrale_poids_Legendre(prod_n_1))
            # beta : <x_pn, pn>
            beta.append(integrale_poids_Legendre(prod_n))
            # alpha : <x_pn, pn+1>
            alpha.append(integrale_poids_Legendre(prod_n1))
        
        # Pour ceux-ci, les détails sont écrits dans le rapport
        else:
            pn = polynome_orthogonaux(i)
            pn_1 = polynome_orthogonaux(i-1)            

            x_pn = multiply_polynome(([1, 0]), pn)
        
            prod_n = multiply_polynome(x_pn, pn)        
            prod_n_1 = multiply_polynome(x_pn, pn_1)

            norme_carre_pn = integrale_poids_Legendre(multiply_polynome(pn, pn))
            norme_carre_pn_1 = integrale_poids_Legendre(multiply_polynome(pn_1, pn_1))

            # si la norme vaut 0, on évite une erreur 'division by 0'
            if norme_carre_pn_1 != 0:
                gamma.append(integrale_poids_Legendre(prod_n_1)/norme_carre_pn_1)

            else:
                gamma.append(integrale_poids_Legendre(prod_n_1))

            beta.append(integrale_poids_Legendre(prod_n)/norme_carre_pn)

            # On évite ici des calcules inutiles, comme on sait que pn(1) = 1 pour tout n,
            # on a que            1 = a_n + b_n + g_n
            # ce qui implique     a_n = 1 - b_n - g_n
            alpha.append(1 - beta[i] - gamma[i])

    return alpha, beta, gamma


# Arguments: Les coefficients de récurrence alpha, beta, gamma
# Return: Une liste des coefficients du polynôme construit grâce aux coefficients de récurrence

def coeffs_polynome_by_coeffs (alpha, beta, gamma):

    # Application de la formule de récurrence donnée dans le cours: x pn = a pn+1 + b pn + g pn-1

    if len(alpha) -1 == -1:
        return [0]
    
    elif len(alpha) == 1:
        return [1]
    
    else:
        pn = coeffs_polynome_by_coeffs(alpha[:-1], beta[:-1], gamma[:-1])
        pn_1 = coeffs_polynome_by_coeffs(alpha[:-2], beta[:-2], gamma[:-2])

        x_pn = multiply_polynome([1, 0], pn)
        beta_n_pn = list_multiply_by_scalar(pn, beta[-1])
        gamma_n_pn_1 = list_multiply_by_scalar(pn_1, gamma[-1])

        x_beta_n_pn = list_soustraction(x_pn, beta_n_pn)
        x_beta_n_pn_gamma_n_pn = list_soustraction(x_beta_n_pn, gamma_n_pn_1)

        return list_multiply_by_scalar(x_beta_n_pn_gamma_n_pn, 1/alpha[-1])
    

# Arguments: Une liste de coefficients
# Return: Une fonction évaluant le polynôme donné par les coefficients

def make_function_with_coeffs (coeffs):

    def polynome (x):

        value = 0
        for i in range(len(coeffs)):
            value += coeffs[i]*(x**(len(coeffs)-i-1))

        return value
    
    return polynome


# Arguments: Les coefficients de récurrence alpha, beta, gamma
# Return: Une fonction évaluant le polynôme construit sur base des coefficients de récurrence

def polynome_made_by_coeffs_recurrence (alpha, beta, gamma):

    coeffs = coeffs_polynome_by_coeffs(alpha, beta, gamma)

    return make_function_with_coeffs(coeffs)


# Arguments: n le degré du polynôme dont on veut les racines
# Return: Une liste triée des racines du polynôme voulu

def racine_polynome_legendre (n):

    coeffs = polynome_orthogonaux(n)

    roots = mat.polyroots(coeffs, maxsteps = 1000)

    return roots

#-----------------------------------------------------

#QUESTION 5 :  Intégration par quadrature Gaussienne
#-----------------------------------------------------


# Arguments: une liste et une matrice
# Return: La liste triée et la matrice permutée de sorte que les lignes correspondent toujours aux éléments de la liste

def my_sort(liste, matrice):
    # Créer une liste d'indices triés
    indices_tries = sorted(range(len(liste)), key=lambda k: liste[k])

    # Trier la liste
    liste_triee = [liste[i] for i in indices_tries]

    # Permuter les lignes de la matrice
    matrice_permutee = [matrice[i] for i in indices_tries]

    return liste_triee, matrice_permutee


# Arguments: les coefficients de récurrence alpha, beta, gamma
# Return: La matrice de Jacobi qui est tridiagonale avec alpha sur la diagonale supérieure, 
#         beta sur la diagonale et gamma sur la diagonale inférieure


def matrice_jacobi(alpha, beta, gamma):
    n = len(alpha)

    matrice = []

    for i in range(n):
        matrice.append([0]*n)

        for j in range(n):
            if i+1 == j:
                matrice[i][j] += alpha[i]
            elif i == j:
                matrice[i][j] += beta[i]
            elif i-1 == j:
                matrice[i][j] += gamma[i]

    return matrice


# Arguments: n tel que n-1 est le degré de la quadrature (commence à 0) et un triplet de listes des coefficients de récurrence
# Return: Une liste avec les poids de Christoffel 

def Ak(n, coeffs):

    # calcules des valeurs et vecteurs propres
    matrix = np.array(matrice_jacobi(coeffs[0], coeffs[1], coeffs[2]))
    eigen = np.linalg.eig(matrix)

    # Tri des valeurs propres dans l'ordre croissant en gardant la correspondance avec les vecteurs propres
    vect_propres = my_sort(eigen[0], np.transpose(eigen[1]))[1]

    list_Ak = []

    for i in range(n):
        list_Ak.append(2*(vect_propres[i][0])**2)

    return list_Ak


# Arguments: n tel que n-1 est le degré de la quadrature
# Return: la liste des poids de Christoffel pour les coefficients de récurrence de Legendre

def Ak_legendre (n):

    coeffs = coefficient_recurrence_Legendre(n, normaux = True)

    return Ak(n, coeffs)


# Arguments: Une fonction f que l'on veut intégrer et n le degré de la quadrature (le bon cette fois)
# Return: L'approximation de l'intégrale de f

def approximation_integrale(f, n):

    integrale = 0
    roots = racine_polynome_legendre(n+1)
    Ak = Ak_legendre(n+1) 

    for i in range(len(roots)):
        integrale += Ak[i]*f(roots[i])

    return integrale


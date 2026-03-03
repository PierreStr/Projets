import math
import gurobipy as grb
import VisualRepresentation as VR
from Data import parse_instance
import GreedyAlgorithm
import numpy as np
import numpy as np
import matplotlib.pyplot as plt
import time

# Parameters -------------------------------------
# file_name : name of the instance used
# C_km : cost for traveling 1 unite of ditance (In order to have a more restrictive constraint of budget)
# time_out : a time out for the solver (in seconds)
# bool_print : true for visualizing and printing the solution
#
# Return : the solution find by Gurobi for the model "OptVax_Dual_Lagrangien"



def Solve_OptVax_Dual_Lagrangien(file_name, C_km, time_out, bool_print, warm_start, lambda_var):

    nb_0_u=0

    # Store every variables of the file
    J, I, x_VC, y_VC, x_loc, y_loc, Q, C, q, f, p, tc, B, R, localities_with_high_priorities, M = parse_instance(file_name)

    # Ensure that the localities with high priorities are in the range {0, ..., J-1}
    for i in range(len(localities_with_high_priorities)):
        localities_with_high_priorities[i] -= 1

    # Initialization of the distance matrix (I + J) x (I + J)
    d = [[0.0] * (I + J) for _ in range(I + J)]

    # Computing the distance between two VCs
    for i in range(I):
        for j in range(I):
            d[i][j] = math.sqrt((x_VC[i] - x_VC[j])**2 + (y_VC[i] - y_VC[j])**2)

    # Computing the distance between a VC and a locality
    for i in range(I):
        for j in range(J):
            d[i][I+j] = math.sqrt((x_VC[i] - x_loc[j])**2 + (y_VC[i] - y_loc[j])**2)
            d[I+j][i] = math.sqrt((x_VC[i] - x_loc[j])**2 + (y_VC[i] - y_loc[j])**2)

    # Computing the distance between two localities
    for i in range(J):
        for j in range(J):
            d[I + i][I + j] = math.sqrt((x_loc[i] - x_loc[j])**2 + (y_loc[i] - y_loc[j])**2)



    # Display the data
    if bool_print:
        print("Number of localities:", J)
        print("Number of VCs:", I)
        print("x coord for VCs:", x_VC)
        print("x coord for VCs:", y_VC)
        print("x coord for localities:", x_loc)
        print("x coord for localities:", y_loc)
        print("Max capacity for one MMT:", Q)
        print("Number of doses available:", C)
        print("Populations of localities:", q)
        print("Cost for opening VC:", f)
        print("Cost for opening a route:", p)
        print("tc:", tc)
        print("Budget available:", B)
        print("Radius of each VC:", R)
        print("List of localities with high priorities:", localities_with_high_priorities)
        print("Number of MMT available:", M)



    # Initialization the Gurobi model
    model = grb.Model("Vaccination Center Optimization")



    # Declaration of variables
    x = model.addVars(I, vtype=grb.GRB.BINARY, name="x")  # Opening VC
    y = model.addVars(I, J, vtype=grb.GRB.BINARY, name="y")  # Localities covered by VC
    v = model.addVars(M, vtype=grb.GRB.BINARY, name="v")  # Opening route
    t = model.addVars(M, J, vtype=grb.GRB.BINARY, name="t")  # Localities covered by MMT
    w = model.addVars(M, I + J, I + J, vtype=grb.GRB.BINARY, name="w")  # Movements of MMTs
    u = model.addVars(M, J, vtype=grb.GRB.INTEGER, lb=0, ub=J+1, name="u")  # Locality's order of passage



    #  -------- 1. One and only one vaccination center must be opened
    model.addConstr(grb.quicksum(x[i] for i in range(I)) == 1)


    # -------- 2 - 3. A locality can be either : not visited, visited by a MMT or within the range of an opened VC
    for j in range(J):
        model.addConstr(grb.quicksum(t[m, j] for m in range(M)) +
                        grb.quicksum(y[i, j] for i in range(I)) <= 1, f"Locality_{j}_covered")


    # -------- 4. Each route represents a mobilized medical team

    # At most one MMT can arrive/leave a locality
    for k in range(I, I+J):
        model.addConstr(grb.quicksum(w[m, j, k] for m in range(M) for j in range(I+J) if j!= k) <= 1)
        model.addConstr(grb.quicksum(w[m, k, j] for m in range(M) for j in range(I+J) if j!= k) <= 1)

    # Ensuring that no MMT goes from location i to location i
    for m in range(M):
        for i in range(I+J):
            model.addConstr(w[m, i, i] == 0)

    # The number of MMTs leaving a location is equal to the number of MMTs arriving to this location
    for k in range(I+J):
        for m in range(M):
            model.addConstr(grb.quicksum(w[m, j, k] for j in range(I+J) if j!= k) == grb.quicksum(w[m, k, j]  for j in range(I+J) if j!= k))


    # Ensuring that the variable t[m, j] is correctly define, i.e. : t[m, j] = 1 if MMT m goes to locality j, 0 if not
    for m in range(M):
        for j in range(I, I+J):
            model.addConstr(t[m, j-I] == grb.quicksum(w[m, k, j] for k in range(I+J)))


    model.addConstrs((u[m, k-I] >= w[m,j,k] for m in range(M) for k in range(I,I + J) for j in range(I,I + J)), name="max_u")


    # -------- 5. Each route must respect the capacity constraint
    for m in range(M):
        model.addConstr(grb.quicksum(q[j]*t[m, j] for j in range(J)) <= (Q)*v[m])


    # -------- 6. The capacity for the VC is only for specifically conditioned doses delivered by MMTs
    for i in range(I):
        model.addConstr(x[i] * grb.quicksum(q[j] * t[m, j] for j in range(J) for m in range(M)) <= C[i], f"VC_{i}_capacity")


    # -------- 7. The starting point and the end point are the same for each route and turns out to be the chosen VC
    for i in range(I):
        model.addConstr(grb.quicksum(w[m, j, i] for m in range(M) for j in range(I+J)) == x[i]*grb.quicksum(v[m] for m in range(M)))
        model.addConstr(grb.quicksum(w[m, i, j] for m in range(M) for j in range(I+J)) == x[i]*grb.quicksum(v[m] for m in range(M)))


    # -------- 8. All prioritized localities have to be covered
    for j in localities_with_high_priorities:
        model.addConstr(grb.quicksum(t[m, j] for m in range(M)) +
                        grb.quicksum(y[i, j] for i in range(I)) == 1, f"Priority_locality_{j}_covered")


    #------------ Additional constraints

    # Budget constraint
    model.addConstr(grb.quicksum(p*v[m] for m in range(M)) + grb.quicksum(f[i]*x[i] for i in range(I)) + grb.quicksum(d[i][j]*w[m, i, j]*C_km for m in range(M) for i in range(I+J) for j in range(I+J)) <= B)

    # Ensuring that the variable y[i, j] is correctly define, i.e. : y[i, j] = 1 if VC i is open and i cover j, 0 if not
    for i in range(I):
        for j in range(J):
            model.addConstr(y[i, j] * (d[i][j+I] - R[i])*x[i] <= 0, f"y_constraint_1_{i}_{j}")
            model.addConstr((1 - y[i, j]) * (R[i] - d[i][j+I])*x[i] <= 0, f"y_constraint_2_{i}_{j}")
            model.addConstr(y[i, j]*(1-x[i])==0, f"y_constraint_3_{i}_{j}")




    # -------  OBJECTIVE FUNCTION MODIFIED TO INCLUDE CONSTRAINT VIA LAGRANGE MULTIPLIERS --------


    model.setObjective(
        grb.quicksum(q[j] * (
            grb.quicksum(t[m, j] for m in range(M)) +
            grb.quicksum(y[i, j] for i in range(I))
        ) for j in range(J))
        +
        grb.quicksum(
            lambda_var[m,k-I,j-I]*(u[m, j-I] + (I+J) * (1 - w[m, k, j]) - w[m, k, j] - u[m, k-I])
            for m in range(M)
            for k in range(I, I + J)
            for j in range(I, I + J)
            if k != j)

        ,sense=grb.GRB.MAXIMIZE)



    # -------------- Resolving the model -------------- #

    model.setParam('TimeLimit', time_out) # Time out for the compilation
    model.optimize()



    # ---------- Visualizing the solution ------------- #

    if bool_print:
        VR.visualize_solution(w, I, J, x, M, file_name)

    w_values = {(m, k, j): w[m, k, j].X for m in range(M) for k in range(I, I + J) for j in range(I, I + J) if k != j}
    u_values = {(m, k): u[m, k].X for m in range(M) for k in range(0, J)}
    model_obj = model.objVal

    return model_obj, w_values, u_values



def subgradient_method(max_iter, alpha, lambda_var):
    g_i_values=[]
    obj_values = []

    for i in range(max_iter):


        model_obj, w_values, u_values = Solve_OptVax_Dual_Lagrangien(file_name, C_km, time_out, bool_print, warm_start, lambda_var)

        g_i = np.zeros_like(lambda_var)

        for m in range(M):
            for k in range(I, I + J):
                for j in range(I, I + J):
                    if k != j:

                        w_value = w_values.get((m, k, j))
                        u_value_k = u_values.get((m, k-I))
                        u_value_j = u_values.get((m, j-I))


                        g_i[m, k - I, j - I] = u_value_j  + (I + J) * (1 - w_value) - w_value - u_value_k


        obj_values.append(math.floor(model_obj))
        g_i_values.append(np.linalg.norm(g_i))

        # Updating dual multipliers
        alpha_i = 0.01/(100 + i) #0.01/(i+alpha) # alpha/ (i + 100)
        lambda_var = np.maximum(lambda_var - alpha_i * g_i, 0)


    return obj_values




def plot_subgradient_method_alpha(obj_values_list, alpha_list, Optvax1, Optvax2_LP):

    colors = ['blue', 'orange', 'green', 'purple', 'brown', 'pink', 'cyan', 'gray']

    fig, ax = plt.subplots(figsize=(4, 4))


    for i, (obj_values, alpha) in enumerate(zip(obj_values_list, alpha_list)):
        color = colors[i % len(colors)]
        ax.plot(obj_values, color=color, label=f'alpha={alpha}', linestyle='-', linewidth=0.8, marker='o', markersize=4)

    ax.axhline(Optvax1, color='red', label='Optvax1', linewidth=0.8)
    ax.axhline(Optvax2_LP, color='green', label='Optvax2_LP', linewidth=0.8)

    ax.set_xlabel('Itérations')
    ax.legend(loc='best')

    plt.show()


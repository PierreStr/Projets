import math
import gurobipy as grb
import VisualRepresentation as VR
from Data import parse_instance
import GreedyAlgorithm

import time

# Parameters -------------------------------------
# file_name : name of the instance used
# C_km : cost for traveling 1 unite of ditance (In order to have a more restrictive constraint of budget)
# time_out : a time out for the solver (in seconds)
# bool_print : true for visualizing and printing the solution
# 
# Return : the solution find by Gurobi for the model "OptVax1"
def Solve_OptVax1(file_name, C_km, time_out, bool_print, warm_start = False):

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
    u = model.addVars(M, I + J, vtype=grb.GRB.CONTINUOUS, name="u")  # Locality's order of passage 



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

    # Dealing with the sub-turns
    for m in range(M):
        for j in range(I, I+J):
            for k in range(I, I+J):
                if k != j:
                    model.addConstr(w[m, k, j] + u[m, k] <= u[m, j] + (I+J)*(1-w[m, k, j]))

    # Ensuring that the variable t[m, j] is correctly define, i.e. : t[m, j] = 1 if MMT m goes to locality j, 0 if not
    for m in range(M):
        for j in range(I, I+J):
            model.addConstr(t[m, j-I] == grb.quicksum(w[m, k, j] for k in range(I+J)))


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



    # Setting the objective function
    model.setObjective(
        grb.quicksum(q[j] * (grb.quicksum(t[m, j] for m in range(M)) +  # MMTs couvrant la localité j
            grb.quicksum(y[i, j] for i in range(I))  # VCs couvrant la localité j
        ) for j in range(J)),  # Pour chaque localité j
        sense=grb.GRB.MAXIMIZE
    )



    if warm_start:
        _, _, VC_chosen, best_routes = GreedyAlgorithm.Heuristic_Algorithm(file_name, C_km, False)
        warm_x, warm_y, warm_v, warm_t, warm_w, warm_u = GreedyAlgorithm.build_solution(VC_chosen, best_routes, file_name)

        # warm start x_i
        for i in range(I):
            x[i].start = warm_x[i]
        
        # warm start y_ij
        for i in range(I):
            for j in range(J):
                y[i, j].start = warm_y[i][j]

        # warm start v_m
        for m in range(M):
            v[m].start = warm_v[m]

        # warm start t_mj
        for m in range(M):
            for j in range(J):
                t[m, j].start = warm_t[m][j]
        
        # warm start w_mjk
        for m in range(M):
            for j in range(I + J):
                for k in range(I + J):
                    w[m, j, k].start = warm_w[m][j][k]
        
        # warm start u_mj
        for m in range(M):
            for j in range(I + J):
                u[m, j].start = warm_u[m][j]
        


    # -------------- Resolving the model -------------- #

    model.setParam('TimeLimit', time_out) # Time out for the compilation
    model.optimize()

    # Print the solution
    if bool_print:
        print("\nThe model could vaccine", round(model.objVal), "citizens")


    # ---------- Visualizing the solution ------------- #

    if bool_print:
        VR.visualize_solution(w, I, J, x, M, file_name)

    return model.objVal


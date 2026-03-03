import math
import matplotlib.cm as cm
from Data import parse_instance
from VisualRepresentation import visualize_greedy_solution
import time



def greedy_algorithm(VC_chosen, constructed_routes, instance):

    J, I, x_VC, y_VC, x_loc, y_loc, Q, C, q, f, p, tc, B, R, localities_with_high_priorities, M, C_km, d = instance.as_tuple()

    covered_localities = {
        j for j in range(J)
        if math.sqrt((x_loc[j] - x_VC[VC_chosen])**2 + (y_loc[j] - y_VC[VC_chosen])**2) <= R[VC_chosen]
    }

    remaining_localities = {
        j for j in range(J)
        if math.sqrt((x_loc[j] - x_VC[VC_chosen])**2 + (y_loc[j] - y_VC[VC_chosen])**2) > R[VC_chosen]
    }

    priority_localities_remaining = {
        j for j in localities_with_high_priorities
        if math.sqrt((x_loc[j] - x_VC[VC_chosen])**2 + (y_loc[j] - y_VC[VC_chosen])**2) > R[VC_chosen]
    }

    mmt_routes = []
    vaccinated_quantity = sum(q[j] for j in covered_localities)
    vaccinated_quantity_MMT = 0
    remaining_budget = B - f[VC_chosen]  # Remaining budget
    current_mmt = 0


    # Process if routes are already constructed after local_search

    d_constructed_routes = 0
    MMT_constructed_routes = 0

    for route in constructed_routes:
        d_constructed_routes += calculate_total_distance(route, VC_chosen, instance)
        MMT_constructed_routes += 1
        for j in route:
            vaccinated_quantity_MMT += q[j]
            vaccinated_quantity += q[j]
            if j in remaining_localities:
                remaining_localities.remove(j)

        mmt_routes.append(route)


    remaining_budget = remaining_budget - MMT_constructed_routes * p - d_constructed_routes * C_km  # Remaining budget
    current_mmt = MMT_constructed_routes

    while remaining_localities and remaining_budget >= p and current_mmt < M:
        current_capacity = 0
        remaining_budget -= p  # Open a new route
        current_route = []
        current_position = VC_chosen

        while remaining_localities:
            nearest_locality = None
            min_distance = float('inf')

            for loc in remaining_localities:
                distance = d[current_position][I + loc]
                if distance < min_distance:
                    min_distance = distance
                    nearest_locality = loc

            distance_to_locality = min_distance

            # Check capacity and budget constraints
            if current_capacity + q[nearest_locality] > Q or vaccinated_quantity_MMT + q[nearest_locality] > C[VC_chosen] or remaining_budget - distance_to_locality * C_km - d[VC_chosen][I + nearest_locality] * C_km < 0:
                break

            current_route.append(nearest_locality)
            current_capacity += q[nearest_locality]
            vaccinated_quantity_MMT += q[nearest_locality]
            remaining_budget -= distance_to_locality * C_km
            remaining_localities.remove(nearest_locality)
            current_position = I + nearest_locality  # Update current position

        # Add the last distance to the budget
        remaining_budget -= d[VC_chosen][current_position] * C_km
        mmt_routes.append(current_route)
        vaccinated_quantity += current_capacity
        current_mmt += 1

    # Check priority localities
    visited_priority_localities = set()

    for route in mmt_routes:
        visited_priority_localities.update(route)

    # Check if all priority localities have been visited
    if all(loc in visited_priority_localities for loc in priority_localities_remaining):
        return mmt_routes, remaining_budget, vaccinated_quantity
    else:
        return greedy_algorithm2(VC_chosen, constructed_routes, instance)



def greedy_algorithm2(VC_chosen, constructed_routes, instance):

    J, I, x_VC, y_VC, x_loc, y_loc, Q, C, q, f, p, tc, B, R, localities_with_high_priorities, M, C_km, d = instance.as_tuple()

    covered_localities = {
        j for j in range(J)
        if math.sqrt((x_loc[j] - x_VC[VC_chosen])**2 + (y_loc[j] - y_VC[VC_chosen])**2) <= R[VC_chosen]
    }
    remaining_localities = {
        j for j in range(J)
        if math.sqrt((x_loc[j] - x_VC[VC_chosen])**2 + (y_loc[j] - y_VC[VC_chosen])**2) > R[VC_chosen]
    }

    priority_localities_remaining = {
        j for j in localities_with_high_priorities
        if math.sqrt((x_loc[j] - x_VC[VC_chosen])**2 + (y_loc[j] - y_VC[VC_chosen])**2) > R[VC_chosen]
    }

    mmt_routes = []
    vaccinated_quantity = sum(q[j] for j in covered_localities)
    vaccinated_quantity_MMT = 0
    remaining_budget = B - f[VC_chosen]  # Remaining budget
    current_mmt = 0

    d_constructed_routes = 0
    MMT_constructed_routes = 0

    for route in constructed_routes:
        d_constructed_routes += calculate_total_distance(route, VC_chosen, instance)
        MMT_constructed_routes += 1
        for j in route:
            vaccinated_quantity_MMT += q[j]
            vaccinated_quantity += q[j]
            if j in remaining_localities:
                remaining_localities.remove(j)

        mmt_routes.append(route)

    remaining_budget = remaining_budget - MMT_constructed_routes * p - d_constructed_routes * C_km  # Remaining budget
    current_mmt = MMT_constructed_routes

    # Add priority localities first in the MMT routes
    while remaining_localities and remaining_budget >= p and current_mmt < M:
        current_capacity = 0
        remaining_budget -= p  # Open a new route
        current_route = []
        current_position = VC_chosen

        # First, visit the priority localities
        for loc in priority_localities_remaining:
            if loc in remaining_localities:
                distance_to_locality = d[current_position][I + loc]
                # Check capacity and budget constraints
                if current_capacity + q[loc] <= Q and remaining_budget - distance_to_locality * C_km - d[VC_chosen][loc] * C_km >= 0:
                    current_route.append(loc)
                    current_capacity += q[loc]
                    vaccinated_quantity_MMT += q[loc]
                    remaining_budget -= distance_to_locality * C_km
                    remaining_localities.remove(loc)
                    current_position = I + loc

        # Then, visit other localities if budget and capacity allow
        while remaining_localities:
                nearest_locality = None
                min_distance = float('inf')

                for loc in remaining_localities:
                    distance = d[current_position][I + loc]
                    if distance < min_distance:
                        min_distance = distance
                        nearest_locality = loc

                distance_to_locality = min_distance

                # Check capacity and budget constraints
                if current_capacity + q[nearest_locality] > Q or vaccinated_quantity_MMT + q[nearest_locality] > C[VC_chosen] or remaining_budget - distance_to_locality * C_km - d[VC_chosen][I + nearest_locality] * C_km < 0:
                    break

                current_route.append(nearest_locality)
                current_capacity += q[nearest_locality]
                vaccinated_quantity_MMT += q[nearest_locality]
                remaining_budget -= distance_to_locality * C_km
                remaining_localities.remove(nearest_locality)
                current_position = I + nearest_locality  # Update current position

            # Add the last distance to the budget
        remaining_budget -= d[VC_chosen][current_position] * C_km
        mmt_routes.append(current_route)
        vaccinated_quantity += current_capacity
        current_mmt += 1

    return mmt_routes, remaining_budget, vaccinated_quantity




def calculate_total_distance(route, VC_chosen, instance):
        
    J, I, x_VC, y_VC, x_loc, y_loc, Q, C, q, f, p, tc, B, R, localities_with_high_priorities, M, C_km, d = instance.as_tuple()

    total_distance = 0
    current_position = VC_chosen

    for loc in route:

        total_distance += d[current_position][I + loc]
        current_position = I + loc

    total_distance += d[current_position][VC_chosen]
    return total_distance



def local_search(VC_chosen, routes, instance):
    def two_opt(route):
        best_route = route
        best_distance = calculate_total_distance(route, VC_chosen, instance)

        for i in range(1, len(route) - 1):
            for j in range(i + 1, len(route)):

                new_route = route[:i] + list(reversed(route[i:j+1])) + route[j+1:]
                new_distance = calculate_total_distance(new_route, VC_chosen, instance)

                if new_distance < best_distance:
                    best_route = new_route
                    best_distance = new_distance

        return best_route

    # Apply to all routes
    improved_routes = [route.copy() for route in routes]
    for idx, route in enumerate(improved_routes):
        improved_routes[idx] = two_opt(route)
    return improved_routes


#-------------Loop through all possible vaccination centers----------------#


def best_VC_greedy_algorithm(instance):
    max_vaccinated_quantity = 0
    best_VC = None
    max_remaining_budget = 0
    best_VC_routes = []
    for i in range(instance.I):
        routes, remaining_budget, vaccinated_quantity = greedy_algorithm(i, [], instance)
        if vaccinated_quantity > max_vaccinated_quantity or (vaccinated_quantity == max_vaccinated_quantity and remaining_budget > max_remaining_budget):
                max_vaccinated_quantity = vaccinated_quantity
                max_remaining_budget = remaining_budget
                best_VC = i
                best_VC_routes = routes
    return best_VC, best_VC_routes, max_vaccinated_quantity


def best_VC_local_search(instance):
    max_vaccinated_quantity = 0
    best_VC = None
    max_remaining_budget = 0
    best_VC_routes = []
    for i in range(instance.I):
        routes, remaining_budget, vaccinated_quantity = greedy_algorithm(i, [], instance)
        improved_routes = local_search(i, routes, instance)
        routes, remaining_budget, vaccinated_quantity = greedy_algorithm(i, improved_routes[:-1], instance)

        if vaccinated_quantity > max_vaccinated_quantity or (vaccinated_quantity == max_vaccinated_quantity and remaining_budget > max_remaining_budget):
                max_vaccinated_quantity = vaccinated_quantity
                max_remaining_budget = remaining_budget
                best_VC = i
                best_VC_routes = routes
    return best_VC, best_VC_routes, max_vaccinated_quantity



def Heuristic(bool_print, instance):

    J, I, x_VC, y_VC, x_loc, y_loc, Q, C, q, f, p, tc, B, R, localities_with_high_priorities, M, C_km, d = instance.as_tuple()

    #Greedy algorithm without prior knowledge of which vaccination center is fixed

    start_cpu_time = time.process_time()

    VC_chosen, routes, vaccinated_quantity = best_VC_greedy_algorithm(I, J, x_VC, y_VC, x_loc, y_loc, q, Q, d, B, C, f, R, localities_with_high_priorities, instance)

    end_cpu_time = time.process_time()
    total_cpu = end_cpu_time - start_cpu_time
    sol_time_greedy = (round(vaccinated_quantity), total_cpu)


    if bool_print:
        visualize_greedy_solution(I, J, VC_chosen, R,  x_VC, y_VC, x_loc, y_loc, routes, localities_with_high_priorities)
        print("Chosen center:", VC_chosen, "| Vaccinated quantity after running the greedy algorithm:", vaccinated_quantity)



    #Greedy algorithm + local_search without prior knowledge of which vaccination center is fixed

    start_cpu_time = time.process_time()

    VC_chosen, routes, vaccinated_quantity_local_search = best_VC_local_search(I, J, x_VC, y_VC, x_loc, y_loc, q, Q, d, B, C, f, R, localities_with_high_priorities, instance)

    end_cpu_time = time.process_time()
    total_cpu = end_cpu_time - start_cpu_time
    sol_time_local_search = (round(vaccinated_quantity_local_search), total_cpu)

    if bool_print:
        visualize_greedy_solution(I, J, VC_chosen, R,  x_VC, y_VC, x_loc, y_loc, routes, localities_with_high_priorities)
        print("Chosen center:", VC_chosen, "| Vaccinated quantity after running the local search algorithm:", vaccinated_quantity_local_search)


    return sol_time_greedy, sol_time_local_search, VC_chosen, routes



# From the chosen VC and the routes given by the local search we build a solution of our model
# return x_i, y_ij, v_m, t_mj, w_mjk, u_mj as define in the model with a feasible solution
def build_solution(best_VC, best_VC_routes, file_name):

    print(best_VC_routes)

    best_VC_routes = best_VC_routes[0 : -1]

    # distance de x = (x_0, x_1) à y = (y_0, y_1)
    def distance(x, y):
        return math.sqrt((x[0] - y[0])**2 + (x[1] - y[1])**2)

    # Give the indices of elem in list, return False if elem not in list
    def give_indices(elem, list):
        i = 0
        for e in list:
            if (elem == e):
                return I
            else:
                i += 1
        return False

    # Store every variables of the file
    J, I, x_VC, y_VC, x_loc, y_loc, Q, C, q, f, p, tc, B, R, localities_with_high_priorities, M = parse_instance(file_name)

    # Build x_i
    x = []
    for i in range(I):
        x.append(1 if i == best_VC else 0)

    # build y_ij
    y = []
    for i in range(I):
        row = []
        if i == best_VC:
            for j in range(J):
                row.append(1 if distance((x_VC[i], y_VC[i]), (x_loc[j], y_loc[j])) <= R[i] else 0)
        else:
            row = [0 for j in range(J)]
        y.append(row)

    # Build v_m
    nbr_mmt = len(best_VC_routes)
    v = [1] * nbr_mmt + [0] * (M - nbr_mmt)

    # Build t_mj
    t = [
        [1 if j in best_VC_routes[m] else 0 for j in range(J)] for m in range(nbr_mmt)
    ] + [[0] * J for _ in range(M - nbr_mmt)]

    # Build w_mjk
    w = [[[0 for _ in range(I + J)] for _ in range(I + J)] for _ in range(M)]

    for m in range(nbr_mmt):
        for j in range(I + J):
            for k in range(I + J):
                ind = give_indices(j, best_VC_routes[m])

                # Vérifications de sécurité
                if ind is False:
                    continue  # Ignorer si la fonction retourne une valeur invalide

                if (
                    ind < len(best_VC_routes[m]) - 1 and best_VC_routes[m][ind + 1] == k - I
                ) or (
                    j == best_VC and k - I == best_VC_routes[m][0]
                ) or (
                    k == best_VC and j - I == best_VC_routes[m][-1]
                ):
                    w[m][j][k] = 1

    # build u_mj
    u = [[0 for j in range(I + J)] for m in range(M)]
    for m in range(nbr_mmt):
        count = 1
        for j in best_VC_routes[m]:
            u[m][I + j] = count
            count += 1

    return x, y, v, t, w, u

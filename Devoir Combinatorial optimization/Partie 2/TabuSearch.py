import GreedyAlgorithm as ga
import math as m
from VisualRepresentation import visualize_greedy_solution
import Data as data



'''-------------------------------
        SOLUTION CLASS
--------------------------------'''


# Class that implement a solution of the problem (maybe not feasible !!)
class Solution:

    def __init__(self, VC_chosen, routes, instance):

        # Data of the instance
        self.J, self.I, self.x_VC, self.y_VC, self.x_loc, self.y_loc, self.Q, self.C, self.q, self.f, self.p, self.tc, self.B, self.R, self.localities_with_high_priorities, self.M, self.C_km, self.d = instance.as_tuple()
        self.localities_with_high_priorities = set(self.localities_with_high_priorities)
        self.instance = instance
        # Solution brought
        self.VC = VC_chosen
        self.routes = routes

        # Usefull quantities
        self.covered_by_VC = set([j for j in range(self.J) if m.sqrt((self.x_loc[j] - self.x_VC[self.VC])**2 + (self.y_loc[j] - self.y_VC[self.VC])**2) <= self.R[self.VC]])
        self.not_covered_by_VC = set([j for j in range(self.J) if m.sqrt((self.x_loc[j] - self.x_VC[self.VC])**2 + (self.y_loc[j] - self.y_VC[self.VC])**2) > self.R[self.VC]])
        self.remaining_budget = self.B - self.count_budget()        
                
        
    # Define equality between 2 solutions
    def __eq__(self, other):

        return all(tuple(route) == tuple(other_route) for route, other_route in zip(self.routes, other.routes))


    def __hash__(self):

        return hash(tuple(tuple(route) for route in self.routes))
    

    # Create a copy of the solution
    def copy(self):
        return Solution(
            VC_chosen=self.VC,
            routes=[route[:] for route in self.routes],
            instance=self.instance
        )


    # Return the total cost of the solution
    def count_budget(self):

        distance = 0
        for route in self.routes:
            distance += ga.calculate_total_distance(route, self.VC, self.instance)
        budget = self.C_km*distance +  self.f[self.VC] + self.p*len(self.routes)

        return budget


    # Return True if the solution is feasible, false otherwise
    def IsFeasible(self):

        # Budget constraint
        budget = self.count_budget()
        
        # Capacity of the MMT constraint (and the VC)
        capacity = []
        for i in range(len(self.routes)):
            capacity.append(sum(self.q[j] for j in self.routes[i]))

        sum_cap = 0
        for cap in capacity:
            sum_cap += cap
            if cap > self.Q:
                return False

        return budget <= self.B and sum_cap <= self.C[self.VC]


    # Return the evaluation of the solution
    def evaluate(self):

        # Locality covered by the VC
        covered_localities = self.covered_by_VC

        # Locality covered by the MMTs
        capacity = []
        for i in range(len(self.routes)):
            capacity.append(sum(self.q[j] for j in self.routes[i]))

        return sum(capacity) + sum(self.q[j] for j in covered_localities)


    # Return the list of all locality not covered by the solution
    def not_covered(self):

        # locality not covered by VC
        not_covered = self.not_covered_by_VC

        # locality not covered by routes
        for route in self.routes:
            not_covered = not_covered - set(route)

        return not_covered



'''-------------------------------
        MOVEMENTS CLASS
--------------------------------'''


# Class of the movement that merge the two given routes
class Merge_Movement:

    # which_routes : tuple containing the two routes we want to change
    # which_loc : tuple containing two integer in [1, len(route)-1] (for respective route)
    #             these integer indicate where we have to merge the two routes
    def __init__(self, initial_solution, which_routes, which_loc):
        
        self.initial_solution = initial_solution
        self.which_routes = which_routes
        self.which_loc = which_loc

        # new_solution = initial_solution if new_solution not feasible
        temp = self.giveNewSolution()
        if temp.IsFeasible():
            self.new_solution = temp
        else:
            self.new_solution = initial_solution


    def __str__(self):
        return "Merge the two routes " + str(self.which_routes[0]) + " and " + str(self.which_routes[1])
        

    # Takes un tuple containing 2 lists listA, listB with a tuple of ind (a, b) and gives 2 new lists such that : 
    #   The first list is listA[0, ..., a]+ listB]b, ..., 0],
    #   The second list is listA[n, ..., a[ + listB[b, ..., n]
    def merge(self):
        
        listA = self.initial_solution.routes[self.which_routes[0]]
        listB = self.initial_solution.routes[self.which_routes[1]]

        a = self.which_loc[0]
        b = self.which_loc[1]

        semilistA = listA[a:len(listA)]
        semilistA.reverse()
        semilistB = listB[0:b]
        semilistB.reverse()

        newListA = listA[0:a] + semilistB
        newListB = semilistA + listB[b:len(listB)]

        return newListA, newListB
    

    # Return the solution : initial solution + merge movement
    def giveNewSolution(self):

        new_sol = self.initial_solution.copy()
        new_routes = self.merge()

        new_sol.routes[self.which_routes[0]] = new_routes[0]
        new_sol.routes[self.which_routes[1]] = new_routes[1]
        new_sol.remaining_budget = new_sol.B - new_sol.count_budget()

        if new_sol.IsFeasible():
            return new_sol

        return self.initial_solution
    

    # Evaluate the new solution
    def evaluate(self):

        return self.initial_solution.evaluate()



# Class of the movement that replace the given locality by the best other locality we can find (can be the given locality)
class Replace_Movement:

    # route : integer < len(initial solution.routes), is the position of the loc_to_replace's route
    # loc_to_replace : the locality we want to replace
    def __init__(self, initial_solution, route, loc_to_replace):
        
        self.initial_solution = initial_solution
        self.route = route
        self.loc_to_replace = loc_to_replace

        self.best_loc = self.replace()
        
        # new_solution = initial_solution if new_solution not feasible
        if self.best_loc == loc_to_replace:
            temp = initial_solution
        else:
            temp = self.giveNewSolution()

        if temp.IsFeasible():
            self.new_solution = temp
        else:
            self.new_solution = initial_solution


    def __str__(self):
        return "Replace the location " + str(self.loc_to_replace) + " by the new location " + str(self.best_loc)


    # Gives the locality that is the best feasible replacement (can be the current locality) 
    def replace(self):
        
        best_loc = self.loc_to_replace
        best_eval = self.initial_solution.evaluate()

        for loc in self.initial_solution.not_covered():

            eval = self.evaluate(loc)
            if self.IsFeasible(loc) and eval > best_eval:
                best_eval = eval
                best_loc = loc
        
        return best_loc


    # Return the solution : initial solution + replace movement
    def giveNewSolution(self):

        if self.best_loc == self.loc_to_replace:
            return self.initial_solution

        sol = self.initial_solution.copy()

        for i in range(len(sol.routes[self.route])):
            if sol.routes[self.route][i] == self.loc_to_replace:
                sol.routes[self.route][i] = self.best_loc    
                break

        sol.remaining_budget = sol.B - sol.count_budget()

        if sol.IsFeasible:
            return sol
        
        return self.initial_solution


    # Evaluate the new solution if we replace the locality by loc. If loc is None we take the best locality we found
    def evaluate(self, new_loc = None):

        sol = self.initial_solution

        if new_loc is None:
            return sol.evaluate() - sol.q[self.loc_to_replace] + sol.q[self.best_loc]

        return sol.evaluate() - sol.q[self.loc_to_replace] + sol.q[new_loc]
    

    # Return True is the solution is feasible while replacing the locality by loc, False if not
    def IsFeasible(self, loc):

        sol = self.initial_solution

        for i in range(len(sol.routes[self.route])):
            if sol.routes[self.route][i] == self.loc_to_replace:

                if 0 < i < len(sol.routes[self.route])-1:
                    before = sol.routes[self.route][i-1] + sol.I
                    after = sol.routes[self.route][i+1] + sol.I
                    break
                elif i == len(sol.routes[self.route])-1:
                    before = sol.routes[self.route][i-1] + sol.I
                    after = sol.VC
                    break
                else:
                    before = sol.VC
                    after = sol.routes[self.route][i+1] + sol.I
                    break
        d = self.initial_solution.instance.d
        diff = d[before][loc] + d[loc][after]

        # Capacity of the MMT constraint (and the VC)
        capacity = []
        for i in range(len(sol.routes)):
            capacity.append(sum(sol.q[j] for j in sol.routes[i] if j != loc))
            
        capacity.append(sol.q[loc])
        sum_cap = 0
        for cap in capacity:
            sum_cap += cap
            if cap > sol.Q:
                return False
            
        return diff*sol.C_km <= sol.remaining_budget and sum_cap <= sol.C[sol.VC]



# Class of the movement that add a locality to the given route
class Add_Movement:

    # route : integer < len(initial solution.routes)
    def __init__(self, initial_solution, route):
        
        self.initial_solution = initial_solution
        self.route = route

        self.best_loc = self.add()

        # new_solution = initial_solution if new_solution not feasible
        if self.best_loc == None:
            temp = initial_solution
        else:
            temp = self.giveNewSolution()

        if temp.IsFeasible():
            self.new_solution = temp
        else:
            self.new_solution = initial_solution


    def __str__(self):
        return "Add the location " + str(self.best_loc) + " to the route " + str(self.route)


    # Gives the best locality that we can add (can be None) 
    def add(self):
        
        best_loc = None
        best_eval = self.initial_solution.evaluate()

        for loc in self.initial_solution.not_covered():

            eval = self.evaluate(loc)
            if self.IsFeasible(loc) and eval > best_eval:
                best_eval = eval
                best_loc = loc
        
        return best_loc


    # Return un new route with location added in route such that the cost is minimized
    def insert_min_budget(self, location, sol):

        min_budget = float('inf')
        best_position = None
        route = sol.routes[self.route]

        for i in range(len(route) + 1):  
            temp_route = route[:i] + [location] + route[i:]

            temp_budget = ga.calculate_total_distance(temp_route, sol.VC, sol.instance)
            
            if temp_budget < min_budget:
                min_budget = temp_budget
                best_position = i

        optimal_route = route[:best_position] + [location] + route[best_position:]

        return optimal_route, best_position


    # Return the solution : initial solution + add movement
    def giveNewSolution(self):

        if self.best_loc == None:
            return self.initial_solution
        
        sol = self.initial_solution.copy()

        sol.routes[self.route], _ = self.insert_min_budget(self.best_loc, sol)

        sol.remaining_budget = sol.B - sol.count_budget()

        if sol.IsFeasible():
            return sol
        
        return self.initial_solution


    # Evaluate the solution with the loc that is added to the given route
    def evaluate(self, new_loc = None):

        sol = self.initial_solution

        if new_loc is None and self.best_loc is not None:
            return sol.evaluate() + sol.q[self.best_loc]
        
        elif new_loc is None and self.best_loc is None:
            return sol.evaluate()

        return sol.evaluate() + sol.q[new_loc]
    

    # Return True if the solution is feasible if we add loc in the given route
    def IsFeasible(self, loc):

        sol = self.initial_solution

        _, i = self.insert_min_budget(loc, sol)

        if 0 < i < len(sol.routes[self.route]):
            before = sol.routes[self.route][i-1] + sol.I
            after = sol.routes[self.route][i] + sol.I
        
        elif i == len(sol.routes[self.route]):
            before = sol.routes[self.route][i-1] + sol.I
            after = sol.VC
        
        else:
            before = sol.VC
            after = sol.routes[self.route][i] + sol.I
        d = self.initial_solution.instance.d
        diff = d[before][loc] + d[loc][after]

        # Capacity of the MMT constraint (and the VC)
        capacity = []
        for i in range(len(sol.routes)):
            capacity.append(sum(sol.q[j] for j in sol.routes[i] if j != loc))
        capacity.append(sol.q[loc])
        sum_cap = 0
        for cap in capacity:
            sum_cap += cap
            if cap > sol.Q:
                return False

        return diff <= sol.remaining_budget and sum_cap <= sol.C[sol.VC]




class Two_opt_Movement:

    # route : integer < len(initial solution.routes)
    def __init__(self, initial_solution, route):
        
        self.initial_solution = initial_solution
        self.route = route

        self.best_route = self.two_opt()

        temp = self.giveNewSolution()

        # new_solution = initial_solution if new_solution not feasible
        if temp.IsFeasible():
            self.new_solution = temp
        else:
            self.new_solution = initial_solution


    def __str__(self):
        return "Two_opt on the route " + str(self.route)


    # Return a new route that is optimize by permuting 2 locality (and only 2)
    def two_opt(self):
        route = self.initial_solution.routes[self.route]
        best_route = route
        best_distance = ga.calculate_total_distance(route, self.initial_solution.VC, self.initial_solution.instance)

        for i in range(0, len(route) - 1):
            for j in range(i + 1, len(route)):

                new_route = route[:i] + list(reversed(route[i:j+1])) + route[j+1:]
                new_distance = ga.calculate_total_distance(new_route, self.initial_solution.VC, self.initial_solution.instance)

                if new_distance < best_distance:
                    best_route = new_route
                    best_distance = new_distance

        return best_route


    # Return the solution : initial solution + two_opt movement
    def giveNewSolution(self):

        sol = self.initial_solution.copy()

        sol.routes[self.route] = self.best_route

        sol.remaining_budget = sol.B - sol.count_budget()

        if sol.IsFeasible():
            return sol
        
        return self.initial_solution


    # Evaluate the new solution
    def evaluate(self):

        sol = self.initial_solution

        return sol.evaluate()



'''-------------------------------
    TABU SEARCH IMPLEMENTATION
--------------------------------'''



# Return a list of all possible movements (different to feasible !! If it is not, the movement does anything)
def possible_movements(solution):

    movements = []

    # add Merge_Movement
    for two_routes in [(k, l) for k in range(len(solution.routes)) for l in range(len(solution.routes)) if k != l]:
        for two_locs in [(i, j) for i in range(1, len(solution.routes[two_routes[0]])) for j in range(1, len(solution.routes[two_routes[1]]))]:
            movements.append(Merge_Movement(solution, two_routes, two_locs))
    
    # add Replace_Movement
    for route in range(len(solution.routes)):
        for loc in solution.routes[route]:
            if loc not in solution.localities_with_high_priorities:
                movements.append(Replace_Movement(solution, route, loc))

    # add Add_Movement
    for route in range(len(solution.routes)):
        movements.append(Add_Movement(solution, route))

    # add Two_opt_Movement
    for route in range(len(solution.routes)):
        movements.append(Two_opt_Movement(solution, route))

    return movements



# return a set that store all feasible solutions given by the movements (it includes the movement null)
def Neighborhood(solution):

    neighbourhood = set()

    for movement in possible_movements(solution):
        neighbourhood.add(movement.new_solution)

    return neighbourhood



# Implement the tabu search heuristic for the movements define later
def tabu_search(solution, max_iterations=10, tabu_size=10):
    
    # initialisation of variables
    best_solution = solution.copy()
    tabu_list = set()

    # Doing max_iterations iterations
    for _ in range(max_iterations):
        
        # computing the neighborhood of the current solution
        neighborhood = Neighborhood(best_solution)

        # Find the best neighbor
        best_neighbor_vaccination = None
        best_neighbor_budget = None
        max_people_vaccinated = float('-inf')
        max_remaining_budget = float('-inf')

        for neighbor in neighborhood - tabu_list:
            
            remaining_budget = neighbor.remaining_budget
            people_vaccinated = neighbor.evaluate()

            # if the neighbor improve the budget we update our temporary variables
            if (neighbor not in tabu_list) and (remaining_budget > max_remaining_budget):
                    best_neighbor_budget = neighbor
                    max_remaining_budget = remaining_budget

            # if the neighbor improve the objective we update our temporary variables
            if (neighbor not in tabu_list) and (people_vaccinated > max_people_vaccinated):
                    best_neighbor_vaccination = neighbor
                    max_people_vaccinated = people_vaccinated
            
            # if the neighbor decrease the objective function, we add it in the tabu list
            elif (neighbor not in tabu_list) and (people_vaccinated < max_people_vaccinated):
                    tabu_list.add(neighbor)
                    if len(tabu_list) > tabu_size:
                        tabu_list.pop()

        # Update of the current solution by the best neighbor found 
        # (we privilege those which improved the objective and then those which maximize the remaining budget)
        if max_people_vaccinated > best_solution.evaluate():
            best_solution = best_neighbor_vaccination

        elif max_remaining_budget > best_solution.remaining_budget:
            best_solution = best_neighbor_budget
        
        # if no improvement, we break
        else:
            break
        
        # We add the current solution to the tabu list so as to not come back on it later
        tabu_list.add(best_solution)
        if len(tabu_list) > tabu_size:
            tabu_list.pop()

    return best_solution


# Return the best solution for each VC : best_VC, best_routes, people_vaccinated
def best_VC_tabu_search(instance, nbr_iterations = 50, size_tabu_list = 100):
        
        max_vaccinated_quantity = 0
        best_VC = None
        max_remaining_budget = 0
        best_VC_routes = []

        # For each VC
        for i in range(instance.I):

            routes, remaining_budget, vaccinated_quantity = ga.greedy_algorithm(i, [], instance)

            # remove the empty lists of routes
            for j in range(len(routes)) : 
                if routes[j] == [] :
                    ind = j
                    routes = routes[:ind]
                    break
            
            # we apply the tabu search
            solution = Solution(i, routes, instance)
            new_solution = tabu_search(solution, nbr_iterations, size_tabu_list)

            new_routes = new_solution.routes
            remaining_budget = new_solution.remaining_budget
            vaccinated_quantity = new_solution.evaluate()
            
            # update our solution if we find better then the current solution
            if vaccinated_quantity > max_vaccinated_quantity or (vaccinated_quantity == max_vaccinated_quantity and remaining_budget > max_remaining_budget):
                    max_vaccinated_quantity = vaccinated_quantity
                    max_remaining_budget = remaining_budget
                    best_VC = i
                    best_VC_routes = new_routes

        return best_VC, best_VC_routes, max_vaccinated_quantity


'''-------------------------------
        TABU SEARCH USE
--------------------------------'''


# Choose your instance
file_name = "coord100-5-1_processed.dat"
instance = data.InstanceData(file_name)
J, I, x_VC, y_VC, x_loc, y_loc, Q, C, q, f, p, tc, B, R, localities_with_high_priorities, M, C_km, d = instance.as_tuple()

# Solution given by the GreedyAlgorithm
VC_chosen, routes, vaccinated_quantity = ga.best_VC_greedy_algorithm(instance)

visualize_greedy_solution(I, J, VC_chosen, R,  x_VC, y_VC, x_loc, y_loc, routes, localities_with_high_priorities)
print("Chosen center:", VC_chosen, "| Vaccinated quantity after running the greedy algorithm:", vaccinated_quantity)


# Solution given after the improvement by the tabu search
VC_chosen, routes, vaccinated_quantity_local_search = best_VC_tabu_search(instance)

visualize_greedy_solution(I, J, VC_chosen, R,  x_VC, y_VC, x_loc, y_loc, routes, localities_with_high_priorities)
print("Chosen center:", VC_chosen, "| Vaccinated quantity after running the tabu search algorithm:", vaccinated_quantity_local_search)

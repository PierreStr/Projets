import matplotlib.pyplot as plt
import numpy as np
import matplotlib.cm as cm


# Store the interesting variables for the visualizer
def stock_info(file_name):

    with open("Instances/" + file_name, "r") as fichier:
        variable_actuelle = []

        counter = 1
        for ligne in fichier:
            ligne = ligne.strip()

            if ligne:
                valeurs = ligne.split()
                if len(valeurs) == 1:
                    variable_actuelle.append(int(valeurs[0]) if valeurs[0].isdigit() else float(valeurs[0]))
                else:
                    variable_actuelle.append(tuple(int(v) for v in valeurs))

            else:
                if counter == 2:
                    coord_VC = variable_actuelle
                if counter == 3:
                    coord_locality = variable_actuelle
                if counter == 12:
                    radius_VC = variable_actuelle
                if counter == 13:
                    priority_locality = variable_actuelle

                variable_actuelle = []
                counter += 1

    return [coord_VC, radius_VC, coord_locality, priority_locality]


# Return the list of coordinate of one MMT's route
def construct_route(coord_VC, coord_locality, w_m):

    route = []

    coord = coord_VC + coord_locality

    for k in range(len(w_m)):
        for l in range(len(w_m[k])):
            if w_m[k][l] > 0:
                route.append([(coord[k][0], coord[l][0]), (coord[k][1], coord[l][1])])

    return route


# Visualizer
def visualization(coord_VC, radius_VC, VC_choosen, coord_locality, priority_locality, w):

    for i in range(len(priority_locality)):
        priority_locality[i] -= 1

    fig, ax = plt.subplots()

    # Having the same proportion for both axes
    ax.set_aspect('equal', 'box')

    # Definition of the coordinates' list of the localities, remembering the priority localities
    coord_x_locality_not_prior = []
    coord_y_locality_not_prior = []
    coord_x_locality_prior = []
    coord_y_locality_prior = []
    for i in range(len(coord_locality)):
        if i in priority_locality:
            coord_x_locality_prior.append(coord_locality[i][0])
            coord_y_locality_prior.append(coord_locality[i][1])
        else:
            coord_x_locality_not_prior.append(coord_locality[i][0])
            coord_y_locality_not_prior.append(coord_locality[i][1])

    # Definition of the coordinates' list of the VCs, remembering the choosen VC
    coord_x_VC = []
    coord_y_VC = []
    color_VC = []
    for i in range(len(coord_VC)):
        coord_x_VC.append(coord_VC[i][0])
        coord_y_VC.append(coord_VC[i][1])
        if i == VC_choosen:               # For the chosen VC, we color it in red and we define its action radius
            color_VC.append("red")
            disc = plt.Circle((coord_VC[i][0], coord_VC[i][1]), radius_VC[i], color="lightblue", alpha=0.5, zorder=1)
        else:
            color_VC.append("blue")

    # Plot routes of the MMTs
    for m in range(len(w)):
        route = construct_route(coord_VC, coord_locality, w[m])
        colors = cm.get_cmap('tab10', len(w))  # define different colors
        color = colors(m) 
        for travel in route:
            ax.plot(travel[0], travel[1], color=color, linewidth=1, zorder=2)

    # Plot localities (in orange for the priority localities)
    ax.plot(coord_x_locality_not_prior, coord_y_locality_not_prior, "o", markersize=3, zorder=3, color = "darkslategrey")
    ax.plot(coord_x_locality_prior, coord_y_locality_prior, "o", markersize=3, zorder=3, color="orange")

    # Plot VCs
    ax.scatter(coord_x_VC, coord_y_VC, color=color_VC, marker="s", zorder=4)

    # Plot the disc
    ax.add_patch(disc)

    plt.show()


# Collect the data and the solution for using visualization
def visualize_solution(w, I, J, x, M, filename):

    # Data given by the file
    infos = stock_info(filename)

    coord_VC = infos[0]
    radius_VC = infos[1]
    coord_locality = infos[2]
    priority_locality = infos[3]

    # Store the chosen VC
    for i in range(I):
        if x[i].x > 0:  
            VC_chosen=i

    # Transformation of the matrix w into a numpy one
    w_matrix = np.zeros((M, I+J, I+J))

    for m in range(M):
        for i in range(I+J):
            for j in range(I+J):
                w_matrix[m, i, j] = w[m, i, j].X

    # Visualisze all
    visualization(coord_VC, radius_VC, VC_chosen, coord_locality, priority_locality, w_matrix)


# Visualizer for the solution given by the greedy algorithm
def visualize_greedy_solution(I, J, VC_chosen, R, x_VC, y_VC, x_loc, y_loc, routes, localities_with_high_priorities):

    fig, ax = plt.subplots()

    # Having the same proportion for both axes
    ax.set_aspect('equal', 'box')

    # Plot VCs
    for i in range(I):
        if i == VC_chosen:
            color = 'red'
            disc = plt.Circle((x_VC[VC_chosen], y_VC[VC_chosen]), R[VC_chosen], color="lightblue", alpha=0.5, zorder=1)
        else :
            color = 'blue'
        ax.scatter(x_VC[i], y_VC[i], color=color, marker="s", zorder=4)

    # Plot localities (in orange for prioritary localities)
    for j in range(J):
        color = 'orange' if j in localities_with_high_priorities else 'darkslategrey' 
        ax.plot(x_loc[j], y_loc[j], "o", markersize=3, zorder=3,c=color)

    # Plot routes of the MMTs
    colors = cm.get_cmap('tab10', len(routes))  # define different colors

    for idx, route in enumerate(routes):
        if not route:
            continue 
        route_coords = [(x_VC[VC_chosen], y_VC[VC_chosen])]  # Beginning at the chosen VC
        for loc in route:
            route_coords.append((x_loc[loc], y_loc[loc]))  # Add each next locality
        route_coords.append((x_VC[VC_chosen], y_VC[VC_chosen]))  # Return to the VC

        # Extract the coordinates and plot the routes
        x_coords, y_coords = zip(*route_coords)
        ax.plot(x_coords, y_coords, color=colors(idx), linewidth=1, zorder=2) 

    # Plot the disc
    ax.add_patch(disc)

    plt.show()
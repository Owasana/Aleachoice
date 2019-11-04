package model;

import java.util.ArrayList;

public class Solver {
    private Exit exit;
    private Pion pion;
    private Maze maze;

    public Solver (Exit exit, Pion pion, Maze maze){
        this.exit = exit;
        this.pion = pion;
        this.maze = maze;
    }

    public class Path extends ArrayList<Maze.CaseIndex>{
    }

    public boolean solve(Maze.CaseIndex prec, Maze.CaseIndex curr, Path path)
    {
        // SI on à atteind la sortie.
        if (curr.equals(exit)) {
            // Ajout de la sortie dans le chemin
            path.add(0, curr);
            return true;
        }

        ArrayList<Maze.CaseIndex> neighbours = maze.neighbours(curr);
        for (Maze.CaseIndex neighbour : neighbours) {
            // On ignore la case précedente 
            if (!neighbour.equals(prec)) {
                // On continue sur un voisin
                if (solve(curr, neighbour, path)) {
                    // Ajout de la case dans le chemin
                    path.add(0, curr);
                    return true;
                }
            }
        }

        return false;
    }

    public Path solve()
    {
        Path path = new Path();
        Maze.CaseIndex debut = maze.new CaseIndex(pion.getY(), pion.getX());

        solve(debut, debut, path);

        return path;
    }
}

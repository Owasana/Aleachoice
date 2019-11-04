package model;

import java.util.ArrayList;
import java.util.Random;

public class Maze {
    static enum Typewall {
        VERTICAL,
        HORIZONTAL
    };

    private int line;
    private int column;
    private int cases[][];
    private boolean wallsV[][];
    private boolean wallsH[][];

    public Maze(int line, int column) {
        this.line = line;
        this.column = column;

        cases = new int[line][column];
        wallsV = new boolean[line][column - 1];
        wallsH = new boolean[line - 1][column];

        // Initialisation des chemin unique des cases
        for(int l = 0; l < line; l++){
            for(int c = 0; c < column; c++){
                cases[l][c] = c + (l * column);
            }
        }

        // Positionnement de tous les walls verticaux
        for(int l = 0; l < line; l++){
            for(int c = 0; c < (column-1); c++){
                wallsV[l][c] = true;
            }
        }

        // Positionnement de tous les walls horizontaux
        for(int l = 0; l < (line-1); l++){
            for(int c = 0; c < column; c++){
                wallsH[l][c] = true;
            }
        }
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public boolean getWallV(int l, int c)
    {
        return wallsV[l][c];
    }

    public boolean getWallH(int l, int c)
    {
        return wallsH[l][c];
    }

    /*private final static String wallH = "___";
    private final static String wallV = "|";

    private void afficherwallsH() {
        for (int c = 0; c < column; ++c) {
            System.out.print(wallH);
        }
        System.out.println();
    }

    private void afficherline(int l) {
        System.out.print(wallV);
        for (int c = 0; c < column - 1; ++c) {
            //System.out.printf("%02d", cases[l][c]);
            System.out.printf("%02d", l * column + c);
            if (wallsV[l][c]) {
                System.out.print(wallV);
            }
            else {
                System.out.print(" ");
            }
        }
        // Dernière case
        //System.out.printf("%02d", cases[l][column - 1]);
        System.out.printf("%02d", l * column + column - 1);
        System.out.println(wallV);
    }

    public void afficher() {
        // Plafond
        afficherwallsH();

        // Étages n - 1
        for(int l = 0; l < line - 1; l++){
            afficherline(l);
            for (int c2 = 0; c2 < column; ++c2) {
                if (wallsH[l][c2]) {
                    System.out.print(wallH);
                }
                else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }
        // Dernier étage, rez de chaussez
        afficherline(line - 1);

        // Sol
        afficherwallsH();
    }*/

    private void fusion(int c1, int c2) {
        // Effacement des cases de valeur c2 par la valeur c1
        for (int l = 0; l < line; ++l) {
            for (int c = 0; c < column; ++c) {
                if (cases[l][c] == c2) {
                    cases[l][c] = c1;
                }
            }
        }
    }

    public void generateFusion() {

        //Numéro d'un wall.
        class wallIndex{
            public int l;
            public int c;
            public Typewall type;

            public wallIndex(int l, int c, Typewall type){
                this.l = l;
                this.c = c;
                this.type = type;
            }
        }


        //Initialisation des listes pour les walls non déjà utilisés.
        ArrayList<wallIndex> list = new ArrayList<wallIndex>();

        for (int l = 0; l < line; ++l){
            for (int c = 0; c < (column-1); ++c){
                list.add(new wallIndex(l,c, Typewall.VERTICAL));
            }
        }

        for (int l = 0; l < (line-1); ++l){
            for (int c = 0; c < column; ++c){
                list.add(new wallIndex(l,c, Typewall.HORIZONTAL));
            }
        }

        // Nombre de chemins actuel (autant que de cases)
        int n = line * column;

        Random random = new Random();

        // Supprimer des walls jusqu'a obtenir un seul chemin
        while (n > 1) {
            int i = random.nextInt(list.size());
            wallIndex indwall = list.remove(i);

            if (indwall.type == Typewall.VERTICAL) {
                // Case à gauche du wall
                int cg = cases[indwall.l][indwall.c];
                // Case à droite du wall
                int cd = cases[indwall.l][indwall.c + 1];

                // Si les chemins de part et d'autres sont différents on les fusionnes.
                if (cg != cd) {
                    // Le wall est enlevé
                    wallsV[indwall.l][indwall.c] = false;
                    // Un chemin fusionné
                    --n;

                    fusion(cg, cd);
                }
            }
            else {
                // Case en haut du wall
                int ch = cases[indwall.l][indwall.c];
                // Case en bas du wall
                int cb = cases[indwall.l + 1][indwall.c];

                // Si les chemins de part et d'autres sont différents on les fusionnes.
                if (ch != cb) {
                    // Le wall est enlevé
                    wallsH[indwall.l][indwall.c] = false;
                    // Un chemin fusionné
                    --n;

                    fusion(ch, cb);
                }
            }
        }

    }

    public class CaseIndex extends Position {
        public CaseIndex(int l, int c) {
            super(c, l);
        }
    }

    // Renvoi a position (index) des case voisines accessibles.
    public ArrayList<CaseIndex> neighbours(CaseIndex index)
    {
        ArrayList<CaseIndex> res = new ArrayList<CaseIndex>();

        // Horizontal gauche
        // Si il existe une case à gauche et qu'il n'y a pas de wall
        if (index.getX() > 0 && !wallsV[index.getY()][index.getX() - 1]) {
            res.add(new CaseIndex(index.getY(), index.getX() - 1));
        }
        // Si il existe une case à droite et qu'il n'y a pas de wall
        if (index.getX() < (column - 1) && !wallsV[index.getY()][index.getX()]) {
            res.add(new CaseIndex(index.getY(), index.getX() + 1));
        }
        // Si il existe une case en haut et qu'il n'y a pas de wall
        if (index.getY() > 0 && !wallsH[index.getY() - 1][index.getX()]) {
            res.add(new CaseIndex(index.getY() - 1, index.getX()));
        }
        // Si il existe une case en bas et qu'il n'y a pas de wall
        if (index.getY() < (line - 1) && !wallsH[index.getY()][index.getX()]) {
            res.add(new CaseIndex(index.getY() + 1, index.getX()));
        }

        return res;
    }
}



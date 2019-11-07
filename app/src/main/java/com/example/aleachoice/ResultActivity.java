package com.example.aleachoice;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;

import model.Collection;
import model.Exit;
import model.Item;
import model.Maze;
import model.Pion;
import model.Solver;
import view.MazeView;

public class ResultActivity extends AppCompatActivity {
    private Collection collection;

    private TextView result;
    private MazeView canvas;
    private Button restartButton;

    // Animation d'affichage des chemins.
    ValueAnimator animator;

    // Temps en ms entre deux cases affichés.
    final static int ANIMATION_PERIOD = 200;
    // 10 case de labyrynthe pour un item.
    final static int ITEM_SURFACE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        result = findViewById(R.id.result);
        canvas = findViewById(R.id.canvas);
        restartButton = findViewById(R.id.restart_button);

        Intent intent = getIntent();
        collection = (Collection) intent.getSerializableExtra("collection");

        // Augmentation de la surface proportionellement au nb d'élement
        final int mazeSize = (int)(Math.sqrt((double)collection.size() * ITEM_SURFACE));

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateAndSolve(mazeSize);
            }
        });

        generateAndSolve(mazeSize);
    }

    private void generateAndSolve(int mazeSize) {
        // Reinitialisation du résultat
        result.setText("");
        // Si il existe une animation, l'arrêter
        if (animator != null) {
            animator.cancel();
        }

        // Création du labyrinthe et génération
        Maze maze = new Maze(mazeSize, mazeSize);
        maze.generateFusion();

        // Element par solveur, soit un chemin associé à un item.
        class ItemSolve {
            public Solver.Path path;
            public Item item;
        }
        ArrayList<ItemSolve> results = new ArrayList<>();

        // Placement de la sortie au centre
        Exit exit = new Exit(mazeSize / 2, mazeSize / 2);

        final float radius = (float)(maze.getColumn() - 1) / 2.0f;

        ArrayList<Item> shuffledItems = collection.shuffled();

        for (int i = 0; i < shuffledItems.size(); ++i) {
            // Coordonnée polaire
            final double angle = i * Math.PI * 2 / shuffledItems.size();
            final int x = (int)(Math.cos(angle) * radius + radius);
            final int y = (int)(Math.sin(angle) * radius + radius);
            Pion pion = new Pion(x, y);

            Solver solver = new Solver(exit, pion, maze);

            // Resolution et sauvegarde du resultat associé à un item
            ItemSolve result = new ItemSolve();
            result.item = shuffledItems.get(i);
            result.path = solver.solve();
            results.add(result);
        }

        // Tri des resultats par longueur de chemin.
        results.sort(new Comparator<ItemSolve>() {
            @Override
            public int compare(ItemSolve o1, ItemSolve o2) {
                return Integer.compare(o1.path.size(), o2.path.size());
            }
        });

        // Création de chemin coloré par la couleur de l'item
        ArrayList<MazeView.ColoredPath> coloredPaths = new ArrayList<>();
        for (ItemSolve result : results) {
            coloredPaths.add(new MazeView.ColoredPath(result.path, result.item.color()));
        }

        canvas.setMaze(maze);
        canvas.setPaths(coloredPaths);
        canvas.setExit(exit);

        final ItemSolve winner = results.get(0);
        final int nbCases = winner.path.size();
        // Définition d'une animation pour chaque case du chemin gagnant.
        animator = ValueAnimator.ofInt(0, nbCases);
        // Affichage toutes les 1 secondes
        animator.setDuration(ANIMATION_PERIOD * winner.path.size());
        // Valeur linéaire par rapport au temps
        animator.setInterpolator(new LinearInterpolator());

        // Invalidation du canvas et ajout d'un case à dessiner.
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final int value = (int)animation.getAnimatedValue();
                canvas.showCases(value);

                if (value == nbCases) {
                    result.setText(winner.item.name());
                    result.setTextColor(winner.item.color());
                }
            }

        });

        animator.start();
    }
}

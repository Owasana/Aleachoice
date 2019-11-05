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

    final static int MAZE_WIDTH = 15; // TODO par rapport au nb item
    final static int MAZE_HEIGHT = 15;
    // Temps en ms entre deux cases affichés.
    final static int ANIMATION_PERIOD = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        result = findViewById(R.id.result);
        canvas = findViewById(R.id.canvas);
        restartButton = findViewById(R.id.restart_button);

        Intent intent = getIntent();
        collection = (Collection) intent.getSerializableExtra("collection");

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateAndSolve();
            }
        });

        generateAndSolve();
    }

    private void generateAndSolve() {
        Maze maze = new Maze(MAZE_HEIGHT, MAZE_WIDTH);
        maze.generateFusion();

        class ItemSolve {
            public Solver.Path path;
            public Item item;
        }
        ArrayList<ItemSolve> results = new ArrayList<>();

        Exit exit = new Exit(MAZE_WIDTH / 2, MAZE_HEIGHT / 2);

        final float radius = (float)(maze.getColumn() - 1) / 2.0f;

        for (int i = 0; i < collection.size(); ++i) {
            // Coordonnée polaire
            final double angle = i * Math.PI * 2 / collection.size();
            final int x = (int)(Math.cos(angle) * radius + radius);
            final int y = (int)(Math.sin(angle) * radius + radius);
            Pion pion = new Pion(x, y);

            Solver solver = new Solver(exit, pion, maze);

            // Resolution et sauvegarde du resultat associé à un item
            ItemSolve result = new ItemSolve();
            result.item = collection.item(i);
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

        final ItemSolve winner = results.get(0);
        final int nbCases = winner.path.size();
        // Définition d'une animation pour chaque case du chemin gagnant.
        ValueAnimator animator = ValueAnimator.ofInt(0, nbCases);
        // Affichage toutes les 1 secondes
        animator.setDuration(ANIMATION_PERIOD * winner.path.size());
        // Valeur linéaire par rapport au temps
        animator.setInterpolator(new LinearInterpolator());

        // Invalidation du canvas et ajout d'un case à dessiner.
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int)animation.getAnimatedValue();
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

package com.example.aleachoice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

import model.Collection;
import model.Exit;
import model.Item;
import model.Maze;
import model.Pion;
import model.Solver;

public class ResultActivity extends AppCompatActivity {
    private Collection collection;

    private TextView result;
    private MazeView canvas;
    private Button restartButton;

    final static int MAZE_WIDTH = 15; // TODO par rapport au nb item
    final static int MAZE_HEIGHT = 15;

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
        };
        ArrayList<ItemSolve> results = new ArrayList<>();

        Exit exit = new Exit(MAZE_WIDTH / 2, MAZE_HEIGHT / 2);

        for (int i = 0; i < collection.size(); ++i) {
            Pion pion = new Pion(0, 0);
            Solver solver = new Solver(exit, pion, maze);

            ItemSolve result = new ItemSolve();
            result.item = collection.item(i);
            result.path = solver.solve();
            System.out.println(result.path.size() + result.item.name()); // TODO DEBUG
            results.add(result);
        }

        canvas.setMaze(maze);

        results.sort(new Comparator<ItemSolve>() {
            @Override
            public int compare(ItemSolve o1, ItemSolve o2) {
                return Integer.compare(o1.path.size(), o2.path.size());
            }
        });

        ItemSolve winner = results.get(0);

        result.setText(winner.item.name());
        result.setTextColor(winner.item.color());
    }
}

package com.example.aleachoice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import model.Collection;
import model.Exit;
import model.Item;
import model.Maze;
import model.Pion;
import model.Solver;

public class ResultActivity extends AppCompatActivity {

    private TextView result;

    final static int MAZE_WIDTH = 15;
    final static int MAZE_HEIGHT = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        result = findViewById(R.id.result);

        Intent intent = getIntent();
        Collection collection = (Collection)intent.getSerializableExtra("collection");

        Maze maze = new Maze(MAZE_HEIGHT, MAZE_WIDTH);
        maze.generateFusion();

        ArrayList<Solver.Path> paths = new ArrayList<>();

        Exit exit = new Exit(MAZE_WIDTH / 2, MAZE_HEIGHT / 2);

        for (int i = 0; i < collection.size(); ++i) {
            Pion pion = new Pion(0, 0, collection.item(i));
            Solver solver = new Solver(exit, pion, maze);
            paths.add(solver.solve());
        }

        MazeView canvas = findViewById(R.id.canvas);
        canvas.setMaze(maze);

        //Item result_item = pickResult.item;
        result.setText("done");
        //result.setTextColor(result_item.color());*/

    }
}

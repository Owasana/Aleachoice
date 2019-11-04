package com.example.aleachoice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import model.Maze;

public class MazeView extends View {
    private Paint background;
    private Paint border;
    private Maze maze;
    final static int BORDER_WIDTH = 20; // TODO par rapport à la taille du laby
    public MazeView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        background = new Paint();
        background.setARGB(255, 255, 255, 200);

        border = new Paint();
        border.setARGB(255, 0, 0, 0);
        border.setStrokeWidth(BORDER_WIDTH);
        border.setStyle(Paint.Style.STROKE);
        border.setStrokeCap(Paint.Cap.SQUARE);
        maze = null;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    @Override
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawCircle(50, 50, 50, paint);
        drawMaze(canvas);
    }

    public void drawMaze(final Canvas canvas) {
        // Effacement du canvas



        final int mazeSize = Math.min(getWidth(), getHeight());
        final int marginHeight = (getHeight() - mazeSize) / 2;
        final int marginWidth = (getWidth() - mazeSize) / 2;

        canvas.drawRect(marginWidth, marginHeight,
                getWidth() - marginWidth, getHeight() - marginHeight, background);

        // Dessin de l'enceinte (pas pour la musique ni les mollard) TODO
        final int halfBorder = BORDER_WIDTH / 2;
        canvas.drawRect(marginWidth + halfBorder, marginHeight + halfBorder,
                getWidth() - marginWidth - halfBorder,
                getHeight() - marginHeight - halfBorder, border);

        final int innerLeft = marginWidth + halfBorder;
        final int innerTop = marginHeight + halfBorder;
        final int innerSize = mazeSize - (BORDER_WIDTH * 2);
        final int caseSize = (innerSize - (BORDER_WIDTH * (maze.getColumn() - 1))) / maze.getColumn();

        // Dessin murs verticaux
        for (int l = 0; l < maze.getLine(); ++l) {
            for (int c = 0; c < maze.getColumn() - 1; ++c) {
                // Dessin du mur droit des cases
                if (maze.getWallV(l, c)) {
                    final int x = innerLeft + (caseSize + BORDER_WIDTH) * (c + 1);
                    final int y = innerTop + (caseSize + BORDER_WIDTH) * l;
                    System.out.println("murV" + x + ", " + y + ", " + caseSize + ", " + innerSize);
                    canvas.drawLine(x, y, x, y + caseSize + BORDER_WIDTH, border);
                }
            }
        }

        // Dessin murs horizontaux
        for (int l = 0; l < maze.getLine() - 1; ++l) {
            for (int c = 0; c < maze.getColumn(); ++c) {
                // Dessin du mur droit des cases
                if (maze.getWallH(l, c)) {
                    final int x = innerLeft + (caseSize + BORDER_WIDTH) * c;
                    final int y = innerTop + (caseSize + BORDER_WIDTH) * (l + 1);
                    System.out.println("murH" + x + ", " + y + ", " + caseSize + ", " + innerSize);
                    canvas.drawLine(x, y, x + caseSize + BORDER_WIDTH, y, border);
                }
            }
        }

        if (maze == null) {
            return; // pas de dessin
        }
    }
}

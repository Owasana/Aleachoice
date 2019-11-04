package com.example.aleachoice;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import model.Maze;
import model.Solver;

//com.example.aleachoice.MazeView
public class MazeView extends View {
    final static int BORDER_WIDTH = 20; // TODO par rapport à la taille du laby

    static public class ColoredPath {
        public Solver.Path path;
        public int color;

        public ColoredPath(Solver.Path path, int color) {
            this.path = path;
            this.color = color;
        }
    };

    private Paint background;
    private Paint border;

    private Maze maze;
    private ArrayList<ColoredPath> paths;
    int currIndex;


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
        paths = null;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
        invalidate();
    }

    public void setPaths(ArrayList<ColoredPath> paths) {
        this.paths = paths;
        currIndex = 0;
        ColoredPath winnerPath = paths.get(0);

        // Définition d'une animation pour chaque case du chemin gagnant.
        ValueAnimator animator = ValueAnimator.ofInt(0, winnerPath.path.size());
        // Affichage toutes les 1 secondes
        animator.setDuration(1000);

        // Invalidation du canvas et ajout d'un case à dessiner.
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int)animation.getAnimatedValue();
                invalidate();
                currIndex = value;
            }
        });

        animator.start();
    }

    @Override
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        // Dimensions utiles pour le dessin
        final int mazeSize = Math.min(getWidth(), getHeight());
        final int marginHeight = (getHeight() - mazeSize) / 2;
        final int marginWidth = (getWidth() - mazeSize) / 2;
        final int halfBorder = BORDER_WIDTH / 2;
        final int innerLeft = marginWidth + halfBorder;
        final int innerTop = marginHeight + halfBorder;
        final int innerSize = mazeSize - (BORDER_WIDTH * 2);
        final int caseSize = (innerSize - (BORDER_WIDTH * (maze.getColumn() - 1))) / maze.getColumn();

        drawMaze(canvas, mazeSize, marginHeight, marginWidth, halfBorder, innerLeft, innerTop, innerSize, caseSize);
        drawPaths(canvas, mazeSize, marginHeight, marginWidth, halfBorder, innerLeft, innerTop, innerSize, caseSize, currIndex);
    }

    public void drawMaze(final Canvas canvas, int mazeSize, int marginHeight, int marginWidth,
                         int halfBorder, int innerLeft, int innerTop, int innerSize, int caseSize)
    {
        canvas.drawRect(marginWidth, marginHeight,
                getWidth() - marginWidth, getHeight() - marginHeight, background);

        // Dessin de l'enceinte (pas pour la musique ni les mollard) TODO
        canvas.drawRect(marginWidth + halfBorder, marginHeight + halfBorder,
                getWidth() - marginWidth - halfBorder,
                getHeight() - marginHeight - halfBorder, border);

        if (maze == null) {
            return; // pas de dessin des murs
        }

        // Dessin murs verticaux
        for (int l = 0; l < maze.getLine(); ++l) {
            for (int c = 0; c < maze.getColumn() - 1; ++c) {
                // Dessin du mur droit des cases
                if (maze.getWallV(l, c)) {
                    final int x = innerLeft + (caseSize + BORDER_WIDTH) * (c + 1);
                    final int y = innerTop + (caseSize + BORDER_WIDTH) * l;
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
                    canvas.drawLine(x, y, x + caseSize + BORDER_WIDTH, y, border);
                }
            }
        }
    }

    public void drawPaths(final Canvas canvas, int mazeSize, int marginHeight, int marginWidth,
                          int halfBorder, int innerLeft, int innerTop, int innerSize, int caseSize, int nbCase)
    {
        System.out.println("draw paths");
        if (paths == null) {
            return;
        }

        // Parcous inverse pour dessiner le gagnant en dernier
        for (int i = paths.size() - 1; i >= 0; --i) {
            ColoredPath cpath = paths.get(i);
            System.out.println(cpath.path.size());

            // Couleur de dessin
            Paint paint = new Paint();
            paint.setColor(cpath.color);

            for (int j = 0; j < nbCase; ++j) {
                Maze.CaseIndex cind = cpath.path.get(j);
                final int x = innerLeft + (caseSize + BORDER_WIDTH) * cind.getX() + halfBorder;
                final int y = innerTop + (caseSize + BORDER_WIDTH) * cind.getY() + halfBorder;
                canvas.drawRect(x, y, x + caseSize, y + caseSize, paint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // On s'assure que la zone est carré.
        if (widthMeasureSpec < heightMeasureSpec)
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        else
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }
}

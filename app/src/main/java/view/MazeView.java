package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import model.Exit;
import model.Maze;
import model.Solver;

public class MazeView extends View {
    static public class ColoredPath {
        public Solver.Path path;
        public int color;

        public ColoredPath(Solver.Path path, int color) {
            this.path = path;
            this.color = color;
        }
    }

    // La transparence des case sur une chemin
    static final int PATH_CASE_ALPHA = 150;
    // La taille d'une case de chemin par rapport à une case
    static final float PATH_CASE_RATIO = 0.5f;
    

    private Paint backgroundPaint;
    private Paint borderPaint;
    private Paint exitPaint;

    private Maze maze;
    private Exit exit;
    private ArrayList<ColoredPath> paths;
    // Nombre de case actuellement affichées.
    int showedCases;

    public MazeView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        backgroundPaint = new Paint();
        backgroundPaint.setARGB(255, 255, 255, 200);

        borderPaint = new Paint();
        borderPaint.setARGB(255, 0, 0, 0);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeCap(Paint.Cap.SQUARE);

        exitPaint = new Paint();
        exitPaint.setARGB(255, 255, 0, 0);

        maze = null;
        paths = null;
        showedCases = 0;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    public void setPaths(ArrayList<ColoredPath> paths) {
        this.paths = paths;
    }

    public void setExit(Exit exit) {
        this.exit = exit;
    }

    public void showCases(int nbCases) {
        showedCases = nbCases;
        invalidate();
    }

    @Override
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        // Dimensions utiles pour le dessin
        final int mazeSize = Math.min(getWidth(), getHeight());
        final int marginHeight = (getHeight() - mazeSize) / 2;
        final int marginWidth = (getWidth() - mazeSize) / 2;
        // La bordure represente 1/10 d'une case
        final int borderWidth = mazeSize / maze.getColumn() / 10;
        final int halfBorder = borderWidth / 2;
        final int innerLeft = marginWidth + halfBorder;
        final int innerTop = marginHeight + halfBorder;
        final int innerSize = mazeSize - (borderWidth * 2);
        final int caseSize = (innerSize - (borderWidth * (maze.getColumn() - 1))) / maze.getColumn();

        /// Dessin d'une ligne de la largeur de la bordure
        borderPaint.setStrokeWidth(borderWidth);

        drawMaze(canvas, mazeSize, marginHeight, marginWidth, borderWidth, halfBorder, innerLeft, innerTop, innerSize, caseSize);
        drawExit(canvas, mazeSize, marginHeight, marginWidth, borderWidth, halfBorder, innerLeft, innerTop, innerSize, caseSize);
        drawPaths(canvas, mazeSize, marginHeight, marginWidth, borderWidth, halfBorder, innerLeft, innerTop, innerSize, caseSize, showedCases);
    }

    public void drawMaze(final Canvas canvas, int mazeSize, int marginHeight, int marginWidth, int borderWidth,
                         int halfBorder, int innerLeft, int innerTop, int innerSize, int caseSize)
    {
        canvas.drawRect(marginWidth, marginHeight,
                getWidth() - marginWidth, getHeight() - marginHeight, backgroundPaint);

        // Dessin de l'enceinte (pas pour la musique ni les mollards)
        canvas.drawRect(marginWidth + halfBorder, marginHeight + halfBorder,
                getWidth() - marginWidth - halfBorder,
                getHeight() - marginHeight - halfBorder, borderPaint);

        if (maze == null) {
            return; // pas de dessin des murs
        }

        // Dessin murs verticaux
        for (int l = 0; l < maze.getLine(); ++l) {
            for (int c = 0; c < maze.getColumn() - 1; ++c) {
                // Dessin du mur droit des cases
                if (maze.getWallV(l, c)) {
                    final int x = innerLeft + (caseSize + borderWidth) * (c + 1);
                    final int y = innerTop + (caseSize + borderWidth) * l;
                    canvas.drawLine(x, y, x, y + caseSize + borderWidth, borderPaint);
                }
            }
        }

        // Dessin murs horizontaux
        for (int l = 0; l < maze.getLine() - 1; ++l) {
            for (int c = 0; c < maze.getColumn(); ++c) {
                // Dessin du mur droit des cases
                if (maze.getWallH(l, c)) {
                    final int x = innerLeft + (caseSize + borderWidth) * c;
                    final int y = innerTop + (caseSize + borderWidth) * (l + 1);
                    canvas.drawLine(x, y, x + caseSize + borderWidth, y, borderPaint);
                }
            }
        }
    }

    public void drawExit(final Canvas canvas, int mazeSize, int marginHeight, int marginWidth, int borderWidth,
                         int halfBorder, int innerLeft, int innerTop, int innerSize, int caseSize)
    {
        final int x = innerLeft + (caseSize + borderWidth) * (maze.getColumn() / 2) + halfBorder + caseSize / 2;
        final int y = innerTop + (caseSize + borderWidth) * (maze.getLine() / 2) + halfBorder + caseSize / 2;
        canvas.drawCircle(x, y, caseSize / 2, exitPaint);
    }

    public void drawPathCase(final Canvas canvas, Paint paint, Maze.CaseIndex cind, int borderWidth,
                             int halfBorder, int innerLeft, int innerTop, int caseSize, int margin)
    {
        final int x = innerLeft + (caseSize + borderWidth) * cind.getX() + halfBorder;
        final int y = innerTop + (caseSize + borderWidth) * cind.getY() + halfBorder;
        canvas.drawRect(x + margin, y + margin, x + caseSize - margin, y + caseSize - margin, paint);
    }

    public void drawPaths(final Canvas canvas, int mazeSize, int marginHeight, int marginWidth, int borderWidth,
                          int halfBorder, int innerLeft, int innerTop, int innerSize, int caseSize, int nbCase)
    {
        if (paths == null) {
            return;
        }


        // Parcous inverse pour dessiner le gagnant en dernier
        for (int i = paths.size() - 1; i >= 0; --i) {
            ColoredPath cpath = paths.get(i);

            // Couleur de dessin
            Paint paint = new Paint();
            paint.setColor(cpath.color);
            paint.setAlpha(PATH_CASE_ALPHA);

            // Dessin de n-1 cases
            for (int j = 0; j < nbCase - 1; ++j) {
                Maze.CaseIndex cind = cpath.path.get(j);
                // La marge minimal
                final int minMargin = (int)(((float)caseSize) * PATH_CASE_RATIO / 2.0f);
                // Marge graduelle en fonction de la distance avec la case avec la tête du chemin
                final int margin = minMargin + minMargin / 3 * (nbCase - 1 - j) / (nbCase - 1);
                drawPathCase(canvas, paint, cind, borderWidth, halfBorder, innerLeft, innerTop, caseSize, margin);
            }


            paint.setAlpha(255);

            // Dessin de la n case (dernière case).
            if (nbCase > 0) {
                Maze.CaseIndex cind = cpath.path.get(nbCase - 1);
                final int margin = (int)(((float)caseSize) * PATH_CASE_RATIO / 4.0f);
                drawPathCase(canvas, paint, cind, borderWidth, halfBorder, innerLeft, innerTop, caseSize, margin);
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

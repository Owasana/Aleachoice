package com.example.aleachoice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class LabyrintheView extends View {
    private Paint paint;
    public LabyrintheView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setARGB(255, 255, 0, 0);
    }

    @Override
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(50, 50, 50, paint);
    }
}

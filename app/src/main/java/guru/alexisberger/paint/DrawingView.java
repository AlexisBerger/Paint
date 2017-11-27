package guru.alexisberger.paint;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.Stack;

/**
 * Created by alexi on 25/11/2017.
 */

public class DrawingView extends View {

    public int width;
    public int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    Context context;
    Paint mPaint;
    private Paint circlePaint;
    private Path circlePath;
    private int background_a;
    private int background_r;
    private int background_g;
    private int background_b;
    private int color;
    private Stack<Bitmap> undo;
    private Stack<Bitmap> redo;

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;


    public DrawingView(Context c, Paint mPaint) {
        super(c);
        context = c;
        this.mPaint = mPaint;
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        circlePaint = new Paint();
        circlePath = new Path();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4f);

        undo = new Stack<>();
        redo = new Stack<>();

        mScaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                mScaleFactor *= detector.getScaleFactor();

                // Don't let the object get too small or too large.
                mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

                invalidate();
                return true;
            }
        });
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStrokeWidth(12 * mScaleFactor);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);
        canvas.drawPath(circlePath, circlePaint);
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        if (undo.size() >= 10)
            undo.removeElementAt(9);
        undo.push(Bitmap.createBitmap(mBitmap));
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;

            circlePath.reset();
            circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        circlePath.reset();
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);

        redo.removeAllElements();

        // kill this so we don't double draw
        mPath.reset();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        mScaleDetector.onTouchEvent(event);
        if (!mScaleDetector.isInProgress())
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
        return true;
    }

    public void undo() {
        if (undo.empty())
            return;
        Bitmap p = undo.pop();
        if (redo.size() >= 5)
            redo.removeElementAt(4);
        redo.push(Bitmap.createBitmap(mBitmap));
        mBitmap = p;
        mCanvas = new Canvas(mBitmap);
        invalidate();

    }

    public void redo() {
        if (redo.empty())
            return;
        Bitmap p = redo.pop();
        if (undo.size() >= 10)
            undo.removeElementAt(9);
        undo.push(Bitmap.createBitmap(mBitmap));
        mBitmap = p;
        mCanvas = new Canvas(mBitmap);
        invalidate();

    }
    public Bitmap getMBitmap(){
        return mBitmap;
    }

    public void clean(){
        //mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        mCanvas.drawARGB(background_a, background_r, background_g, background_b);
    }

    public void setBackground(int a, int r, int g, int b){
        background_a = a;
        background_r = r;
        background_g = g;
        background_b = b;
        mCanvas.drawARGB(a,r,g,b);
    }

}
package guru.alexisberger.paint;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.SeekBar;

/**
 * Created by alexi on 26/11/2017.
 */

public abstract class ColorPicker extends Dialog {
    private int green = 0;
    private int red = 0;
    private int bleu = 0;
    private int alpha = 0;


    public ColorPicker(@NonNull Context context) {
        super(context);
        setContentView(R.layout.color_picker);

        SeekBar redSb = findViewById(R.id.red);
        SeekBar blueSb = findViewById(R.id.blue);
        SeekBar greenSb = findViewById(R.id.green);
        SeekBar alphaSb = findViewById(R.id.alpha);
        redSb.setMax(255);
        greenSb.setMax(255);
        blueSb.setMax(255);
        alphaSb.setMax(255);

        final SurfaceView surface = (SurfaceView) findViewById(R.id.surfaceView);
        surface.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // Do some drawing when surface is ready
                Canvas canvas = holder.lockCanvas();
                canvas.drawColor(Color.argb(alpha, red, green, bleu));
                holder.unlockCanvasAndPost(canvas);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Canvas canvas = holder.lockCanvas();
                canvas.drawColor(Color.argb(alpha, red, green, bleu));
                holder.unlockCanvasAndPost(canvas);
            }
        });


        redSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                red = seekBar.getProgress();
                Canvas canvas = surface.getHolder().lockCanvas();
                canvas.drawColor(Color.argb(alpha, red, green, bleu));
                surface.getHolder().unlockCanvasAndPost(canvas);
                surface.invalidate();
                onChange(alpha,red,green,bleu);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        greenSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                green = seekBar.getProgress();
                Canvas canvas = surface.getHolder().lockCanvas();
                canvas.drawColor(Color.argb(alpha, red, green, bleu));
                surface.getHolder().unlockCanvasAndPost(canvas);
                onChange(alpha,red,green,bleu);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });blueSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bleu = seekBar.getProgress();
                Canvas canvas = surface.getHolder().lockCanvas();
                canvas.drawColor(Color.argb(alpha, red, green, bleu));
                surface.getHolder().unlockCanvasAndPost(canvas);
                onChange(alpha,red,green,bleu);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        alphaSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                alpha = seekBar.getProgress();
                Canvas canvas = surface.getHolder().lockCanvas();
                canvas.drawColor(Color.BLACK);
                canvas.drawColor(Color.argb(alpha, red, green, bleu));
                surface.getHolder().unlockCanvasAndPost(canvas);
                onChange(alpha,red,green,bleu);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    protected abstract void onChange(int a, int r, int g, int b);

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getBleu() {
        return bleu;
    }

    public void setBleu(int bleu) {
        this.bleu = bleu;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }
}

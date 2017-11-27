package guru.alexisberger.paint;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.net.URI;

public class SettingsActivity extends AppCompatActivity {

    private ColorPicker colorPicker;
    private int background_green = 0;
    private int background_red = 0;
    private int background_bleu = 0;
    private int background_alpha = 0;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent();
        setContentView(R.layout.activity_settings);
        final SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView3);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Canvas canvas = holder.lockCanvas();
                canvas.drawColor(Color.argb(background_alpha, background_red, background_green, background_bleu));
                holder.unlockCanvasAndPost(canvas);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        colorPicker = new ColorPicker(this){

            @Override
            protected void onChange(int a, int r, int g, int b) {
                background_alpha = a;
                background_bleu = b;
                background_green = g;
                background_red = r;
                Canvas canvas = surfaceView.getHolder().lockCanvas();
                canvas.drawColor(Color.argb(background_alpha, background_red, background_green, background_bleu));
                surfaceView.getHolder().unlockCanvasAndPost(canvas);
                surfaceView.invalidate();
                intent.putExtra("red", background_red);
                intent.putExtra("bleu", background_bleu);
                intent.putExtra("green", background_green);
                intent.putExtra("alpha", background_alpha);
                SettingsActivity.this.setResult(Activity.RESULT_OK, intent);

            }
        };
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPicker.show();

            }
        });
    }



}

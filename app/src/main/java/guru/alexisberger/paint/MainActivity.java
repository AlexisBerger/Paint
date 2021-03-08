package guru.alexisberger.paint;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private Paint mPaint;
    private DrawingView dv;
    private ColorPicker colorPicker;
    private static final int WRITE_REQUEST_CODE = 43;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewGroup content = findViewById(R.id.content_main);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
        dv = new DrawingView(this, mPaint);
        content.addView(dv);
        colorPicker = new ColorPicker(this) {

            @Override
            protected void onChange(int a, int r, int g, int b) {
                //Log.wtf("color", " "+a+" "+r+" "+g+" "+b);
                mPaint.setARGB(a, r, g, b);

            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_undo) {
            dv.undo();
            return true;
        } else if (id == R.id.action_redo) {
            dv.redo();
            return true;
        } else if (id == R.id.action_color) {

            colorPicker.show();

            return true;
        } else if (id == R.id.action_save) {
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

            // Filter to only show results that can be "opened", such as
            // a file (as opposed to a list of contacts or timezones).
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            // Create a file with the requested MIME type.
            intent.setType("image/png");
            intent.putExtra(Intent.EXTRA_TITLE, "draw.png");
            startActivityForResult(intent, WRITE_REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WRITE_REQUEST_CODE) {
            OutputStream outputStream = null;
            try {
                outputStream = MainActivity.this.getContentResolver().openOutputStream(data.getData());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {

                dv.getMBitmap().compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
                Toast.makeText(getApplicationContext(), "Image saved", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("Error", data.getData().getPath() + " not saved");
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

package com.example.catchtheball;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class Avatars extends AppCompatActivity {


    int high_score;
    ImageButton orangeAv, greenAv, moustacheAv, criminalAv, detectiveAv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatars);
        orangeAv = (ImageButton)findViewById(R.id.orang_box);
        greenAv = (ImageButton)findViewById(R.id.green_box);
        detectiveAv = (ImageButton)findViewById(R.id.detective_box);
        criminalAv = (ImageButton)findViewById(R.id.criminal_box);
        moustacheAv = (ImageButton)findViewById(R.id.moustache_box);

        Intent intent = getIntent();
        high_score = intent.getIntExtra("high_score", 0);

        if (high_score < 2000)
        {
            moustacheAv.getBackground().setAlpha(40);
            moustacheAv.setImageAlpha(40);
        }
        if (high_score < 1500)
        {
            criminalAv.getBackground().setAlpha(40);
            criminalAv.setImageAlpha(40);
        }
        if (high_score < 1000)
        {
            detectiveAv.getBackground().setAlpha(40);
            detectiveAv.setImageAlpha(40);
        }
        if (high_score < 700)
        {
            greenAv.getBackground().setAlpha(40);
            greenAv.setImageAlpha(40);
        }
        if (high_score < 300)
        {
            orangeAv.getBackground().setAlpha(40);
            orangeAv.setImageAlpha(40);
        }
    }

    public void Blue(View view)
    {
        Intent intent = new Intent();
        intent.putExtra("drawable_right", R.drawable.box_right);
        intent.putExtra("drawable_left", R.drawable.box_left);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    public void Orange(View view)
    {
        if (high_score <= 300)
        {
            Toast.makeText(this, "YOUR HIGH SCORE SHOULD BE 300 OR MORE TO UNLOCK THIS AVATAR!", Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent();
            intent.putExtra("drawable_right", R.drawable.box_orange_right);
            intent.putExtra("drawable_left", R.drawable.box_orange_left);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
    }

    public void Green(View view)
    {
        if (high_score <= 700)
        {
            Toast.makeText(this, "YOUR HIGH SCORE SHOULD BE 700 OR MORE TO UNLOCK THIS AVATAR!", Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent();
            intent.putExtra("drawable_right", R.drawable.box_green_right);
            intent.putExtra("drawable_left", R.drawable.box_green_left);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
    }

    public void Detective(View view)
    {
        if (high_score <= 1000)
        {
            Toast.makeText(this, "YOUR HIGH SCORE SHOULD BE 1000 OR MORE TO UNLOCK THIS AVATAR!", Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent();
            intent.putExtra("drawable_right", R.drawable.box_detective_right);
            intent.putExtra("drawable_left", R.drawable.box_detective_left);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
    }

    public void Criminal(View view)
    {
        if (high_score <= 1500)
        {
            Toast.makeText(this, "YOUR HIGH SCORE SHOULD BE 1500 OR MORE TO UNLOCK THIS AVATAR!", Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent();
            intent.putExtra("drawable_right", R.drawable.box_criminal_right);
            intent.putExtra("drawable_left", R.drawable.box_criminal_left);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
    }

    public void Moustache(View view)
    {
        if (high_score <= 2000)
        {
            Toast.makeText(this, "YOUR HIGH SCORE SHOULD BE 2000 OR MORE TO UNLOCK THIS AVATAR!", Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent();
            intent.putExtra("drawable_right", R.drawable.box_moustache_right);
            intent.putExtra("drawable_left", R.drawable.box_moustache_left);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
    }
}

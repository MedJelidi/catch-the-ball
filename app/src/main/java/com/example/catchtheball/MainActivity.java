package com.example.catchtheball;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private FrameLayout gameFrame;
    private int frameHeight, frameWidth, initialFrameWidth;
    private LinearLayout startLayout;

    private MediaPlayer soundtrack, soundtrack1, orangehit, explode, wide, transform2, game_over;

    private ImageView box, box_orange, box2, black1, black2, black3, black4, orange, pink1, pink2, blue, boxInit, initOrange, initBlack, initBlue, initPink, music, audio, musicOff, audioOff;
    private Drawable imageBoxRight, imageBoxLeft;

    private int boxSize, initialBoxSize;

    private float initialBoxX, initialBoxY, boxX, boxY, orangeX, orangeY, black1X, black1Y, black2X, black2Y, black3X, black3Y, black4X, black4Y,pink1X, pink1Y, pink2X, pink2Y, blueX, blueY;

    private TextView scoreLabel, highScoreLabel;
    private int score, highScore, timeCount;
    private SharedPreferences settings;

    private long timeLeft = 5000;
    private CountDownTimer mCountDownTimer;
    private Timer timer;
    private Handler handler = new Handler();

    private boolean start_flg = false;
    private boolean action_flg = false;
    private boolean pink1_flg = false;
    private boolean pink2_flg = false;
    private boolean blue_flg = false;
    private boolean small = false;
    private boolean timeRunning = false;
    private boolean gameIsOver;
    private boolean musicOn = true;
    private boolean audioOn = true, fama = false;
    private boolean orangeDone = false, greenDone = false, detectiveDone = false, criminalDone = false, moustacheDone = false;
    private int rightDrawable, leftDrawable;
    int maxVolume = 50;
    float soundScale = 0.0f;
    private RadioGroup mRadioGroupMode;
    private RadioButton easyMode, normalMode, hardMode;
    private RelativeLayout mRelativeLayoutMode;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                rightDrawable = data.getIntExtra("drawable_right", 0);
                leftDrawable = data.getIntExtra("drawable_left", 0);
                imageBoxRight = getResources().getDrawable(rightDrawable);
                imageBoxLeft = getResources().getDrawable(leftDrawable);
                boxInit = (ImageView)findViewById(R.id.boxInit);
                boxInit.setImageResource(rightDrawable);
                boxInit.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_anim));
                fama = true;
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                fama = false;
            }
        }
    }//onActivityResult


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (fama == false)
        {
            boxInit = findViewById(R.id.boxInit);
            boxInit.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_anim));
            imageBoxRight = getResources().getDrawable(R.drawable.box_right);
            imageBoxLeft = getResources().getDrawable(R.drawable.box_left);
        }

        mRadioGroupMode = (RadioGroup)findViewById(R.id.radioGroupMode);
        mRelativeLayoutMode = (RelativeLayout)findViewById(R.id.mode_layout);
        gameFrame = findViewById(R.id.gameFrame);
        startLayout = findViewById(R.id.startLayout);
        easyMode = (RadioButton)findViewById(R.id.easy_mode);
        normalMode = (RadioButton)findViewById(R.id.normal_mode);
        hardMode = (RadioButton)findViewById(R.id.hard_mode);
        box = findViewById(R.id.box);
        box2 = findViewById(R.id.box);
        initOrange = findViewById(R.id.initOrange);
        initPink = findViewById(R.id.initPink);
        initBlue = findViewById(R.id.initBlue);
        initBlack = findViewById(R.id.initBlack);
        black1 = findViewById(R.id.black1);
        black2 = findViewById(R.id.black2);
        black3 = findViewById(R.id.black3);
        black4 = findViewById(R.id.black4);
        blue = findViewById(R.id.blue);
        orange = findViewById(R.id.orange);
        pink1 = findViewById(R.id.pink1);
        pink2 = findViewById(R.id.pink2);
        music = findViewById(R.id.music);
        musicOff = findViewById(R.id.musicoff);
        audioOff = findViewById(R.id.audiooff);
        musicOff.setVisibility(View.INVISIBLE);
        audioOff.setVisibility(View.INVISIBLE);
        audio = findViewById(R.id.audio);
        scoreLabel = findViewById(R.id.scoreLabel);
        scoreLabel.setVisibility(View.INVISIBLE);
        highScoreLabel = findViewById(R.id.highScoreLabel);
        initialBoxX = box.getX();
        initialBoxY = box.getY();

       if (!Check.isSoundtrackPlaying())
        {
            soundtrack = MediaPlayer.create(MainActivity.this, R.raw.soundtrack);
            soundtrack.setLooping(true);
            soundtrack.start();
            Check.setSoundtrackPlaying(true);
        }

        /*soundtrack = MediaPlayer.create(MainActivity.this, R.raw.soundtrack);
        soundtrack.setLooping(true);
        soundtrack.start();*/

        orangehit = MediaPlayer.create(MainActivity.this, R.raw.orangehit);
        explode = MediaPlayer.create(MainActivity.this, R.raw.explode);
        wide = MediaPlayer.create(MainActivity.this, R.raw.wide);
        transform2 = MediaPlayer.create(MainActivity.this, R.raw.wrong);

        game_over = MediaPlayer.create(MainActivity.this, R.raw.game_over);

        //boxInit.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_anim));

        /*imageBoxRight = getResources().getDrawable(R.drawable.box_right);
        imageBoxLeft = getResources().getDrawable(R.drawable.box_left);*/

        //orangeHit = MediaPlayer.create(MainActivity.this, )

        settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        highScore = settings.getInt("GAME_SCORE", 0);
        highScoreLabel.setText("High Score : " + highScore);

    }


    public void avatarsDisplay(View view)
    {
        settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        highScore = settings.getInt("GAME_SCORE", 0);
        Intent intent = new Intent(this, Avatars.class);
        intent.putExtra("high_score",highScore);
        startActivityForResult(intent, 1);
    }


    public void changePos()
    {

        timeCount += 20;

        orangeY += 12;

        float orangeCenterX = orangeX + orange.getWidth() / 2;
        float orangeCenterY = orangeY + orange.getHeight() / 2;

        if (hitCheck(orangeCenterX, orangeCenterY))
        {
            if (audioOn)
            {
                orangehit.start();
            }
            orangeY = frameHeight + 100;
            score += 10;
        }

        if (orangeY > frameHeight)
        {
            orangeY = -100;
            orangeX = (float) Math.floor(Math.random() * (frameWidth - orange.getWidth()));
        }

        orange.setX(orangeX);
        orange.setY(orangeY);

        if (!pink1_flg && timeCount % 15000 == 0)
        {
            pink1_flg = true;
            pink1Y = -20;
            pink1X = (float) Math.floor(Math.random() * (frameWidth - pink1.getWidth()));
        }

        if (!pink2_flg && timeCount % 10000 == 0)
        {
            pink2_flg = true;
            pink2Y = -20;
            pink2X = (float) Math.floor(Math.random() * (frameWidth - pink2.getWidth()));
        }

        if (!blue_flg && timeCount % 20000 == 0)
        {
            blue_flg = true;
            blueY = -20;
            blueX = (float) Math.floor(Math.random() * (frameWidth - blue.getWidth()));
        }

        if (pink1_flg)
        {
            pink1Y += 20;

            float pink1CenterX = pink1X + pink1.getWidth() / 2;
            float pink1CenterY = pink1Y + pink1.getHeight() / 2;

            if (hitCheck(pink1CenterX, pink1CenterY))
            {
                if (audioOn)
                {
                    wide.start();
                }
                pink1Y = frameHeight + 30;
                score += 30;
                if (initialFrameWidth > frameWidth * 110 / 100)
                {
                    frameWidth = frameWidth * 110 / 100;
                    changeFrameWidth(frameWidth);
                }

            }

            if (pink1Y > frameHeight) pink1_flg = false;
            pink1.setX(pink1X);
            pink1.setY(pink1Y);
        }

        if (pink2_flg)
        {
            pink2Y += 32;

            float pink2CenterX = pink2X + pink2.getWidth() / 2;
            float pink2CenterY = pink2Y + pink2.getHeight() / 2;

            if (hitCheck(pink2CenterX, pink2CenterY))
            {
                if (audioOn)
                {
                    wide.start();
                }
                pink2Y = frameHeight + 30;
                score += 60;
                if (initialFrameWidth > frameWidth * 110 / 100)
                {
                    frameWidth = frameWidth * 120 / 100;
                    changeFrameWidth(frameWidth);
                }

            }

            if (pink2Y > frameHeight) pink2_flg = false;
            pink2.setX(pink2X);
            pink2.setY(pink2Y);
        }

        if (blue_flg)
        {
            blueY += 20;

            float blueCenterX = blueX + blue.getWidth() / 2;
            float blueCenterY = blueY + blue.getHeight() / 2;

            if (hitCheck(blueCenterX, blueCenterY))
            {
                blueY = frameHeight + 60;
                if (audioOn)
                {
                    transform2.start();
                }
                if (small)
                {
                    box.getLayoutParams().height = 132;
                    box.getLayoutParams().width = 132;
                    boxSize = 132;
                    box.requestLayout();
                    boxX = box.getX();
                    boxY = 1788.0f;
                    small = false;
                }
                else
                {
                    box.getLayoutParams().height = 66;
                    box.getLayoutParams().width = 66;
                    boxSize = 66;
                    box.requestLayout();
                    boxX = box.getX();
                    //boxY = box.getY();
                    boxY = 1844.0f;
                    small = true;
                }
            }

            if (blueY > frameHeight) blue_flg = false;
            blue.setX(blueX);
            blue.setY(blueY);
        }

        if (easyMode.isChecked())
        {
            easyGame();
        }
        else if (normalMode.isChecked())
        {
            normalGame();
        }
        else
        {
            hardGame();
        }


        if (action_flg)
        {
            boxX += 14;
            box.setImageDrawable(imageBoxRight);
        }
        else
        {
            boxX -= 14;
            box.setImageDrawable(imageBoxLeft);
        }

        if (boxX < 0)
        {
            boxX = 0;
            box.setImageDrawable(imageBoxRight);
        }
        else if (boxX > (frameWidth - boxSize))
        {
            boxX = frameWidth - boxSize;
            box.setImageDrawable(imageBoxLeft);
        }

        box.setX(boxX);
        scoreLabel.setText("Score : " + score);
    }

    public void easyGame()
    {
        black1Y += 14;
        black2Y += 18;

        float black1CenterX = black1X + black1.getWidth() / 2;
        float black1CenterY = black1Y + black1.getHeight() / 2;

        float black2CenterX = black2X + black2.getWidth() / 2;
        float black2CenterY = black2Y + black2.getHeight() / 2;

        if (hitCheck(black1CenterX, black1CenterY) || hitCheck(black2CenterX, black2CenterY))
        {
            if (audioOn)
            {
                explode.start();
            }
            if (hitCheck(black1CenterX, black1CenterY))
            {
                black1Y = frameHeight + 100;
            }
            else
            {
                black2Y = frameHeight + 100;
            }
            frameWidth = frameWidth * 80 / 100;
            changeFrameWidth(frameWidth);
            if (frameWidth <= boxSize)
            {
                stopTimer();
                gameIsOver = true;
                soundtrack.setVolume(0.0f, 0.0f);
                if (audioOn)
                {
                    game_over.start();
                }
                gameOver();
            }

        }

        if (black1Y > frameHeight)
        {
            black1Y = -100;
            black1X = (float) Math.floor(Math.random() * (frameWidth - black1.getWidth()));
        }

        if (black2Y > frameHeight)
        {
            black2Y = -100;
            black2X = (float) Math.floor(Math.random() * (frameWidth - black2.getWidth()));
        }

        black1.setX(black1X);
        black1.setY(black1Y);

        black2.setX(black2X);
        black2.setY(black2Y);

    }

    public void normalGame()
    {
        black1Y += 28;
        black2Y += 32;
        black3Y += 30;

        float black1CenterX = black1X + black1.getWidth() / 2;
        float black1CenterY = black1Y + black1.getHeight() / 2;

        float black2CenterX = black2X + black2.getWidth() / 2;
        float black2CenterY = black2Y + black2.getHeight() / 2;

        float black3CenterX = black3X + black3.getWidth() / 2;
        float black3CenterY = black3Y + black3.getHeight() / 2;

        if (hitCheck(black1CenterX, black1CenterY) || hitCheck(black2CenterX, black2CenterY) || hitCheck(black3CenterX, black3CenterY))
        {
            if (audioOn)
            {
                explode.start();
            }
            if (hitCheck(black1CenterX, black1CenterY))
            {
                black1Y = frameHeight + 100;
            }
            else if (hitCheck(black2CenterX, black2CenterY))
            {
                black2Y = frameHeight + 100;
            }
            else
            {
                black3Y = frameHeight + 100;
            }
            frameWidth = frameWidth * 80 / 100;
            changeFrameWidth(frameWidth);
            if (frameWidth <= boxSize)
            {
                stopTimer();
                gameIsOver = true;
                soundtrack.setVolume(0.0f, 0.0f);
                if (audioOn)
                {
                    game_over.start();
                }
                gameOver();
            }

        }

        if (black1Y > frameHeight)
        {
            black1Y = -100;
            black1X = (float) Math.floor(Math.random() * (frameWidth - black1.getWidth()));
        }

        if (black2Y > frameHeight)
        {
            black2Y = -100;
            black2X = (float) Math.floor(Math.random() * (frameWidth - black2.getWidth()));
        }

        if (black3Y > frameHeight)
        {
            black3Y = -100;
            black3X = (float) Math.floor(Math.random() * (frameWidth - black3.getWidth()));
        }

        black1.setX(black1X);
        black1.setY(black1Y);

        black2.setX(black2X);
        black2.setY(black2Y);

        black3.setX(black3X);
        black3.setY(black3Y);
    }

    public void hardGame()
    {
        black1Y += 28;
        black2Y += 32;
        black3Y += 30;
        black4Y += 34;

        float black1CenterX = black1X + black1.getWidth() / 2;
        float black1CenterY = black1Y + black1.getHeight() / 2;

        float black2CenterX = black2X + black2.getWidth() / 2;
        float black2CenterY = black2Y + black2.getHeight() / 2;

        float black3CenterX = black3X + black3.getWidth() / 2;
        float black3CenterY = black3Y + black3.getHeight() / 2;

        float black4CenterX = black4X + black4.getWidth() / 2;
        float black4CenterY = black4Y + black4.getHeight() / 2;

        if (hitCheck(black1CenterX, black1CenterY) || hitCheck(black2CenterX, black2CenterY) || hitCheck(black3CenterX, black3CenterY) | hitCheck(black4CenterX, black4CenterY))
        {
            if (audioOn)
            {
                explode.start();
            }
            if (hitCheck(black1CenterX, black1CenterY))
            {
                black1Y = frameHeight + 100;
            }
            else if (hitCheck(black2CenterX, black2CenterY))
            {
                black2Y = frameHeight + 100;
            }
            else if (hitCheck(black3CenterX, black3CenterY))
            {
                black3Y = frameHeight + 100;
            }
            else {
                black4Y = frameHeight + 100;
            }

            frameWidth = frameWidth * 80 / 100;
            changeFrameWidth(frameWidth);
            if (frameWidth <= boxSize)
            {
                stopTimer();
                gameIsOver = true;
                soundtrack.setVolume(0.0f, 0.0f);
                if (audioOn)
                {
                    game_over.start();
                }
                gameOver();
            }

        }

        if (black1Y > frameHeight)
        {
            black1Y = -100;
            black1X = (float) Math.floor(Math.random() * (frameWidth - black1.getWidth()));
        }

        if (black2Y > frameHeight)
        {
            black2Y = -100;
            black2X = (float) Math.floor(Math.random() * (frameWidth - black2.getWidth()));
        }

        if (black3Y > frameHeight)
        {
            black3Y = -100;
            black3X = (float) Math.floor(Math.random() * (frameWidth - black3.getWidth()));
        }

        if (black4Y > frameHeight)
        {
            black4Y = -100;
            black4X = (float) Math.floor(Math.random() * (frameWidth - black4.getWidth()));
        }

        black1.setX(black1X);
        black1.setY(black1Y);

        black2.setX(black2X);
        black2.setY(black2Y);

        black3.setX(black3X);
        black3.setY(black3Y);

        black4.setX(black4X);
        black4.setY(black4Y);
    }

    public void gameOver()
    {
        boxInit.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_anim));
        timer.cancel();
        timer = null;
        start_flg = false;
        mRadioGroupMode.setVisibility(View.VISIBLE);
        mRelativeLayoutMode.setVisibility(View.VISIBLE);

        try {
            TimeUnit.SECONDS.sleep(2);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        changeFrameWidth(initialFrameWidth);
        box.getLayoutParams().height = 132;
        box.getLayoutParams().width = 132;
        boxSize = 132;
        box.requestLayout();
        boxX = 0.0f;
        boxY = 1788.0f;
        boxInit.setVisibility(View.VISIBLE);
        if (musicOn)
        {
            music.setVisibility(View.VISIBLE);
        }
        else
        {
            musicOff.setVisibility(View.VISIBLE);
        }
        if (audioOn)
        {
            audio.setVisibility(View.VISIBLE);
        }
        else
        {
            audioOff.setVisibility(View.VISIBLE);
        }
        initOrange.setVisibility(View.VISIBLE);
        initBlue.setVisibility(View.VISIBLE);
        initBlack.setVisibility(View.VISIBLE);
        initPink.setVisibility(View.VISIBLE);
        startLayout.setVisibility(View.VISIBLE);
        box.setVisibility(View.INVISIBLE);
        orange.setVisibility(View.INVISIBLE);
        pink1.setVisibility(View.INVISIBLE);
        pink2.setVisibility(View.INVISIBLE);
        black1.setVisibility(View.INVISIBLE);
        black2.setVisibility(View.INVISIBLE);
        black3.setVisibility(View.INVISIBLE);
        black4.setVisibility(View.INVISIBLE);
        blue.setVisibility(View.INVISIBLE);


        if (score > highScore)
        {
            highScore = score;
            highScoreLabel.setText("High Score : " + highScore);

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("GAME_SCORE", highScore);
            editor.commit();
        }

        checkAvatars(score);

        if (musicOn)
        {
            soundtrack.setVolume(1.0f, 1.0f);
        }

    }

    public void checkAvatars(int score)
    {
        orangeDone = settings.getBoolean("ORANGE_DONE", false);
        greenDone = settings.getBoolean("GREEN_DONE", false);
        detectiveDone = settings.getBoolean("DETECTIVE_DONE", false);
        criminalDone = settings.getBoolean("CRIMINAL_DONE", false);
        moustacheDone = settings.getBoolean("MOUSTACHE_DONE", false);

        if (score >= 300)
        {
            if (orangeDone == false)
            {
                Toast toast = Toast.makeText(this, "YOU'VE UNLOCKED A NEW AVATAR!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 30);
                LinearLayout toastContentView = (LinearLayout) toast.getView();
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setImageResource(R.drawable.box_orange_notif);
                toastContentView.addView(imageView, 0);
                toast.show();
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("ORANGE_DONE", true);
                editor.commit();
            }
        }

        if (score >= 700)
        {
            if (greenDone == false)
            {
                Toast toast = Toast.makeText(this, "YOU'VE UNLOCKED A NEW AVATAR!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 30);
                LinearLayout toastContentView = (LinearLayout) toast.getView();
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setImageResource(R.drawable.box_green_notif);
                toastContentView.addView(imageView, 0);
                toast.show();
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("GREEN_DONE", true);
                editor.commit();
                greenDone = true;
            }
        }

        if (score >= 1000)
        {
            if (detectiveDone == false)
            {
                Toast toast = Toast.makeText(this, "YOU'VE UNLOCKED A NEW AVATAR!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 30);
                LinearLayout toastContentView = (LinearLayout) toast.getView();
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setImageResource(R.drawable.box_detective_notif);
                toastContentView.addView(imageView, 0);
                toast.show();
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("DETECTIVE_DONE", true);
                editor.commit();
                detectiveDone = true;
            }
        }

        if (score >= 1500)
        {
            if (criminalDone == false)
            {
                Toast toast = Toast.makeText(this, "YOU'VE UNLOCKED A NEW AVATAR!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 30);
                LinearLayout toastContentView = (LinearLayout) toast.getView();
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setImageResource(R.drawable.box_criminal_notif);
                toastContentView.addView(imageView, 0);
                toast.show();
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("CRIMINAL_DONE", true);
                editor.commit();
                criminalDone = true;
            }
        }

        if (score >= 2000)
        {
            if (moustacheDone == false)
            {
                Toast toast = Toast.makeText(this, "YOU'VE UNLOCKED A NEW AVATAR!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 30);
                LinearLayout toastContentView = (LinearLayout) toast.getView();
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setImageResource(R.drawable.box_moustache_notif);
                toastContentView.addView(imageView, 0);
                toast.show();
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("MOUSTACHE_DONE", true);
                editor.commit();
                moustacheDone = true;
            }
        }

    }

    public void changeFrameWidth(int frameWidth)
    {
        ViewGroup.LayoutParams params = gameFrame.getLayoutParams();
        params.width = frameWidth;
        gameFrame.setLayoutParams(params);
    }

    public boolean hitCheck(float x, float y)
    {
        if (boxX <= x && x <= boxX + boxSize && boxY <= y && y <= frameHeight)
        {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (start_flg)
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                action_flg = true;
                if (timeRunning)
                {
                    stopTimer();
                }
            }
            else if (event.getAction() == MotionEvent.ACTION_UP)
            {
                action_flg = false;
                startTimer();
            }
        }
        return true;
    }

    public void startTimer()
    {
        mCountDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                frameWidth = frameWidth * 80 / 100;
                changeFrameWidth(frameWidth);
                timeLeft = 5000;
                startTimer();
            }
        }.start();
        timeRunning = true;
    }

    public void stopTimer()
    {
        mCountDownTimer.cancel();
        timeLeft = 5000;
        timeRunning = false;
    }

    public void startGame(View view)
    {
        musicOff.setVisibility(View.INVISIBLE);
        audioOff.setVisibility(View.INVISIBLE);
        mRadioGroupMode.setVisibility(View.INVISIBLE);
        mRelativeLayoutMode.setVisibility(View.INVISIBLE);
        boxInit.clearAnimation();
        start_flg = true;
        gameIsOver = false;
        scoreLabel.setVisibility(View.VISIBLE);
        startLayout.setVisibility(View.INVISIBLE);
        music.setVisibility(View.INVISIBLE);
        audio.setVisibility(View.INVISIBLE);
        boxInit.setVisibility(View.INVISIBLE);
        initOrange.setVisibility(View.INVISIBLE);
        initBlue.setVisibility(View.INVISIBLE);
        initBlack.setVisibility(View.INVISIBLE);
        initPink.setVisibility(View.INVISIBLE);


        if (frameHeight == 0)
        {
            frameHeight = gameFrame.getHeight();
            frameWidth = gameFrame.getWidth();
            initialFrameWidth = frameWidth;

            boxSize = box.getHeight();
            initialBoxSize = boxSize;
            boxX = box.getX();
            boxY = box.getY();
        }

        frameWidth = initialFrameWidth;

        box.setX(0.0f);
        orange.setY(3000.0f);
        pink1.setY(3000.0f);
        pink2.setY(3000.0f);
        black1.setY(3000.0f);
        black2.setY(3000.0f);
        black3.setY(3000.0f);
        black4.setY(3000.0f);
        blue.setY(3000.0f);

        orangeY = orange.getY();
        pink1Y = pink1.getY();
        pink2Y = pink2.getY();
        black1Y = black1.getY();
        black2Y = black2.getY();
        black3Y = black3.getY();
        black4Y = black4.getY();
        blueY = blue.getY();



        box.setVisibility(View.VISIBLE);
        orange.setVisibility(View.VISIBLE);
        pink1.setVisibility(View.VISIBLE);
        pink2.setVisibility(View.VISIBLE);
        black1.setVisibility(View.VISIBLE);
        black2.setVisibility(View.VISIBLE);
        black3.setVisibility(View.VISIBLE);
        black4.setVisibility(View.VISIBLE);
        blue.setVisibility(View.VISIBLE);


        timeCount = 0;
        score = 0;
        scoreLabel.setText("Score : 0");

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (start_flg)
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }
        }, 0, 20);
    }

    public void quitGame(View view)
    {
        soundtrack.stop();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            finishAndRemoveTask();
        }
        else
        {
            finish();
        }
    }

    public void onOffMusic(View view)
    {
        if (musicOn)
        {
            soundtrack.setVolume(0.0f, 0.0f);
            musicOff.setVisibility(View.VISIBLE);
            music.setVisibility(View.INVISIBLE);
            musicOn = false;
        }
        else
        {
            soundtrack.setVolume(1.0f, 1.0f);
            musicOff.setVisibility(View.INVISIBLE);
            music.setVisibility(View.VISIBLE);
            musicOn = true;
        }
    }

    public void onOffAudio(View view)
    {
        if (audioOn)
        {
            audioOn = false;
            audioOff.setVisibility(View.VISIBLE);
            audio.setVisibility(View.INVISIBLE);
        }
        else
        {
            audioOn = true;
            audioOff.setVisibility(View.INVISIBLE);
            audio.setVisibility(View.VISIBLE);
        }
    }

    public void resetScore(View view)
    {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("ORANGE_DONE", false);
        editor.putBoolean("GREEN_DONE", false);
        editor.putBoolean("DETECTIVE_DONE", false);
        editor.putBoolean("CRIMINAL_DONE", false);
        editor.putBoolean("MOUSTACHE_DONE", false);
        editor.putInt("GAME_SCORE", 0);
        editor.commit();
        highScore = 0;
        highScoreLabel.setText("High Score : 0");
    }
}

package com.example.carrace.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carrace.CarGame;
import com.example.carrace.R;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class Activity_game extends AppCompatActivity {

    public static final String SENSOR_TYPE = "SENSOR_TYPE";
    public static final String SCORE = "SCORE";
    private ImageView[] lives;
    private ImageView[][] path;
    private Button[] movementButtons;
    private CarGame gameManager;
    private Timer timer = new Timer();
    private TextView score;
    private int counter = 0;
    private MediaPlayer player;
    private SensorManager sensorManager;
    private Sensor accSensor;
    private boolean sensorsFlag;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        findViews();
        // starting game
        gameManager = new CarGame();
        gameManager.startGame(path.length, path[0].length,lives.length,300);
        // getting input sensors or buttons
        Bundle bundle = getIntent().getBundleExtra(Activity_Menu.BUNDLE);
        sensorsFlag = bundle.getBoolean(SENSOR_TYPE);
        initSensor();
        if(sensorsFlag){
            for (int i = 0; i < movementButtons.length; i++){
                movementButtons[i].setVisibility(View.INVISIBLE);
            }
        }
        else{
            initButton();
        }

    }

    private void startTicker() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        counter++;
                        score.setText(""+counter);
                        int status = gameManager.next(counter);
                        //hit
                        if(status == 1){
                            vibrate();
                            popUpMsg(gameManager.getLives()+" lives left!!");

                            player = MediaPlayer.create(getApplicationContext(),R.raw.hit_sound);
                            player.start();
                            player.setOnCompletionListener(MediaPlayer::pause);
                        }
                        //game over
                        if(status == 2){
                            gameOver();
                        }
                        if(status == 3){
                            counter += 100;
                            score.setText(""+counter);
                            player = MediaPlayer.create(getApplicationContext(),R.raw.coin_sound);
                            player.start();
                            player.setOnCompletionListener(MediaPlayer::pause);
                        }
                        updateUI();
                    }
                });
            }
        }, 0, gameManager.getSpeed());
    }
    private void stopTicker() {
        timer.cancel();
    }

    private void updateUI() {
        //update lives
        int livesValue = gameManager.getLives();
        for (int i = 0; i <lives.length; i++) {
            ImageView im = lives[i];
            if(i<livesValue){
                im.setImageResource(R.drawable.img_heart);
            }else{
                im.setImageResource(R.drawable.img_heart_empty);
            }
        }
        //update path
        for (int i = 0; i < path.length; i++) {
            for (int j = 0; j < path[i].length; j++) {
                ImageView im = path[i][j];
                if (gameManager.getVals()[i][j] == 0) {
                    im.setVisibility(View.INVISIBLE);
                } else if (gameManager.getVals()[i][j] == 1) {
                    im.setVisibility(View.VISIBLE);
                    im.setImageResource(R.drawable.img_rock);
                } else if (gameManager.getVals()[i][j] == 2) {
                    im.setVisibility(View.VISIBLE);
                    im.setImageResource(R.drawable.img_racing_car);
                } else if (gameManager.getVals()[i][j] == 3) {
                    im.setVisibility(View.VISIBLE);
                    im.setImageResource(R.drawable.img_boom);
                }else if (gameManager.getVals()[i][j] == 4) {
                    im.setVisibility(View.VISIBLE);
                    im.setImageResource(R.drawable.img_coin);
                }

            }
        }
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }
    private void popUpMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void gameOver(){
        stopTicker();
        Intent Intent = new Intent(Activity_game.this, Activity_Over.class);
        Intent.putExtra(SCORE, counter);
        startActivity(Intent);
        this.finish();
    }
    //------- sensors -----
    private void initSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private SensorEventListener accSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            DecimalFormat df = new DecimalFormat("##.##");
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            if(sensorsFlag){
                if(x>2){
                    gameManager.moveCar(0);
                }else if(x<-2){
                    gameManager.moveCar(1);
                }
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(accSensorEventListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(accSensorEventListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startTicker();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopTicker();
    }

    private void initButton() {
        for (int i = 0; i < movementButtons.length; i++) {
            int finalI = i;
            movementButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gameManager.moveCar(finalI);
                }
            });
        }
    }
    private void findViews() {

        //find lives
        lives = new ImageView[] {findViewById(R.id.img_heart1),findViewById(R.id.img_heart2),findViewById(R.id.img_heart3)};
        //find score
        score = findViewById(R.id.tv_score);
        //find path
        path = new ImageView[][]{
                {findViewById(R.id.img_id_00), findViewById(R.id.img_id_01), findViewById(R.id.img_id_02), findViewById(R.id.img_id_03), findViewById(R.id.img_id_04)},
                {findViewById(R.id.img_id_10), findViewById(R.id.img_id_11), findViewById(R.id.img_id_12), findViewById(R.id.img_id_13), findViewById(R.id.img_id_14)},
                {findViewById(R.id.img_id_20), findViewById(R.id.img_id_21), findViewById(R.id.img_id_22), findViewById(R.id.img_id_23), findViewById(R.id.img_id_24)},
                {findViewById(R.id.img_id_30), findViewById(R.id.img_id_31), findViewById(R.id.img_id_32), findViewById(R.id.img_id_33), findViewById(R.id.img_id_34)},
                {findViewById(R.id.img_id_40), findViewById(R.id.img_id_41), findViewById(R.id.img_id_42), findViewById(R.id.img_id_43), findViewById(R.id.img_id_44)},
                {findViewById(R.id.img_id_50), findViewById(R.id.img_id_51), findViewById(R.id.img_id_52), findViewById(R.id.img_id_53), findViewById(R.id.img_id_54)}
        };
        //buttons
        movementButtons = new Button[]{findViewById(R.id.btn_left), findViewById(R.id.btn_right)};
    }
}
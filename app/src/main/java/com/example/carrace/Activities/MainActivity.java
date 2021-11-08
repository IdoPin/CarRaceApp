package com.example.carrace.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.carrace.CarGame;
import com.example.carrace.R;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private LinearLayout gameLayout;
    private LinearLayout gameOverLayout;
    private ImageView[] lives;
    private ImageView[][] path;
    private Button[] movementButtons;
    private CarGame gameManager;
    private Timer timer = new Timer();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initButton();
        gameManager = new CarGame();
        gameManager.startGame(path.length, path[0].length,lives.length,500);

    }

    private void startTicker() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int status = gameManager.next();
                        //hit
                        if(status == 1){
                            vibrate();
                            popUpMsg(gameManager.getLives()+" lives left!!");
                        }
                        //game over
                        if(status == 2){
                            gameOver();
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
        gameLayout.setVisibility(View.INVISIBLE);
        gameOverLayout.setVisibility(View.VISIBLE);
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

    private void findViews() {
        //layouts
        gameLayout = findViewById(R.id.game_layout);
        gameOverLayout = findViewById(R.id.game_over);
        //find lives
        lives = new ImageView[] {findViewById(R.id.img_heart1),findViewById(R.id.img_heart2),findViewById(R.id.img_heart3)};
        //find path
        path = new ImageView[][]{
                {findViewById(R.id.img_id_00), findViewById(R.id.img_id_01), findViewById(R.id.img_id_02)},
                {findViewById(R.id.img_id_10), findViewById(R.id.img_id_11), findViewById(R.id.img_id_12)},
                {findViewById(R.id.img_id_20), findViewById(R.id.img_id_21), findViewById(R.id.img_id_22)},
                {findViewById(R.id.img_id_30), findViewById(R.id.img_id_31), findViewById(R.id.img_id_32)}
        };
        //buttons
        movementButtons = new Button[]{findViewById(R.id.btn_left), findViewById(R.id.btn_right)};
    }
}
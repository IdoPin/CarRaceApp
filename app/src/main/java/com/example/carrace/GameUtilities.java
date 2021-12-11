package com.example.carrace;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

public class GameUtilities {
    private static GameUtilities instance;
    private static Context appContext;

    public static GameUtilities getInstance() {
        return instance;
    }

    private GameUtilities(Context context) {
        appContext = context;
    }

    public static GameUtilities initHelper(Context context) {
        if (instance == null)
            instance = new GameUtilities(context);
        return instance;
    }

    public void vibrate(int millisecond) {
        Vibrator v = (Vibrator) appContext.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(millisecond, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(millisecond);
        }
    }
    private void popUpMsg(String msg){
        Toast.makeText(appContext, msg, Toast.LENGTH_SHORT).show();
    }

    public void playSound(int audio) {

        final MediaPlayer mp = MediaPlayer.create(appContext, audio);
        mp.start();
        mp.setOnCompletionListener(MediaPlayer::pause);

    }
}

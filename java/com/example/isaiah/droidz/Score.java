package com.example.isaiah.droidz;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Isaiah on 7/28/15.
 */
public class Score {

    private boolean isHighScore;
    private int Score;

    public Score(int score, Activity ac){
        Score = score;

        SharedPreferences scoreIn = ac.getPreferences(ac.MODE_MULTI_PROCESS);

        Log.d("score", "All: " + scoreIn.getAll().toString());

        int current = scoreIn.getInt("s_core", score);
        Log.d("score", current+"");
        if(score > current){
            Log.d("score", "is highscore");
            isHighScore = true;
            SharedPreferences.Editor editor = scoreIn.edit();

            editor.putInt("s_core", score);

            editor.apply();
        }
    }

    public boolean isHighScore(){
        return isHighScore;
    }

    public int getScore(){
        return Score;
    }
}

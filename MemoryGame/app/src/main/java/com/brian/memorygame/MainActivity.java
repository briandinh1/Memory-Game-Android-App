package com.brian.memorygame;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Random mRandom;
    private ArrayList<Integer> mSequence;
    private Button[] mButtons;
    private int mCurrentPos;
    private Button mButtonReset;
    private boolean mClickable;
    private int mCurrentRound;
    private TextView mTextViewRound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mRandom = new Random();
        mSequence = new ArrayList<Integer>();
        mButtons = new Button[9];
        mButtonReset = findViewById(R.id.button_reset);
        mClickable = false;
        mCurrentRound = 1;
        mTextViewRound = findViewById(R.id.text_view_round);


        for (int i = 0; i < 9; i++) {
            String buttonID = "button_" + i;
            int resourceID = getResources().getIdentifier(buttonID, "id", getPackageName());
            mButtons[i] = findViewById(resourceID);
            mButtons[i].setOnClickListener(this);
            mButtons[i].setTag(i);
        }

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetBoard();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                generateSequence();
            }
        }, 1000);
    }

    // general onclick for all 9 of the game buttons
    @Override
    public void onClick(View view) {
        if (mClickable) {
            if (view.getTag() == mSequence.get(mCurrentPos)) {
                ++mCurrentPos;
                if (mCurrentPos >= mSequence.size()) {
                    updatePoints();
                    generateSequence();
                }
            }
            else resetBoard();
        }
    }

    private void generateSequence() {
        mClickable = false; // prevent clicking of buttons while generating sequence
        mSequence.clear();
        mCurrentPos = 0;
        for (int i = 1; i <= mCurrentRound + 2; i++) {
            final int rand = mRandom.nextInt(9);
            mSequence.add(rand);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mButtons[rand].setBackgroundColor(Color.BLUE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mButtons[rand].setBackground(mButtonReset.getBackground());
                        }
                    }, 300);
                }
            }, 500 * i);
        }

        // after generated, player can click again
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                mClickable = true;
            }
        }, 500 * (mCurrentRound + 2));
    }

    private void resetBoard() {
        this.recreate();
    }

    private void updatePoints() {
        ++mCurrentRound;
        mTextViewRound.setText("Round " + mCurrentRound);
    }
}

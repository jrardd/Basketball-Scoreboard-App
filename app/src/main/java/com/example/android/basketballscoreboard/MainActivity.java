package com.example.android.basketballscoreboard;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final long MAIN_START_TIME_MILLI = 720000;
    private static final long SHOT_CLOCK_START_TIME_MILLI = 24000;

    private TextView mainClock;
    private TextView shotClock;

    private CountDownTimer mCountDownTimer;
    private CountDownTimer sCountDownTimer;

    private boolean isMainClockRunning;
    private boolean isShotClockRunning;

    private long mainTimeLeftMilli = MAIN_START_TIME_MILLI;
    private long shotClockTimeLeftMilli = SHOT_CLOCK_START_TIME_MILLI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainClock = findViewById(R.id.main_clock);
        shotClock = findViewById(R.id.shot_clock);

        ImageButton startPause = findViewById(R.id.start_pause);
        ImageButton resetShotClock = findViewById(R.id.reset_shotclock_btn);
        Button setFourteenButton = findViewById(R.id.set_14);
        Button resetTimers = findViewById(R.id.reset_time_btn);

        startPause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isMainClockRunning) {
                    pauseTimer();
                } else {
                    startMainClock();
                    startShotClock();
                }
            }
        });

        resetShotClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetShotClock();
            }
        });

        setFourteenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFourteen();
            }
        });

        resetTimers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetMainClock();
                resetShotClock();
            }
        });
    }

    public void startMainClock() {

        mCountDownTimer = new CountDownTimer(mainTimeLeftMilli, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                mainTimeLeftMilli = millisUntilFinished;
                updateMainClock();
            }

            @Override
            public void onFinish() {
                pauseTimer();
            }
        }.start();

        isMainClockRunning = true;
    }

    public void startShotClock() {

        sCountDownTimer = new CountDownTimer(shotClockTimeLeftMilli, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                shotClockTimeLeftMilli = millisUntilFinished;
                updateShotClock();

            }

            @Override
            public void onFinish() {
                pauseTimer();
                resetShotClock();
            }
        }.start();

        isShotClockRunning = true;
    }

    public void pauseTimer() {
        mCountDownTimer.cancel();
        sCountDownTimer.cancel();
        isMainClockRunning = false;
        isShotClockRunning = false;
    }

    public void resetMainClock() {
        if (isMainClockRunning) {
            mCountDownTimer.cancel();
            mainTimeLeftMilli = MAIN_START_TIME_MILLI;
            startMainClock();
            updateMainClock();
        } else {
            mainTimeLeftMilli = MAIN_START_TIME_MILLI;
            updateMainClock();
        }
    }

    public void resetShotClock() {


        if (isShotClockRunning) {

            if (mainTimeLeftMilli <= 24000) {
                sCountDownTimer.cancel();
                shotClockTimeLeftMilli = mainTimeLeftMilli;
                updateShotClock();
                startShotClock();
            } else {
                sCountDownTimer.cancel();
                shotClockTimeLeftMilli = SHOT_CLOCK_START_TIME_MILLI;
                startShotClock();
                updateShotClock();
            }
        } else {

            if (mainTimeLeftMilli <= 24000) {
                shotClockTimeLeftMilli = mainTimeLeftMilli;
                updateShotClock();
            } else {
                shotClockTimeLeftMilli = SHOT_CLOCK_START_TIME_MILLI;
                updateShotClock();
            }
        }
    }

    public void setFourteen() {
        if (isShotClockRunning) {
            sCountDownTimer.cancel();
            shotClockTimeLeftMilli = 14000;
            startShotClock();
            updateShotClock();
        } else {
            shotClockTimeLeftMilli = 14000;
            updateShotClock();
        }
    }

    public void updateMainClock() {
        int minutes = (int) (mainTimeLeftMilli / 1000) / 60;
        int seconds = (int) (mainTimeLeftMilli / 1000) % 60;

        String timeFormat = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mainClock.setText(timeFormat);
        
    }

    public void updateShotClock() {
        int seconds = (int) (shotClockTimeLeftMilli / 1000) % 60;
        String timeFormat = String.format(Locale.getDefault(), "%02d", seconds);


        shotClock.setText(timeFormat);

    }

}

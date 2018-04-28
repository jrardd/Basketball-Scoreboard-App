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


    private static final long MAIN_START_TIME_MILLI = 720000;           //Starting time constants
    private static final long SHOT_CLOCK_START_TIME_MILLI = 24000;

    private TextView mainClock;
    private TextView shotClock;

    private CountDownTimer mCountDownTimer;
    private CountDownTimer sCountDownTimer;

    private boolean isMainClockRunning;
    private boolean isShotClockRunning;

    private long mainTimeLeftMilli = MAIN_START_TIME_MILLI;
    private long shotClockTimeLeftMilli = SHOT_CLOCK_START_TIME_MILLI;

    private int homeScore = 0;
    private int awayScore = 0;
    private int fouls = 0;
    private int timeouts = 6;
    private int period = 1;


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

    //The next few methods are used for the main clock and shot clock.

    /*
     Initialize main clock CountDownTimer with one second (1000 millisecond) ticks.
     startMainClock()
     returns: void
     */

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

    /*
     Initialize shot clock CountDownTimer with one second (1000 millisecond) ticks.
     startShotClock()
     returns: void
     */

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

    /*
     Pause all clocks
     pauseTimer()
     returns: void
     */

    public void pauseTimer() {
        mCountDownTimer.cancel();
        sCountDownTimer.cancel();
        isMainClockRunning = false;
        isShotClockRunning = false;
    }

    /*
     Reset the main clock to starting time value (12 Minutes).
     resetMainClock()
     returns: void
     */

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

    /*
     Reset the shot clock to starting time value (24 seconds).
     If the main clock is less than 24 seconds, the shot clock will match the main clock instead.
     resetShotClock()
     returns: void
     */

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

    /*
     Sets the shot clock to 14 seconds.
     setFourteen()
     returns: void
     */

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

    /*
     Updates the main clock TextView with formatted time string (00:00)
     updateMainClock()
     returns: void
     */

    public void updateMainClock() {
        int minutes = (int) (mainTimeLeftMilli / 1000) / 60;
        int seconds = (int) (mainTimeLeftMilli / 1000) % 60;

        String timeFormat = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mainClock.setText(timeFormat);

    }

    /*
     Updates the shot clock TextView with formatted time string (00)
     updateMainClock()
     returns: void
     */

    public void updateShotClock() {
        int seconds = (int) (shotClockTimeLeftMilli / 1000) % 60;
        String timeFormat = String.format(Locale.getDefault(), "%02d", seconds);


        shotClock.setText(timeFormat);

    }


    //The next few methods are used for the period, scoring, fouls, and timeouts.


    public void addHomeScore(View view) {
        homeScore++;
        updateHomeScore();
    }

    public void minusHomeScore(View view) {
        if (homeScore > 0) {
            homeScore--;
        }
        updateHomeScore();
    }

    public void updateHomeScore(){
        TextView homeScoreText = findViewById(R.id.home_score);

        if(homeScore < 100){
            String scoreFormat = String.format(Locale.getDefault(), "\t\t\t\t\t\t"+ "%02d", homeScore);
            homeScoreText.setText(scoreFormat);

        }

        else {
            String scoreFormat = String.format(Locale.getDefault(), "%03d", homeScore);
            homeScoreText.setText(scoreFormat);
        }

    }

    public void addAwayScore(View view) {
        awayScore++;
        updateAwayScore();
    }

    public void minusAwayScore(View view) {
        if (awayScore > 0) {
            awayScore--;
        }
        updateAwayScore();
    }

    public void updateAwayScore() {
        TextView awayScoreText = findViewById(R.id.away_score);

        if(awayScore < 100){
            String scoreFormat = String.format(Locale.getDefault(), "\t\t\t\t\t\t"+ "%02d", awayScore);
            awayScoreText.setText(scoreFormat);

        }

        else {
            String scoreFormat = String.format(Locale.getDefault(), "%02d", awayScore);
            awayScoreText.setText(scoreFormat);
        }
    }

    public void addPeriod(View view) {
        if (period < 4) {
            period++;
        }
        updatePeriod();
    }

    public void minusPeriod(View view) {
        if (period > 1) {
            period--;
        }
        updatePeriod();
    }

    public void updatePeriod() {
        TextView periodText = findViewById(R.id.period_number);
        String periodString = String.valueOf(period);
        periodText.setText(periodString);
    }


}

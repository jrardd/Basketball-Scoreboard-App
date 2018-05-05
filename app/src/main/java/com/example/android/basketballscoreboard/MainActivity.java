package com.example.android.basketballscoreboard;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.GridLayoutAnimationController;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private static final long MAIN_START_TIME_MILLI = 720000;           //Starting main clock time constant
    private static final long SHOT_CLOCK_START_TIME_MILLI = 24000;      //Starting shot clock time constant

    private TextView mainClock;
    private TextView shotClock;
    private CheckBox startPause;
    private ImageButton resetShotClock;
    private Button setFourteenButton;
    private Button resetTimers;

    private CountDownTimer mCountDownTimer;                             //Main clock CountDownTimer
    private CountDownTimer sCountDownTimer;                             //Shot clock CountDownTimer

    private boolean isPortrait;
    private boolean isMainClockRunning;
    private boolean isShotClockRunning;
    private boolean orientationLocked = false;

    private long mainTimeLeftMilli = MAIN_START_TIME_MILLI;
    private long shotClockTimeLeftMilli = SHOT_CLOCK_START_TIME_MILLI;

    private int homeScore = 0;
    private int homeFouls = 0;
    private int homeTimeouts = 6;

    private int awayScore = 0;
    private int awayFouls = 0;
    private int awayTimeouts = 6;

    private int period = 1;


    /*
    Method called upon app startup
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainClock = findViewById(R.id.main_clock);
        shotClock = findViewById(R.id.shot_clock);
        startPause = findViewById(R.id.start_pause);
        resetShotClock = findViewById(R.id.reset_shotclock_btn);
        setFourteenButton = findViewById(R.id.set_14);
        resetTimers = findViewById(R.id.reset_time_btn);
        isPortrait = this.getResources().getBoolean(R.bool.is_portrait);


        startPause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isMainClockRunning) {
                    pauseTimer();
                    Log.i("Message", "Clicked");
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


    /*
    Method called upon orientation change
    
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_main);
        isPortrait = this.getResources().getBoolean(R.bool.is_portrait);
        mainClock = findViewById(R.id.main_clock);
        shotClock = findViewById(R.id.shot_clock);
        startPause = findViewById(R.id.start_pause);
        resetShotClock = findViewById(R.id.reset_shotclock_btn);
        setFourteenButton = findViewById(R.id.set_14);
        resetTimers = findViewById(R.id.reset_time_btn);
        String mode;


        if (isMainClockRunning) {
            startPause.setChecked(true);
        }

        if (isPortrait) {
            mode = "Referee Mode";
            updateAllPortrait();
        } else {
            mode = "Free Mode";
            updateAllLandscape();
        }
        Toast.makeText(this, mode, Toast.LENGTH_LONG).show();
        updateMainClock();
        updateShotClock();


        startPause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isMainClockRunning) {
                    pauseTimer();
                    Log.i("Message", "Clicked");
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
        CheckBox startPause = findViewById(R.id.start_pause);
        mCountDownTimer.cancel();
        sCountDownTimer.cancel();
        isMainClockRunning = false;
        isShotClockRunning = false;
        startPause.setChecked(false);
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
     updateShotClock()
     returns: void
     */

    public void updateShotClock() {
        int seconds = (int) (shotClockTimeLeftMilli / 1000) % 60;
        String timeFormat = String.format(Locale.getDefault(), "%02d", seconds);
        shotClock.setText(timeFormat);

    }


    //The next few methods are used for the period, scoring, fouls, and timeouts.


    /*
     Adds 1 point to Home Score and updates view
     addHomeScore()
     returns: void
     */

    public void addHomeScore(View view) {
        homeScore++;
        updateHomeScore();
    }

    /*
     Subtracts 1 point from Home Score and updates view
     minusHomeScore()
     returns: void
     */

    public void minusHomeScore(View view) {
        if (homeScore > 0) {
            homeScore--;
        }
        updateHomeScore();
    }

    /*
     Updates the Home Score TextView with formatted string.
     updateHomeScore()
     returns: void
     */

    public void updateHomeScore() {
        TextView homeScoreText = findViewById(R.id.home_score);

        if (homeScore < 100) {
            String scoreFormat = String.format(Locale.getDefault(), "\t\t\t\t\t\t" + "%02d", homeScore);
            homeScoreText.setText(scoreFormat);

        } else {
            String scoreFormat = String.format(Locale.getDefault(), "%03d", homeScore);
            homeScoreText.setText(scoreFormat);
        }

    }

    /*
     Adds 1 point to Away Score and updates view
     addAwayScore()
     returns: void
     */

    public void addAwayScore(View view) {
        awayScore++;
        updateAwayScore();
    }

    /*
     Subtracts 1 point from Away Score and updates view
     minusAwayScore()
     returns: void
     */

    public void minusAwayScore(View view) {
        if (awayScore > 0) {
            awayScore--;
        }
        updateAwayScore();
    }

   /*
    Updates the Away Score TextView with formatted string.
    addAwayScore()
    returns: void
    */

    public void updateAwayScore() {
        TextView awayScoreText = findViewById(R.id.away_score);

        if (awayScore < 100) {
            String scoreFormat = String.format(Locale.getDefault(), "\t\t\t\t\t\t" + "%02d", awayScore);
            awayScoreText.setText(scoreFormat);

        } else {
            String scoreFormat = String.format(Locale.getDefault(), "%02d", awayScore);
            awayScoreText.setText(scoreFormat);
        }
    }

    /*
     Adds 1 to Period and updates view, stops adding at 4.
     addPeriod()
     returns: void
     */

    public void addPeriod(View view) {
        if (period < 4) {
            period++;
        }
        updatePeriod();
    }

    /*
     Subtracts 1 from Period and updates view, stops subtracting at 1.
     minusPeriod()
     returns: void
     */

    public void minusPeriod(View view) {
        if (period > 1) {
            period--;
        }
        updatePeriod();
    }

    /*
     Updates the Period TextView.
     updatePeriod()
     returns: void
     */

    public void updatePeriod() {
        TextView periodText = findViewById(R.id.period_number);
        String periodString = String.valueOf(period);
        periodText.setText(periodString);
    }

    /*
     Adds 1 to Home Foul and updates view.
     addHomeFoul()
     returns: void
     */

    public void addHomeFoul(View view) {
        homeFouls++;
        updateHomeFoul();
    }

    /*
     Subtracts 1 from Home Foul and updates view, stops subtracting at 0.
     minusHomeFoul()
     returns: void
     */

    public void minusHomeFoul(View view) {
        if (homeFouls > 0) {
            homeFouls--;
        }
        updateHomeFoul();
    }

    /*
     Updates the Home Foul TextView.
     updateHomeFoul()
     returns: void
     */

    public void updateHomeFoul() {
        TextView homeFoulText = findViewById(R.id.home_fouls);
        String homeFoulString = String.valueOf(homeFouls);
        homeFoulText.setText(homeFoulString);
    }

    /*
     Adds 1 to Away Foul and updates view.
     addAwayFoul()
     returns: void
     */

    public void addAwayFoul(View view) {
        awayFouls++;
        updateAwayFoul();
    }

    /*
     Subtracts 1 from Away Foul and updates view, stops subtracting at 0.
     minusAwayFoul()
     returns: void
     */

    public void minusAwayFoul(View view) {
        if (awayFouls > 0) {
            awayFouls--;
        }
        updateAwayFoul();
    }

    /*
     Updates the Away Foul TextView.
     updateAwayFoul()
     returns: void
     */

    public void updateAwayFoul() {
        TextView awayFoulText = findViewById(R.id.away_fouls);
        String awayFoulString = String.valueOf(awayFouls);
        awayFoulText.setText(awayFoulString);
    }

    /*
     Adds 1 to Home Timeout and updates view, stops adding at 6.
     addHomeTimeout()
     returns: void
     */

    public void addHomeTimeout(View view) {
        if (homeTimeouts < 6) {
            homeTimeouts++;
        }
        updateHomeTimeout();
    }

    /*
     Subtracts 1 from Home Timeout and updates view, stops subtracting at 0.
     minusHomeTimeout()
     returns: void
     */

    public void minusHomeTimeout(View view) {
        if (homeTimeouts > 0) {
            homeTimeouts--;
        }
        updateHomeTimeout();
    }

    /*
     Updates the Home Timeout TextView.
     updateHomeTimeout()
     returns: void
     */

    public void updateHomeTimeout() {
        TextView homeTimeoutText = findViewById(R.id.home_timeouts);
        String homeTimeoutString = String.valueOf(homeTimeouts);
        homeTimeoutText.setText(homeTimeoutString);
    }

    /*
     Adds 1 to Away Timeout and updates view, stops adding at 6.
     addAwayTimeout()
     returns: void
     */

    public void addAwayTimeout(View view) {
        if (awayTimeouts < 6) {
            awayTimeouts++;
        }
        updateAwayTimeout();
    }

    /*
     Subtracts 1 from Away Timeout and updates view, stops subtracting at 0.
     addAwayTimeout()
     returns: void
     */


    public void minusAwayTimeout(View view) {
        if (awayTimeouts > 0) {
            awayTimeouts--;
        }
        updateAwayTimeout();
    }

    /*
     Updates the Away Timeout TextView.
     updateAwayTimeout()
     returns: void
     */

    public void updateAwayTimeout() {
        TextView awayTimeoutText = findViewById(R.id.away_timeouts);
        String awayTimeoutString = String.valueOf(awayTimeouts);
        awayTimeoutText.setText(awayTimeoutString);
    }

    /*
     If in Portrait (Referee Mode), resets the Scores, Period, Fouls, and Timeouts to their starting values.
     If in Landscape (Free Mode), resets the Scores and Period to their starting values.
     resetScore()
     returns: void
     */

    public void resetScore(View view) {
        isPortrait = this.getResources().getBoolean(R.bool.is_portrait);
        homeScore = 0;
        homeFouls = 0;
        homeTimeouts = 6;
        awayScore = 0;
        awayFouls = 0;
        awayTimeouts = 6;
        period = 1;

        if (isPortrait) {
            updateAllPortrait();
        } else {
            updateAllLandscape();
        }


    }

    /*
     Updates the TextViews of all variables available in Portrait (Referee Mode)
     updateAllPortrait()
     returns: void
     */

    public void updateAllPortrait() {
        updateHomeScore();
        updateHomeFoul();
        updateHomeTimeout();
        updateAwayScore();
        updateAwayFoul();
        updateAwayTimeout();
        updatePeriod();
    }

    /*
     Updates the TextViews of all variables available in Landscape (Free Mode)
     updateAllLandscape()
     returns: void
     */

    public void updateAllLandscape() {
        updateHomeScore();
        updateAwayScore();
        updatePeriod();
    }

    /*
     Plays buzzer sound for 1 second (1000 milliseconds).
     playSound()
     returns: void
     */

    public void playSound(View view) {
        final MediaPlayer buzzer = MediaPlayer.create(this, R.raw.buzzer);
        CountDownTimer soundTimer = new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
                buzzer.start();
            }

            public void onFinish() {
                buzzer.stop();
                buzzer.release();
            }
        };
        soundTimer.start();

    }

    /*
     Locks and unlocks screen orientation.
     lockOrientation()
     returns: void
     */

    public void lockOrientation(View view) {
        if (!orientationLocked) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            orientationLocked = true;
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
            orientationLocked = false;
        }
    }


}

package ru.kalievmars.cooltimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    Time time;
    TextView textViewTimer;
    Button buttonStart;
    SeekBar seekBar;
    CountDownTimer timer;
    SharedPreferences sharedPreferences;

    boolean isStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar = findViewById(R.id.seekBar);
        textViewTimer = findViewById(R.id.textView_timer);
        buttonStart = findViewById(R.id.button_start);
        buttonStart.setOnClickListener(startTimerOnClickListener);

        time = new Time(0, 0);
        isStart = true;
        seekBar.setMax(600);

        seekBar.setOnSeekBarChangeListener(SeekBarChangeListener);
        setTextTime(seekBar.getProgress());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        setDefaultInterval(sharedPreferences);

//        Handler handler = new Handler();
//
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                textView.setText(String.valueOf(count));
//                count++;
//                handler.postDelayed(this, 1000);
//            }
//        };
//
//        handler.post(runnable);

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    View.OnClickListener startTimerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            timer = initCountDownTimer(time.getSeconds() * 1000L);
            if(isStart) {
                seekBar.setEnabled(false);
                buttonStart.setText(R.string.button_stop);
                isStart = false;
                timer.start();
            } else {
                turnOnStartTimer();
            }
        }
    };

    SeekBar.OnSeekBarChangeListener SeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            setTextTime(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private CountDownTimer initCountDownTimer(long inFutureTime) {
        CountDownTimer timer = new CountDownTimer(inFutureTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                setTextTime(millisUntilFinished);
                if(isStart) {
                    cancel();
                    onFinish();
                }
            }

            @Override
            public void onFinish() {
                if(sharedPreferences.getBoolean("enable_sound", true)) {
                    if(time.getSeconds() == 0) {
                        String tempSharedPref = sharedPreferences.getString("timer_melody", "bell");
                        if(tempSharedPref.equals("bell")) {
                            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bell);
                            mediaPlayer.start();
                        } else if(tempSharedPref.equals("bell2")) {
                            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bell2);
                            mediaPlayer.start();
                        } else if(tempSharedPref.equals("bell3")) {
                            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bell3);
                            mediaPlayer.start();
                        }

                    }
                }
                setTextTime(seekBar.getProgress());
                turnOnStartTimer();
            }
        };
        return timer;
    }


    void setTextTime(int seconds) {
        time.setTime(seconds);
        textViewTimer.setText(time.toString());
    }

    void setTextTime(long seconds) {
        time.setTime(seconds);
        textViewTimer.setText(time.toString());
    }

    void turnOnStartTimer() {
        seekBar.setEnabled(true);
        buttonStart.setText(R.string.button_start);
        isStart = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.timer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.action_about:
                Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
        }
        return true;
    }

    private void setDefaultInterval(SharedPreferences sharedPreferences) {
        try {
            String value = sharedPreferences.getString("default_interval", "30");
            setTextTime(Integer.parseInt(value));
            seekBar.setProgress(Integer.parseInt(value));
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("default_interval")) {
            setDefaultInterval(sharedPreferences);
        }
    }
}
package ru.kalievmars.cooltimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Time time;
    TextView textViewTimer;
    Button buttonStart;
    SeekBar seekBar;
    CountDownTimer timer;
    MediaPlayer mediaPlayer;

    boolean isStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar = findViewById(R.id.seekBar);
        textViewTimer = findViewById(R.id.textView_timer);
        buttonStart = findViewById(R.id.button_start);
        buttonStart.setOnClickListener(startTimerOnClickListener);
        mediaPlayer = MediaPlayer.create(this, R.raw.bell_sample);
        time = new Time(0, 0);
        isStart = true;
        seekBar.setMax(600);
        seekBar.setProgress(30);
        seekBar.setOnSeekBarChangeListener(SeekBarChangeListener);
        setTextTime(seekBar.getProgress());

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
                if(time.getSeconds() == 0) {
                    mediaPlayer.start();
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

}
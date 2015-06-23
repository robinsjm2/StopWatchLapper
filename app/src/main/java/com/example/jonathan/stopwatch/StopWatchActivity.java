/* Author:  Jonathan Robinson
 * StopWatchActivity
 */
package com.example.jonathan.stopwatch;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.*;
import java.util.concurrent.*;
import android.util.*;
import android.os.*;

public class StopWatchActivity extends Activity {

    private TextView txtDisplay1, txtDisplay2, txtLog;
    private long elapsedLapTimeInMillis, elapsedTotalTimeInMillis;
    private int laps = 0;
    private Timer t;
    private final int TIMER_INTERVAL_IN_MILLIS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);
        //init the timer

        txtDisplay1 = (TextView) findViewById(R.id.txtDisplay1);
        txtDisplay2 = (TextView) findViewById(R.id.txtDisplay2);
        txtLog = (TextView) findViewById(R.id.txtLapLog);

    }

    public void startWatch( View v ){
        t = new Timer();

        Log.i("Event", "Did call startWatch();");

        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                // update the UI
                mHandler.obtainMessage(1).sendToTarget();
                elapsedLapTimeInMillis += TIMER_INTERVAL_IN_MILLIS;
                elapsedTotalTimeInMillis += TIMER_INTERVAL_IN_MILLIS;
            }

        }, 0, TIMER_INTERVAL_IN_MILLIS);
    }

    public void updateUI(){

        // set the current lap timer to the elapsed time
        txtDisplay1.setText(this.getPrettyTime(this.elapsedLapTimeInMillis));
        txtDisplay2.setText(this.getPrettyTime(this.elapsedTotalTimeInMillis));
    }

    public void stopWatch( View v ) {
        // stop counting; stops the timer
        if( t != null ) {
            t.cancel();
            t = null;
        }
    }



    public void lap( View v ){

        String logText = txtLog.getText().toString();
        String sDisplay1 = txtDisplay1.getText().toString();

        // start new lap
        this.elapsedLapTimeInMillis = 0;
        laps++; // increment the laps counter

        // add the two timers to the log
        logText += "\n Lap " + laps + "- " + sDisplay1;

        txtLog.setText(logText);
    }

    public void resetWatch( View v ){
        // get the labels to reset
        String zeroLabel = "00:00.00";

        this.elapsedLapTimeInMillis = 0;
        this.elapsedTotalTimeInMillis = 0;


        txtDisplay1.setText( zeroLabel );
        txtDisplay2.setText( zeroLabel );
    }

    /**
    * getPrettyTime( long time ) - Converts number of millis into a formatted string ( MM:ss:millis )
    * */
    private String getPrettyTime( long time ){
        long min, sec, millis;

        // convert everything to common unit and determine the time parts
        min = TimeUnit.MILLISECONDS.toMinutes(time);
        sec = TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(min);
        millis = TimeUnit.MILLISECONDS.toMillis( time ) - TimeUnit.SECONDS.toMillis(sec) - TimeUnit.MINUTES.toMillis(min);
        return String.format("%d:%d.%d",
                min,
                sec,
                millis);
    }

    /* UI needs to be handled in the main thread */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            updateUI();
        }
    };

}

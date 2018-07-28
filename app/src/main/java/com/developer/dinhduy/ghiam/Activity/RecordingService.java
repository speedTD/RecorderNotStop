package com.developer.dinhduy.ghiam.Activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.developer.dinhduy.ghiam.Activity.Databases.DBSaveFile;
import com.developer.dinhduy.ghiam.Activity.Databases.MySharedPreferences;
import com.developer.dinhduy.ghiam.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class RecordingService extends Service {
    //All Field
    private MediaRecorder mediaRecorder=null;
    private DBSaveFile mDB;
    private String mFilePath=null;
    private long mStartMilisTime=0;  // Thời gian
    private long mTimeMilise=0;   //Thời Lượng file sẽ được ghi bằng nó
    private int mElapTimeSeconds=0;
    private static final SimpleDateFormat mTimerFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
    private onTimerChangedListener onTimerChangedListener=null;
    private String TAG="GHIAM365";

    private Timer mtimer;
    private TimerTask mTimeTask=null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public interface onTimerChangedListener {
        void onTimerChanged(int seconds);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDB=new DBSaveFile(getBaseContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "command ");
        StartRecording();
        return START_STICKY;
    }
    String Filename;
     //Method Start Recording........À Hấ
    private void StartRecording() {
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        Filename= date.getTime()+".mp3";
        mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFilePath+="/GHI ÂM/"+Filename;

        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(mFilePath);
        mediaRecorder.setAudioChannels(1);
        Toast.makeText(getApplicationContext(), "StartRecoding...", Toast.LENGTH_SHORT).show();
        mediaRecorder.setAudioSamplingRate(8000);
        mediaRecorder.setAudioEncodingBitRate(96000);
       //chất lượng của File Mp3 Thu được
        try {
            if (MySharedPreferences.getPrefHighQuality(this)==true) {
                mediaRecorder.setAudioSamplingRate(44100);
                mediaRecorder.setAudioEncodingBitRate(192000);
            }
            mediaRecorder.prepare();
            mediaRecorder.start();
            mStartMilisTime= System.currentTimeMillis();
            // Lưu Vào Database
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "PrePare Faild ! on RecordingService ");
        }

    }
    private void StopRecording(){
      mediaRecorder.stop();
      mediaRecorder.release();
      mTimeMilise=System.currentTimeMillis()-mStartMilisTime;
        Log.d(TAG, "StopRecording: Time "+mTimeMilise/1000);
        Log.d(TAG, "Insert Data in RecordingService Success !: ");
        //Stop Notification
        if(mTimeTask!=null){
          mTimeTask.cancel();
          mTimeTask=null;
        }
        Toast.makeText(this, "File Được Lưu Tại "+mFilePath, Toast.LENGTH_SHORT).show();
        mDB.InsertData(Filename,mFilePath,mTimeMilise);
        //GetZise File
        try{
            File file = new File(mFilePath);
            double length = file.length();
            length = length/(1000000);
            String x= String.format("%.2f",length);
            Toast.makeText(this, "File Path : " + file.getPath() + ", File size : " + x +" MB", Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            Toast.makeText(this, "File Not Found ", Toast.LENGTH_SHORT).show();
        }
        mediaRecorder=null;
    }
    private void TimeStart(){
        mtimer=new Timer();
        mTimeTask=new TimerTask() {
            @Override
            public void run() {
                mElapTimeSeconds++;
                if(onTimerChangedListener!=null){
                    onTimerChangedListener.onTimerChanged(mElapTimeSeconds);
                    NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(1,CreateNotification());
                }


            }
        };

        mtimer.scheduleAtFixedRate(mTimeTask,1000,1000);

    }

    private Notification CreateNotification() {
        NotificationCompat.Builder mbuilder=new NotificationCompat.Builder(getApplicationContext());
        mbuilder.setSmallIcon(R.drawable.play_icon);
        mbuilder.setContentTitle("Recording...");
        mbuilder.setContentText(mTimerFormat.format(mElapTimeSeconds * 1000));
        mbuilder.setContentIntent(PendingIntent.getActivities(getApplicationContext(),2018,
                new Intent[]{ new Intent(getApplicationContext(),MainActivity.class)},0));
        return  mbuilder.build() ;
    }

    @Override
    public void onDestroy() {
        if(mediaRecorder!=null){
            StopRecording();
        }
        super.onDestroy();

    }
}

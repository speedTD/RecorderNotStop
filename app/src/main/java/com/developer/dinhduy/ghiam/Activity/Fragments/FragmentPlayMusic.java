package com.developer.dinhduy.ghiam.Activity.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.developer.dinhduy.ghiam.Activity.File;
import com.developer.dinhduy.ghiam.R;
import com.melnykov.fab.FloatingActionButton;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class FragmentPlayMusic extends DialogFragment {

    private final String TAG_ITEM="ITEM_365";
    private File item;
    private Handler handler=new Handler();
    private MediaPlayer mMediaPlayer;
    private long Phut;
    private long giay;
    private TextView mTimeStart,mTimeEnd,mNameFie;
    private FloatingActionButton mBtn_Start_And_Stop;
    private SeekBar mseekBar;
    private Boolean isplay=true;

    public FragmentPlayMusic() {
        super();
    }
    public  FragmentPlayMusic NhanItem(File item){
        FragmentPlayMusic fragmentPlayMusic=new FragmentPlayMusic();
        Bundle bundle=new Bundle();
        bundle.putParcelable(TAG_ITEM,item);
        fragmentPlayMusic.setArguments(bundle);
        return fragmentPlayMusic;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item=getArguments().getParcelable(TAG_ITEM);
        long duaration=item.getTimeFile();
        Phut= TimeUnit.MILLISECONDS.toMinutes(duaration);
        giay=TimeUnit.MILLISECONDS.toSeconds(duaration)-TimeUnit.MINUTES.toSeconds(Phut);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog=super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view= getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_playmusic,null);
        mNameFie=(TextView) view.findViewById(R.id.id_dialog_File_name);
        mTimeStart=(TextView) view.findViewById(R.id.id_dialog_TimeStart);
        mTimeEnd=(TextView) view.findViewById(R.id.id_dialog_TimeEnd) ;
        mBtn_Start_And_Stop=(FloatingActionButton)  view.findViewById(R.id.id_dialog_btn_playstop);
        mseekBar=(SeekBar) view.findViewById(R.id.id_dialog_seekbar);
        mseekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mMediaPlayer!=null&&b){
                    //i nghĩa là điểm cuối
                    mMediaPlayer.seekTo(i);

                    long minutes = TimeUnit.MILLISECONDS.toMinutes(mMediaPlayer.getCurrentPosition());
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(mMediaPlayer.getCurrentPosition())
                            - TimeUnit.MINUTES.toSeconds(minutes);
                    mTimeStart.setText(String.format("%02d:%02d", minutes,seconds));
                }else if(mMediaPlayer==null&&b){
                    updateSeekBar();
                    ChaySeeBar(i);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(mMediaPlayer != null) {

                    handler.removeCallbacks(runnable);

                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mMediaPlayer != null) {
                    handler.removeCallbacks(runnable);
                    mMediaPlayer.seekTo(seekBar.getProgress());

                    long minutes = TimeUnit.MILLISECONDS.toMinutes(mMediaPlayer.getCurrentPosition());
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(mMediaPlayer.getCurrentPosition())
                            - TimeUnit.MINUTES.toSeconds(minutes);
                    mTimeStart.setText(String.format("%02d:%02d", minutes,seconds));
                    updateSeekBar();
                }
            }
        });
        mNameFie.setText(item.getNameFile());
        mTimeEnd.setText(String.format("%02d:%02d", Phut,giay));
        mBtn_Start_And_Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isplay=!isplay;
                onPlay(isplay);
            }
        });
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        builder.setView(view);
        return builder.create();

    }
    private void ChaySeeBar(int progress) {


        mMediaPlayer = new MediaPlayer();

        try {
            mMediaPlayer.setDataSource(item.getFilePath());
            mMediaPlayer.prepare();
            mseekBar.setMax(mMediaPlayer.getDuration());
            mMediaPlayer.seekTo(progress);

            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    StopMedia();
                }
            });

        } catch (IOException e) {
            Log.e("365", " chay Seebar failed");
        }


        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void onPlay(Boolean isplay) {
        if (!isplay) {
            //currently MediaPlayer is not playing audio
            if(mMediaPlayer == null) {
                PlayMedia(); //start from beginning
            } else {
                ResumMedia(); //resume the currently paused MediaPlayer
            }

        } else {
            //pause the MediaPlayer
            PauseMedia();
        }
    }

    //chạy nhạc
    private void PlayMedia(){
        mBtn_Start_And_Stop.setImageResource(R.drawable.stop_icon);

        mMediaPlayer=new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(item.getFilePath());
            mMediaPlayer.prepare();

            mseekBar.setMax(mMediaPlayer.getDuration());
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mMediaPlayer.start();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        updateSeekBar();

        //keep screen on while playing audio
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    private void updateSeekBar() {
        handler.postDelayed(runnable, 1000);
    }

    //Tiếp Tục chạy nhạc
    private void ResumMedia(){
        mBtn_Start_And_Stop.setImageResource(R.drawable.stop_icon);
        handler.removeCallbacks(runnable);
        mMediaPlayer.start();
        updateSeekBar();
    }
    //Dừng Nhạc
    private void StopMedia(){
        mBtn_Start_And_Stop.setImageResource(R.drawable.play_icon);
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer=null;
        isplay=!isplay;
        mseekBar.setProgress(mseekBar.getMax());
        mTimeStart.setText(mTimeEnd.getText());
        mseekBar.setProgress(mseekBar.getProgress());
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }
    //Tạm Dừng Nhạc
    private void PauseMedia(){
        mBtn_Start_And_Stop.setImageResource(R.drawable.play_icon);
        handler.removeCallbacks(runnable);
        mMediaPlayer.pause();
        updateSeekBar();
    }
    private Runnable runnable =new Runnable() {
        @Override
        public void run() {
            if(mMediaPlayer != null){

                int mCurrentPosition = mMediaPlayer.getCurrentPosition();
                mseekBar.setProgress(mCurrentPosition);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition)
                        - TimeUnit.MINUTES.toSeconds(minutes);
                mTimeStart.setText(String.format("%02d:%02d", minutes, seconds));

                updateSeekBar();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mMediaPlayer!=null){
            StopMedia();
        }
    }


}

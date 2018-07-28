package com.developer.dinhduy.ghiam.Activity.Fragments;



import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.developer.dinhduy.ghiam.Activity.RecordingService;
import com.developer.dinhduy.ghiam.R;
import com.melnykov.fab.FloatingActionButton;

import java.io.File;



/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRecorder extends Fragment  {
    private FloatingActionButton mButton_Xuly;
    private Chronometer mChronomenter_Time_Count;
    private View view;
    private Boolean ChangeState=false;
    public static String TAG="GHIAM365";
    private long timeWhenPaused=0;

    public FragmentRecorder() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Bắt đầu Ghi Âm
    //TODO: recording pause
    private void onRecord(boolean State){

        Intent mService = new Intent(getActivity(), RecordingService.class);

        if(State){
            mButton_Xuly.setImageResource(R.drawable.stop_icon);
            Toast.makeText(getContext(), "Recoding...", Toast.LENGTH_SHORT).show();


            //tạo thư mục chứa các File Ghi Âm
            File folder =new File(Environment.getExternalStorageDirectory()+"/GHI ÂM");
            if(!folder.exists()){
                folder.mkdir();
            }
            // bắt đầu chạy Tính Giờ
            mChronomenter_Time_Count.setBase(SystemClock.elapsedRealtime());
            mChronomenter_Time_Count.start();
            mChronomenter_Time_Count.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {

                }
            });
            timeWhenPaused = 0;
            //chạy Service đảm nhiệm việc Ghi âm
            // dừng chạy Service
            getActivity().startService(mService);
            Log.d(TAG, "Start Service in Fragment ");
            //Clear all Window
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        }else {

            Toast.makeText(getContext(), "Stoped...", Toast.LENGTH_SHORT).show();
            mButton_Xuly.setImageResource(R.drawable.play_icon);
            mChronomenter_Time_Count.setBase(SystemClock.elapsedRealtime());
            mChronomenter_Time_Count.stop();
            // dừng chạy Service

            getActivity().stopService(mService);
            Log.d(TAG, "Stop Service in Fragment ");
            //Clear all Window
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        }

    }
     boolean check=false;
    //Dừng Ghi âm
    //TODO: implement pause recording
  /*  private void onPauseRecord(boolean state) {
        if (state) {
            //pause recording
            mButon_Pause.setText("Tiếp tục");
            Toast.makeText(getContext(), "Tạm dừng", Toast.LENGTH_SHORT).show();
            timeWhenPaused = mChronomenter_Time_Count.getBase() - SystemClock.elapsedRealtime();
            mChronomenter_Time_Count.stop();

        } else {
            check=false;
            //resume recording
            mButon_Pause.setText("Dùng lại");
            mChronomenter_Time_Count.setBase(SystemClock.elapsedRealtime() + timeWhenPaused);
            mChronomenter_Time_Count.start();
        }
    }*/

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_fragment_recorder, container, false);
        //ánh xạ
        Anhxa();
        //Sự kiện Ghi âm Start Và Stop

        mButton_Xuly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeState=!ChangeState;
                onRecord(ChangeState);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void Anhxa() {
        mButton_Xuly=(FloatingActionButton) view.findViewById(R.id.id_btn_Play_Stop);
        mChronomenter_Time_Count=(Chronometer) view.findViewById(R.id.id_time_count_recorder);
    }
}

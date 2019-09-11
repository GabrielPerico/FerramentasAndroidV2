package com.edu.senac.ferramentas20;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AudioFragment extends Fragment {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    private boolean permissionRecord = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};

    boolean mStartPlaying = true;
    boolean mStartRecording = true;

    Button gravar, escutar;
    ImageView imgGravar, imgEscutar;
    ProgressBar pgb1, pgb2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_audio, container, false);

        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        fileName = getActivity().getExternalCacheDir().getAbsolutePath() + "audioSenac.3gp";

        imgGravar = view.findViewById(R.id.imgRecord);
        gravar = view.findViewById(R.id.gravar);
        pgb1 = view.findViewById(R.id.progressBar2);
        pgb1.setVisibility(View.GONE);

        gravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gravar();

            }
        });

        imgEscutar = view.findViewById(R.id.imgPlay);
        escutar = view.findViewById(R.id.escutar);
        pgb2 = view.findViewById(R.id.progressBar4);
        pgb2.setVisibility(View.GONE);

        escutar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                escutar();
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionRecord) {
            Toast.makeText(getActivity(), "Aceite as permissões", Toast.LENGTH_SHORT).show();
        }
    }

    public void escutar() {
        onPlay(mStartPlaying);
        if (mStartPlaying){
            escutar.setText("Parar audio");
            imgEscutar.setImageResource(R.drawable.pause_button);
            pgb1.setVisibility(View.VISIBLE);
        }else {
            escutar.setText("Escutar");
            imgEscutar.setImageResource(R.drawable.play_button);
            pgb1.setVisibility(View.GONE);
        }
        mStartPlaying = !mStartPlaying;
    }

    public void gravar() {
        onRecord(mStartRecording);
        if (mStartRecording){
            gravar.setText("Parar gravação");
            imgGravar.setImageResource(R.drawable.stop);
            pgb2.setVisibility(View.VISIBLE);
        }else{
            gravar.setText("Gravar");
            imgGravar.setImageResource(R.drawable.microphone);
            pgb2.setVisibility(View.GONE);
        }
        mStartRecording = !mStartRecording;
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (Exception e) {
            Log.e("audio", "erro => startPlaying");
        }
    }

    public void onRecord(boolean start){
        if(start){
            startRecording();
        }else{
            stopRecording();
        }
    }
    public void onPlay(boolean start){
        if (start){
            startPlaying();
        }else{
            stopPlaying();
        }
    }


    private void stopPlaying() {
        player.release();
        player = null;
    }

    private void startRecording() {
        recorder = new MediaRecorder();

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (Exception e) {
            Log.e("audio", "erro => startRecording");
        }
        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }
}

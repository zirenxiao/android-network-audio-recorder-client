package com.example.recorder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class AudioRecorder extends Thread {
    private boolean isRecording;
    private TcpClient tc;

    public AudioRecorder(TcpClient tc){
        this.isRecording = true;
        this.tc = tc;
    }

    @Override
    public void run() {
        AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.MIC, Settings.SAMPLE_HZ, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, Settings.getBuffSize());
        record.startRecording();
        byte audioData[] = new byte[Settings.getBuffSize()];
        while (isRecording) {
            int number = record.read(audioData, 0, Settings.getBuffSize());
            tc.send(audioData);
        }
        record.stop();
    }

    public void close(){
        isRecording = false;
    }
}

package com.example.recorder;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class AudioPlayer {
//    private boolean isPlaying;
//    public void start() throws IOException {
//        int dataSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
//        byte[] bytes = new byte[dataSize];
//        //定义输入流，将音频写入到AudioTrack类中，实现播放
//        DataInputStream dis = null;//new DataInputStream(new BufferedInputStream(new FileInputStream(audioFile)));
//        //实例AudioTrack
//
//        //开始播放
//        track.play();
//        //由于AudioTrack播放的是流，所以，我们需要一边播放一边读取
//        while (isPlaying && (dataSize = dis.read(bytes)) != -1) {
//            if (dataSize > 0) {
//                track.write(bytes, 0, dataSize);
//            }
//        }
//        //播放结束
//        track.flush();
//    }
    private AudioTrack track;
    private static AudioPlayer ap = null;

    public static AudioPlayer getInstance(){
        if (ap == null){
            ap = new AudioPlayer();
        }
        return ap;
    }

    public AudioPlayer(){
        track = new AudioTrack(AudioManager.STREAM_MUSIC, Settings.SAMPLE_HZ, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, Settings.getBuffSize(), AudioTrack.MODE_STREAM);
    }

    public void play(){
        track.play();
    }

    public void stop(){
        track.flush();
    }

    public void playData(byte[] bytes){
        track.write(bytes, 0, Settings.getBuffSize());
    }
}

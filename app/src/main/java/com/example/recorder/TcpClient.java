package com.example.recorder;

import android.media.AudioFormat;
import android.media.AudioRecord;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TcpClient extends Thread {
    private String TAG = "TcpClient";
    private String serverIP;
    private int serverPort;
    private PrintWriter pw;
    private InputStream is;
    private DataInputStream dis;
    private boolean isRun;
    private Socket socket = null;
    byte buff[];//  = new byte[4096];
    private String rcvMsg;
    private int rcvLen;

    public TcpClient(String ip , int port){
        this.serverIP = ip;
        this.serverPort = port;
        this.isRun = false;
        buff = new byte[Settings.getBuffSize()];
        MainActivity.addText("初始化中....");
    }

    public void close(){
        try {
            pw.close();
            is.close();
            dis.close();
            socket.close();
            isRun = false;
            AudioPlayer.getInstance().stop();
            MainActivity.addText("已断开连接！请重启APP！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String msg){
        pw.println(msg);
        pw.flush();
    }

    public void send(byte[] b){
        try {
            socket.getOutputStream().write(b);
            socket.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
            isRun = false;
        }
    }

    @Override
    public void run() {
        try {
            MainActivity.addText("连接中....");
            socket = new Socket(serverIP,serverPort);
            socket.setSoTimeout(Settings.TCP_TIMEOUT);
            pw = new PrintWriter(socket.getOutputStream(),true);
            is = socket.getInputStream();
            dis = new DataInputStream(is);
            isRun = true;
        } catch(ConnectException e){
            MainActivity.addText("连接超时！");
            return;
        } catch(IOException e) {
            e.printStackTrace();
            return;
        }
        MainActivity.addText("已连接！");
        AudioRecorder ar = new AudioRecorder(this);
        ar.start();
        AudioPlayer.getInstance().play();
        while (isRun){
            try {
                rcvLen = dis.read(buff);
//                rcvMsg = new String(buff,0,rcvLen,"utf-8");
////                Log.i(TAG, "run: 收到消息:"+ rcvMsg);
//                Intent intent =new Intent();
//                intent.setAction("tcpClientReceiver");
//                intent.putExtra("tcpClientReceiver",rcvMsg);
                AudioPlayer.getInstance().playData(buff);
            } catch (SocketTimeoutException e){
//                MainActivity.addText("读取超时！再试中....");
            } catch (IOException e) {
                e.printStackTrace();
                MainActivity.addText("连接已断开！");
                break;
            }
        }
        ar.close();
        this.close();
    }

    public boolean isRun() {
        return isRun;
    }

}
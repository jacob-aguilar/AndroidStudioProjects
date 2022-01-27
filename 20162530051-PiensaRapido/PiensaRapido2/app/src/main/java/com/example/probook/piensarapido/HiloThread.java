package com.example.probook.piensarapido;

import android.content.Context;
import android.media.MediaPlayer;

public class HiloThread extends Thread {

    private Context context;
    MediaPlayer mediaplay;
    private boolean stop;
    private boolean pause;
    private Object pauseLock;

    public HiloThread(Context context) {
        this.context = context;
        stop = false;
        pauseLock = new Object();
    }

    @Override
    public void run() {

        for(int i=1; i<=3; i++){
            if (stop){
                break;
            }
            if (mediaplay != null)
                mediaplay.release();
            mediaplay = MediaPlayer.create(context, R.raw.piano);
            mediaplay.start();
            try {
                Thread.sleep(6000);
            }catch (InterruptedException e){
                return;
            }

            synchronized (pauseLock){
                while (pause){
                    try {
                        pauseLock.wait();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void pausar(){
        synchronized (pauseLock){
            pause = true;
        }
    }

    public void renaudar(){
        synchronized (pauseLock){
            pause = false;
            pauseLock.notifyAll();
        }
    }

    public void terminar(){
        stop = true;
    }
}

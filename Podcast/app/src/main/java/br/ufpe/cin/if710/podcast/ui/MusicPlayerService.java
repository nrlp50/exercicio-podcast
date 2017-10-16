package br.ufpe.cin.if710.podcast.ui;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import br.ufpe.cin.if710.podcast.R;

/**
 * Created by nicola on 10/15/17.
 */

public class MusicPlayerService extends Service {

    private final String TAG = "MusicPlayerNoBindingService";

    private MediaPlayer mPlayer;
    private int mStartID;
    private int time;
    @Override
    public void onCreate() {
        super.onCreate();

        // configurar media player
        //Nine Inch Nails Ghosts I-IV is licensed under a Creative Commons Attribution Non-Commercial Share Alike license.

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        mPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(intent.getStringExtra("uri")));
        time = intent.getIntExtra("time", 0);
        if (null != mPlayer) {
            //nao deixa entrar em loop
            mPlayer.setLooping(false);

            // encerrar o service quando terminar a musica
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // encerra se foi iniciado com o mesmo ID
                    stopSelf(mStartID);
                }
            });
        }

        if (null != mPlayer) {
            // ID para o comando de start especifico
            mStartID = startId;

            /**/
            //se ja esta tocando...
            if (!mPlayer.isPlaying()) {
                //volta pra o inicio
                mPlayer.start();
            }

            mPlayer.seekTo(time);
        }
        // nao reinicia service automaticamente se for eliminado
        return START_NOT_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mPlayer) {
            mPlayer.stop();
            mPlayer.release();
        }

    }

    //nao eh possivel fazer binding com este service
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
        //return null;
    }


}

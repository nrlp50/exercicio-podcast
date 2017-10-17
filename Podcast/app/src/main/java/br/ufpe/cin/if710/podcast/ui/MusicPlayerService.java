package br.ufpe.cin.if710.podcast.ui;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;

import static java.security.AccessController.getContext;

/**
 * Created by nicola on 10/15/17.
 */

public class MusicPlayerService extends Service {

    private final String TAG = "MusicPlayerNoBindingService";
    public final static String PLAYER_FINISHED = "br.ufpe.cin.if710.podcast.services.action.FINISHED";

    private MediaPlayer mPlayer;
    private int mStartID;
    private String fileUri;
    @Override
    public void onCreate() {
        super.onCreate();

        // configurar media player
        //Nine Inch Nails Ghosts I-IV is licensed under a Creative Commons Attribution Non-Commercial Share Alike license.

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int time = 0;

        fileUri = intent.getStringExtra("uri");
        mPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(fileUri));
        String selection = PodcastProviderContract.EPISODE_FILE_URI + " = ?";
        String[] selectionArgs = {fileUri};
        Cursor cursor = getContentResolver().query(PodcastProviderContract.EPISODE_LIST_URI, null, selection, selectionArgs, null);


        if(cursor.moveToFirst())  time = Integer.parseInt(cursor.getString(cursor.getColumnIndex(PodcastProviderContract.EPISODE_TIME)));
        cursor.close();

        if (null != mPlayer) {
            //nao deixa entrar em loop
            mPlayer.setLooping(false);

            // encerrar o service quando terminar a musica
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // encerra se foi iniciado com o mesmo ID
                    String mSelectionClause = PodcastProviderContract.EPISODE_FILE_URI + " = ?";
                    String[] mSelectionArgs = {fileUri};


                    ContentValues cv = new ContentValues();
                    cv.put(PodcastProviderContract.EPISODE_STATE, "2");
                    cv.put(PodcastProviderContract.EPISODE_TIME, "0");

                    int mRowsUpdated = getContentResolver().update(
                            PodcastProviderContract.EPISODE_LIST_URI,   // the user dictionary content URI
                            cv,                       // the columns to update
                            mSelectionClause,         // the column to select on
                            mSelectionArgs            // the value to compare to
                    );

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

        if (null != mPlayer) {
            mPlayer.stop();
            int time = mPlayer.getCurrentPosition();

            String mSelectionClause = PodcastProviderContract.EPISODE_FILE_URI + " = ?";
            String[] mSelectionArgs = {fileUri};



            ContentValues cv = new ContentValues();
            cv.put(PodcastProviderContract.EPISODE_STATE, "2");
            cv.put(PodcastProviderContract.EPISODE_TIME, String.valueOf(time) );

            int mRowsUpdated = getContentResolver().update(
                    PodcastProviderContract.EPISODE_LIST_URI,   // the user dictionary content URI
                    cv,                       // the columns to update
                    mSelectionClause,         // the column to select on
                    mSelectionArgs            // the value to compare to
            );




            mPlayer.release();
        }


        super.onDestroy();
        Intent myIntent = new Intent(MusicPlayerService.PLAYER_FINISHED);
        myIntent.putExtra("uri",fileUri);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(myIntent);
    }

    //nao eh possivel fazer binding com este service
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
        //return null;
    }


}

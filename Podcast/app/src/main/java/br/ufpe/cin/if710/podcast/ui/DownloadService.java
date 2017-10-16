package br.ufpe.cin.if710.podcast.ui;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by nicola on 10/12/17.
 */

public class DownloadService extends IntentService{
    public static final String DOWNLOAD_COMPLETE = "br.ufpe.cin.if710.podcast.services.action.DOWNLOAD_COMPLETE";

    public DownloadService() {
        super("DownloadService");
    }


    @Override
    public void onHandleIntent(Intent i) {
        try {

            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            root.mkdirs();
            Uri uri = Uri.parse(i.getStringExtra("downloadLink"));
            File output = new File(root, uri.getLastPathSegment());
            if (output.exists()) {
                output.delete();
            }
            URL url = new URL(i.getStringExtra("downloadLink"));
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            FileOutputStream fos = new FileOutputStream(output.getPath());
            BufferedOutputStream out = new BufferedOutputStream(fos);
            try {
                InputStream in = c.getInputStream();
                byte[] buffer = new byte[8192];
                int len = 0;
                while ((len = in.read(buffer)) >= 0) {
                    out.write(buffer, 0, len);
                }
                out.flush();
            }
            finally {
                fos.getFD().sync();
                out.close();
                c.disconnect();
            }

            Intent myIntent = new Intent(DOWNLOAD_COMPLETE);
            myIntent.putExtra("uri", Uri.parse("file://"+output.getAbsolutePath()).toString());
            myIntent.putExtra("downloadLink", i.getStringExtra("downloadLink"));
            LocalBroadcastManager.getInstance(this).sendBroadcast(myIntent);

        } catch (IOException e2) {
            Log.e(getClass().getName(), "Exception durante download", e2);
        }
    }

}
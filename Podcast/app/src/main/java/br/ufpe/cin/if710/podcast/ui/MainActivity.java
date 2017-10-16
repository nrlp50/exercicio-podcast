package br.ufpe.cin.if710.podcast.ui;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.UserDictionary;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.db.PodcastProvider;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.domain.XmlFeedParser;
import br.ufpe.cin.if710.podcast.ui.adapter.CustomCursorAdapter;
import br.ufpe.cin.if710.podcast.ui.adapter.XmlFeedAdapter;

import android.util.Log;

public class MainActivity extends Activity {

    //ao fazer envio da resolucao, use este link no seu codigo!
    private final String RSS_FEED = "http://leopoldomt.com/if710/fronteirasdaciencia.xml";
    public final static String EXTRA_TITLE = "br.ufpe.cin.if710.podcast.TITLE";
    public final static String EXTRA_DATE = "br.ufpe.cin.if710.podcast.EXTRA_DATE";
    public final static String EXTRA_DESCRIPTION = "br.ufpe.cin.if710.podcast.EXTRA_DESCRIPTION";
    //TODO teste com outros links de podcast

    private ListView items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = (ListView) findViewById(R.id.items);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new DownloadXmlTask().execute(RSS_FEED);
    }

    @Override
    protected void onStop() {
        super.onStop();
        XmlFeedAdapter adapter = (XmlFeedAdapter) items.getAdapter();
        adapter.clear();
    }

    private class DownloadXmlTask extends AsyncTask<String, Void, List<ItemFeed>> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "iniciando...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected List<ItemFeed> doInBackground(String... params) {
            List<ItemFeed> itemList = new ArrayList<>();

            try {
                itemList = XmlFeedParser.parse(getRssFeed(params[0]));

                for(ItemFeed item : itemList){
                    ContentValues cv = new ContentValues();
                    cv.put(PodcastProviderContract.EPISODE_TITLE, item.getTitle());
                    cv.put(PodcastProviderContract.EPISODE_DATE, item.getPubDate());
                    cv.put(PodcastProviderContract.EPISODE_DESC, item.getDescription());
                    cv.put(PodcastProviderContract.EPISODE_DOWNLOAD_LINK, item.getDownloadLink());
                    cv.put(PodcastProviderContract.EPISODE_LINK, item.getLink());
                    cv.put(PodcastProviderContract.EPISODE_FILE_URI, item.getFileUri());

                    String selection = PodcastProviderContract.EPISODE_DOWNLOAD_LINK + " = ?";
                    String[] selectionArgs = {item.getDownloadLink()};


                    Cursor cursor = getContentResolver().query(PodcastProviderContract.EPISODE_LIST_URI, null,selection,selectionArgs,null);
                    if(cursor == null)
                        getContentResolver().insert(PodcastProviderContract.EPISODE_LIST_URI, cv);
                    else cursor.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return itemList;
        }

        @Override
        protected void onPostExecute(List<ItemFeed> feed) {
            Toast.makeText(getApplicationContext(), "terminando...", Toast.LENGTH_SHORT).show();

            new ReadFromDatabase().execute();
        }
    }


    private class ReadFromDatabase extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "iniciando...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Cursor doInBackground(Void... params) {

            Cursor cursor = null;

            ContentResolver cr = getContentResolver();
            String [] projection = {PodcastProviderContract._ID,
                    PodcastProviderContract.EPISODE_TITLE,
                    PodcastProviderContract.EPISODE_DATE,
                    PodcastProviderContract.EPISODE_DESC,
                    PodcastProviderContract.EPISODE_DOWNLOAD_LINK,
                    PodcastProviderContract.EPISODE_LINK,
                    PodcastProviderContract.EPISODE_FILE_URI
            };

            cursor = cr.query(PodcastProviderContract.EPISODE_LIST_URI, projection,null,null,null);

            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            Toast.makeText(getApplicationContext(), "Lendo do Database...", Toast.LENGTH_SHORT).show();

            ArrayList<ItemFeed> feed = new ArrayList<ItemFeed>();
            while(cursor.moveToNext()) {
                feed.add( new ItemFeed(cursor.getString(cursor.getColumnIndex(PodcastProviderContract.EPISODE_TITLE)),
                        cursor.getString(cursor.getColumnIndex(PodcastProviderContract.EPISODE_LINK)),
                        cursor.getString(cursor.getColumnIndex(PodcastProviderContract.EPISODE_DATE)),
                        cursor.getString(cursor.getColumnIndex(PodcastProviderContract.EPISODE_DESC)),
                        cursor.getString(cursor.getColumnIndex(PodcastProviderContract.EPISODE_DOWNLOAD_LINK)),
                        cursor.getString(cursor.getColumnIndex(PodcastProviderContract.EPISODE_FILE_URI)),
                        0
                ));
            }

            cursor.close();
            XmlFeedAdapter adapter = new XmlFeedAdapter(getApplicationContext(), R.layout.itemlista, feed);

            //atualizar o list view
            items.setAdapter(adapter);
            items.setTextFilterEnabled(true);

        }
    }
//

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter f = new IntentFilter(DownloadService.DOWNLOAD_COMPLETE);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onDownloadCompleteEvent, f);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onDownloadCompleteEvent);
    }


    private BroadcastReceiver onDownloadCompleteEvent=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent i) {

            Toast.makeText(ctxt, "Download finalizado!", Toast.LENGTH_LONG).show();

            String downloadLink = i.getStringExtra("downloadLink");


            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            root.mkdirs();

            Uri uri = Uri.parse(downloadLink);
            File output = new File(root, uri.getLastPathSegment());
            String fileUri = output.getAbsolutePath();

            String mSelectionClause = PodcastProviderContract.EPISODE_DOWNLOAD_LINK + " = ?";
            String[] mSelectionArgs = {downloadLink};


            ContentValues cv = new ContentValues();
            cv.put(PodcastProviderContract.EPISODE_FILE_URI, fileUri);


            int mRowsUpdated = getContentResolver().update(
                    PodcastProviderContract.EPISODE_LIST_URI,   // the user dictionary content URI
                    cv,                       // the columns to update
                    mSelectionClause,         // the column to select on
                    mSelectionArgs            // the value to compare to
            );

            Toast.makeText(ctxt,""+mRowsUpdated, Toast.LENGTH_LONG).show();
            new ReadFromDatabase().execute();
        }
    };

    //TODO Opcional - pesquise outros meios de obter arquivos da internet
    private String getRssFeed(String feed) throws IOException {
        InputStream in = null;
        String rssFeed = "";
        try {
            URL url = new URL(feed);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }
            byte[] response = out.toByteArray();
            rssFeed = new String(response, "UTF-8");
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return rssFeed;
    }
}

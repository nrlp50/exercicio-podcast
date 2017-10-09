package br.ufpe.cin.if710.podcast.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class PodcastProvider extends ContentProvider {

    private PodcastDBHelper dBHelper;
    public PodcastProvider() {
    }


    private boolean isDatabaseTable(Uri uri){
        return uri.getLastPathSegment().equals(PodcastProviderContract.DATABASE_TABLE);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase tdb = dBHelper.getWritableDatabase();
        long id = tdb.insert(PodcastProviderContract.DATABASE_TABLE, null, values);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public boolean onCreate() {
        dBHelper = PodcastDBHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor = null;
        if(isDatabaseTable(uri)) {
            cursor = dBHelper.getReadableDatabase().query(PodcastProviderContract.DATABASE_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        }else{
            throw new UnsupportedOperationException("Not yet implemented");
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

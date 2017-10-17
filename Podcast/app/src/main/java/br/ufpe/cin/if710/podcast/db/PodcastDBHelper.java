package br.ufpe.cin.if710.podcast.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PodcastDBHelper extends SQLiteOpenHelper {


    private static final int DB_VERSION = 1;

    private PodcastDBHelper(Context context) {
        super(context, PodcastProviderContract.DATABASE_NAME, null, DB_VERSION);
    }

    private static PodcastDBHelper db;

    public static PodcastDBHelper getInstance(Context c) {
        if (db==null) {
            db = new PodcastDBHelper(c.getApplicationContext());
        }
        return db;
    }

    public final static String[] columns = {
            PodcastProviderContract._ID, PodcastProviderContract.EPISODE_TITLE, PodcastProviderContract.EPISODE_DATE,
            PodcastProviderContract.EPISODE_LINK, PodcastProviderContract.EPISODE_DESC, PodcastProviderContract.EPISODE_DOWNLOAD_LINK,
            PodcastProviderContract.EPISODE_FILE_URI, PodcastProviderContract.EPISODE_STATE
    };

    final private static String CREATE_CMD =
            "CREATE TABLE "+PodcastProviderContract.DATABASE_TABLE+" (" + PodcastProviderContract._ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + PodcastProviderContract.EPISODE_TITLE + " TEXT NOT NULL, "
                    + PodcastProviderContract.EPISODE_DATE + " TEXT NOT NULL, "
                    + PodcastProviderContract.EPISODE_LINK + " TEXT NOT NULL, "
                    + PodcastProviderContract.EPISODE_DESC + " TEXT NOT NULL, "
                    + PodcastProviderContract.EPISODE_DOWNLOAD_LINK + " TEXT NOT NULL, "
                    + PodcastProviderContract.EPISODE_FILE_URI + " TEXT NOT NULL, "
                    + PodcastProviderContract.EPISODE_STATE + " TEXT NOT NULL,"
                    + PodcastProviderContract.EPISODE_TIME + " TEXT NOT NULL)";


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CMD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        throw new RuntimeException("inutilizado");
    }
}

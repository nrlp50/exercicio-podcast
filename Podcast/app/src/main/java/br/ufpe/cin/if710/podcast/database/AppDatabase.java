package br.ufpe.cin.if710.podcast.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import br.ufpe.cin.if710.podcast.dao.ItemFeedDao;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;

/**
 * Created by nicola on 12/13/17.
 */



//https://medium.com/@ajaysaini.official/building-database-with-room-persistence-library-ecf7d0b8f3e9
@Database(entities = {ItemFeed.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract ItemFeedDao getItemFeedDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "poscast")
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}


package br.ufpe.cin.if710.podcast.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.if710.podcast.domain.ItemFeed;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by nicola on 12/13/17.
 */

@Dao
public interface ItemFeedDao {

    @Query("SELECT * FROM podcasts")
    LiveData<List<ItemFeed> > getAll();

    @Query("SELECT * FROM podcasts where  downloadLink LIKE  :downloadLinkName")
    LiveData<ItemFeed> findItemFeedByDownloadLink(String downloadLinkName);

    @Query("SELECT * FROM podcasts where  fileUri LIKE  :file_uri")
    LiveData<ItemFeed> findItemFeedByUri(String file_uri);

    @Query("SELECT COUNT(*) from user")
    int countUsers();


    @Insert(onConflict = REPLACE)
    void insertAll(ItemFeed... items);

    @Insert(onConflict = REPLACE)
    void insert(ItemFeed item);

    @Update
    void update(ItemFeed item);


    @Delete
    void delete(ItemFeed user);


}

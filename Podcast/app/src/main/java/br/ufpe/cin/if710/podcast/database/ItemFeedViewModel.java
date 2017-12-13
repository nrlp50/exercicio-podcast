package br.ufpe.cin.if710.podcast.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import br.ufpe.cin.if710.podcast.dao.ItemFeedDao;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;

/**
 * Created by nicola on 12/13/17.
 */



//https://android.jlelse.eu/android-architecture-components-room-livedata-and-viewmodel-fca5da39e26b
public class ItemFeedViewModel extends AndroidViewModel {

    private final LiveData<List<ItemFeed>> itemFeedList;

    private ItemFeedDao itemFeedDao;

    public ItemFeedViewModel(Application application){
        super(application);

        itemFeedDao =  AppDatabase.getAppDatabase(this.getApplication()).getItemFeedDao();
        itemFeedList = itemFeedDao.getAll();
    }

    public LiveData<List<ItemFeed>> getItemFeedList() {
        return itemFeedList;
    }

    public void deleteItem(ItemFeed itemFeed) {
        new deleteAsyncTask(itemFeedDao).execute(itemFeed);
    }

    private static class deleteAsyncTask extends AsyncTask<ItemFeed, Void, Void> {


        private ItemFeedDao itemFeedDao;
        deleteAsyncTask(ItemFeedDao itemFeedDao) {
            this.itemFeedDao = itemFeedDao;
        }

        @Override
        protected Void doInBackground(final ItemFeed... params) {
            itemFeedDao.delete(params[0]);
            return null;
        }

    }

}

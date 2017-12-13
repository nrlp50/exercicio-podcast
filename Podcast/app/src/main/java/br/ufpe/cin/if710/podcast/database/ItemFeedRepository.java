package br.ufpe.cin.if710.podcast.database;

import android.arch.lifecycle.LiveData;

import javax.inject.Inject;

import br.ufpe.cin.if710.podcast.dao.ItemFeedDao;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;

/**
 * Created by nicola on 12/13/17.
 */

public class ItemFeedRepository {
    private final ItemFeedDao itemFeedDao;

    @Inject
    public ItemFeedRepository(ItemFeedDao itemFeedDao){
        this.itemFeedDao = itemFeedDao;
    }

    LiveData<ItemFeed> getListItem(String downloadLink){
        return itemFeedDao.findItemFeedByDownloadLink(downloadLink);
    }

    public void delete(ItemFeed itemFeed){
        itemFeedDao.delete(itemFeed);
    }

    public void insert(ItemFeed itemFeed){
        itemFeedDao.insert(itemFeed);
    }

    public void update(ItemFeed itemFeed){
        itemFeedDao.update(itemFeed);
    }
}

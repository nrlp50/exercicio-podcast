package br.ufpe.cin.if710.podcast;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ProviderTestCase2;

import android.test.mock.MockContentResolver;
import android.util.Log;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.ufpe.cin.if710.podcast.db.PodcastProvider;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;


/**
 * Created by nicola on 12/12/17.
 */

@RunWith(AndroidJUnit4.class)
public class ContentProviderTest extends ProviderTestCase2<PodcastProvider>{


    MockContentResolver mockContentResolver;



    public ContentProviderTest(){
        super(PodcastProvider.class, PodcastProviderContract.AUTHORITY);
    }

    @Before
    @Override
    public void setUp() throws Exception {
        //setContext(InstrumentationRegistry.getTargetContext());
        super.setUp();

    }

    @After
    @Override
    public void tearDown() throws  Exception{
        super.tearDown();
    }



    @Test
    public void insertItemFeedTest(){


        ContentValues cv = new ContentValues();
        cv.put(PodcastProviderContract.EPISODE_TITLE, "Tanto Faz");
        cv.put(PodcastProviderContract.EPISODE_DATE, "01/10/1010");
        cv.put(PodcastProviderContract.EPISODE_DESC, "sem ideia");
        cv.put(PodcastProviderContract.EPISODE_DOWNLOAD_LINK, "youtubiu.com");
        cv.put(PodcastProviderContract.EPISODE_LINK, "faltando");
        cv.put(PodcastProviderContract.EPISODE_FILE_URI, "123451000");
        cv.put(PodcastProviderContract.EPISODE_STATE, "0");
        cv.put(PodcastProviderContract.EPISODE_TIME, String.valueOf(1000));


        Uri uri = getMockContentResolver().insert(PodcastProviderContract.EPISODE_LIST_URI, cv);

        assertNotNull(uri);
    }


    @Test
    public void updateItemFeedTest(){

        ContentValues cv = new ContentValues();

        cv.put(PodcastProviderContract.EPISODE_STATE, "2");
        cv.put(PodcastProviderContract.EPISODE_FILE_URI, "1, 2 feijao com arroz");

        String mSelectionClause = PodcastProviderContract.EPISODE_DOWNLOAD_LINK + " = ?";
        String[] mSelectionArgs = {"youtubiu.com"};

        int mRowsUpdated = getMockContentResolver().update(
                PodcastProviderContract.EPISODE_LIST_URI,
                cv,
                mSelectionClause,
                mSelectionArgs
        );


        assertEquals(1,mRowsUpdated);
    }

}

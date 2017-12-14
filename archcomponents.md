*Architechture Components*
==========

Criamos uma brach chamada room para refatorarmos o código para o uso do RoomDatabase para persistir os dados do podcast. 
As classes que foram adicionadas pela refatoração foram AppDatabase, ItemFeedViewModel e ItemFeedDao.
As classes correspondentes ao Content Provider foram excluídas. 


O ItemFeed foi refatorado e possui um novo atributo id como chave primária.

```Java
@Entity(tableName = "podcasts")
	public class ItemFeed implements Serializable {


	    @PrimaryKey(autoGenerate = true)
	    public int id;


	    private final String title;
	    private final String link;
	    private final String pubDate;
	    private final String description;
	    private final String downloadLink;
	    private final String fileUri;
	    private final String state;
	    private final int time;


	    public ItemFeed(String title, String link, String pubDate, String description,
	                    String downloadLink, String fileUri, String state, int time) {


	        this.title = title;
	        this.link = link;
	        this.pubDate = pubDate;
	        this.description = description;
	        this.downloadLink = downloadLink;
	        this.fileUri = fileUri;
	        this.state = state;
	        this.time = time;
	    }


	    public String getTitle() {
	        return title;
	    }


	    public String getLink() { return link; }


	    public String getPubDate() {
	        return pubDate;
	    }


	    public String getDescription() {
	        return description;
	    }


	    public String getDownloadLink() {
	        return downloadLink;
	    }


	    public int getTime(){return time;}


	    public String getState(){return state; }
	    public String getFileUri(){ return fileUri;}
	    @Override
	    public String toString() {
	        return title;
	    }
	}

```



O ItemFeedDao é uma interface na qual possui as assinaturas dos métodos de acesso do banco de dados.

```Java
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

```


O AppDatabase é uma classe abstrata que extende de RoomDatabase. Ela cria e guarda a instância do RoomDatabase.

```Java
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
```

ViewModel para o LiveData

```Java
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

```



Nos baseamos em um blog no medium para fazer a refatoração do código.
 https://android.jlelse.eu/android-architecture-components-room-livedata-and-viewmodel-fca5da39e26b

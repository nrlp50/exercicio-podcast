package br.ufpe.cin.if710.podcast.ui.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.ui.DownloadService;
import br.ufpe.cin.if710.podcast.ui.EpisodeDetailActivity;
import br.ufpe.cin.if710.podcast.ui.MainActivity;
import br.ufpe.cin.if710.podcast.ui.MusicPlayerService;

public class XmlFeedAdapter extends ArrayAdapter<ItemFeed> {

    private int linkResource;
    private Context adapterContext;
    private List<ItemFeed> items;
    public XmlFeedAdapter(Context context, int resource, List<ItemFeed> objects) {
        super(context, resource, objects);
        linkResource = resource;
        adapterContext = context;
        items = objects;
    }

    /**
     * public abstract View getView (int position, View convertView, ViewGroup parent)
     * <p>
     * Added in API level 1
     * Get a View that displays the data at the specified position in the data set. You can either create a View manually or inflate it from an XML layout file. When the View is inflated, the parent View (GridView, ListView...) will apply default layout parameters unless you use inflate(int, android.view.ViewGroup, boolean) to specify a root view and to prevent attachment to the root.
     * <p>
     * Parameters
     * position	The position of the item within the adapter's data set of the item whose view we want.
     * convertView	The old view to reuse, if possible. Note: You should check that this view is non-null and of an appropriate type before using. If it is not possible to convert this view to display the correct data, this method can create a new view. Heterogeneous lists can specify their number of view types, so that this View is always of the right type (see getViewTypeCount() and getItemViewType(int)).
     * parent	The parent that this view will eventually be attached to
     * Returns
     * A View corresponding to the data at the specified position.
     */


	/*
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.itemlista, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.item_title);
		textView.setText(items.get(position).getTitle());
	    return rowView;
	}
	/**/

    //http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
    static class ViewHolder {
        TextView item_title;
        TextView item_date;
        Button button;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final  ViewHolder holder;
        final ItemFeed itemFeed = getItem(position);

        if (convertView == null) {
            convertView = View.inflate(getContext(), linkResource, null);
            holder = new ViewHolder();
            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            holder.item_date = (TextView) convertView.findViewById(R.id.item_date);
            holder.button = (Button) convertView.findViewById(R.id.item_action);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.item_title.setText(getItem(position).getTitle());
        holder.item_date.setText(getItem(position).getPubDate());


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent myIntent = new Intent(adapterContext, EpisodeDetailActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    myIntent.putExtra(MainActivity.EXTRA_TITLE, items.get(position).getTitle());
                    myIntent.putExtra(MainActivity.EXTRA_DESCRIPTION, items.get(position).getDescription());
                    myIntent.putExtra(MainActivity.EXTRA_DATE, items.get(position).getPubDate());

                    adapterContext.startActivity(myIntent);

                }catch(Exception e){
                    Log.e("error", e.getMessage());
                }
            }
        });

//        if( itemFeed.getFileUri() != ""){
//            Log.d("Teste FileUri:", itemFeed.getFileUri());
//            holder.button.setEnabled(true);
//            holder.button.setText("Tocar");
//        }else {
//            holder.button.setEnabled(true);
//            holder.button.setText("baixar");
//        }

        holder.button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View src){

                if(holder.button.getText().toString().equals("baixar")){
                    holder.button.setEnabled(false);
                    holder.button.setText("Baixando");
                    Intent myIntentService = new Intent(adapterContext, DownloadService.class);
                    myIntentService.putExtra("downloadLink", items.get(position).getDownloadLink() );
                    adapterContext.startService(myIntentService);
                }else if(holder.button.getText().toString().equals("Tocar")){
//                    holder.button.setText("Parar");
//                    holder.button.setEnabled(true);
//                    Intent myIntentService = new Intent(adapterContext, MusicPlayerService.class);
//                    myIntentService.putExtra("uri", Uri.parse(items.get(position).getDownloadLink() ));
//                    myIntentService.putExtra("time", itemFeed.getTime());
//                    adapterContext.startService(myIntentService);
                }else{
                    holder.button.setText("Tocar");
                    holder.button.setEnabled(true);
                }

            }
        });


        return convertView;
    }



}
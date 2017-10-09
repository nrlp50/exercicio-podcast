package br.ufpe.cin.if710.podcast.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import br.ufpe.cin.if710.podcast.R;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.ui.EpisodeDetailActivity;
import br.ufpe.cin.if710.podcast.ui.MainActivity;

/**
 * Created by nicola on 10/8/17.
 */

public class CustomCursorAdapter extends CursorAdapter{


    private Context adapterContext;
    private Cursor adapterCursor;
    public CustomCursorAdapter(Context context, Cursor cursor){
        super(context, cursor,0);
        adapterContext = context;
        adapterCursor = cursor;
    }


    private static class ViewHolder {
        int titleIndex;
        int dateIndex;
        TextView title;
        TextView date;
        int desc;

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){

        View view = LayoutInflater.from(context).inflate (R.layout.itemlista, null);
        ViewHolder holder = new ViewHolder();
        holder.title = (TextView) view.findViewById(R.id.item_title);
        holder.date = (TextView) view.findViewById(R.id.item_date);
        holder.titleIndex = cursor.getColumnIndexOrThrow(PodcastProviderContract.EPISODE_TITLE);
        holder.dateIndex = cursor.getColumnIndexOrThrow(PodcastProviderContract.EPISODE_DATE);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor){
        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.title.setText(cursor.getString(holder.titleIndex));
        holder.date.setText(cursor.getString(holder.dateIndex));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent myIntent = new Intent(adapterContext, EpisodeDetailActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    myIntent.putExtra(MainActivity.EXTRA_TITLE, cursor.getString(holder.titleIndex) );
                    myIntent.putExtra(MainActivity.EXTRA_DESCRIPTION, "ola");
                    myIntent.putExtra(MainActivity.EXTRA_DATE, cursor.getString(holder.dateIndex));

                    adapterContext.startActivity(myIntent);

                }catch(Exception e){
                    Log.e("error", e.getMessage());
                }
            }
        });

    }

}

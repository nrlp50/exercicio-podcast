package br.ufpe.cin.if710.podcast.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import br.ufpe.cin.if710.podcast.R;

public class EpisodeDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_detail);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_TITLE);
        TextView textTitle = findViewById(R.id.text_title);
        textTitle.setText(message);

        message = intent.getStringExtra(MainActivity.EXTRA_DESCRIPTION);
        TextView textDescription = findViewById(R.id.text_description);
        textDescription.setText(message);

        message = intent.getStringExtra(MainActivity.EXTRA_DATE);
        TextView textDate = findViewById(R.id.text_date);
        textDate.setText(message);

    }
}

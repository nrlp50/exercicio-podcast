package br.ufpe.cin.if710.podcast.domain;

import android.media.MediaPlayer;

import java.io.Serializable;

public class ItemFeed implements Serializable {
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
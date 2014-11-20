package com.merge.phillipjones.mergehifi;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

import java.io.IOException;

/**
 * Created by phillipjones on 11/18/14.
 */
public class Song {
    private String name;
    private String albumArtPath;
    private String songPath;
    private String artistName;

    public Song(String name, String artistName, String albumArt, String songPath) {
        this.name = name;
        this.artistName = artistName;
        this.albumArtPath = albumArt;
        this.songPath = songPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbumArtLocation() {
        return albumArtPath;
    }

    public Drawable getAlbumArtDrawable(AssetManager assets) {
        try {
            return Drawable.createFromStream(assets.open(albumArtPath), null);
        } catch (Exception e) {
            return null;
        }
    }

    public String getSongPath() {
        return songPath;
    }

    public String getArtistName() {
        return artistName;
    }
}

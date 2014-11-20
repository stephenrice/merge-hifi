package com.merge.phillipjones.mergehifi;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by phillipjones on 11/18/14.
 */
public class Person {
    private String fullName;
    private String profilePicturePath;
    private List<Song> playlist;
    private Set<Song> favoritedSongs;

    public Person(String fullName, String profilePicturePath) {
        this.fullName = fullName;
        this.profilePicturePath = profilePicturePath;
        playlist = new ArrayList<Song>();
        favoritedSongs = new HashSet<Song>();
    }

    public String getFullName() {
        return fullName;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public Drawable getProfilePictureDrawable(AssetManager assets) {
        try {
            return Drawable.createFromStream(assets.open(profilePicturePath), null);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Song> getPlaylist() {
        return playlist;
    }

    public void addSong(Song song) {
        playlist.add(song);
    }

    public void addToFavorites(Song song) {
        favoritedSongs.add(song);
    }

    public void removeFromFavorites(Song song) {
        if (favoritedSongs.contains(song)) {
            favoritedSongs.remove(song);
        }
    }
    public boolean doesFavoriteSong(Song song) {
        return favoritedSongs.contains(song);
    }




}

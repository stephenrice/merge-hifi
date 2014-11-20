package com.merge.hifi.merge;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Stephen on 11/16/14.
 */

public class Song implements Parcelable{

        private int id;
        private String title;
        private String artist;
        private String friend;

        public Song(int songID, String songTitle, String songArtist, String songFriend){
            id=songID;
            title=songTitle;
            artist=songArtist;
            friend = songFriend;
        }

    private String albumArtPath;
    private String songPath;
    private String artistName;
    private String name;

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



    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(title);
        out.writeString(artist);
        out.writeString(friend);
    }

    public static final Parcelable.Creator<Song> CREATOR
            = new Parcelable.Creator<Song>() {
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
    public Song(Parcel in){
        id=in.readInt();
        title=in.readString();
        artist=in.readString();
        friend = in.readString();
    }

        public long getID(){return id;}
        public String getTitle(){return title;}
        public String getArtist(){return artist;}
    public String getFriend(){
        return friend;
    }

    }


//    private String   title;
//    private String artist;
//    private boolean isLocked;
//    private Drawable albumArt;
//    private View.OnClickListener songOnClickListener = null;
//
//    public View.OnClickListener getSongOnClickListener() {
//        return songOnClickListener;
//    }
//
//    public void setSongOnClickListener(View.OnClickListener songOnClickListener) {
//        this.songOnClickListener = songOnClickListener;
//    }
//
//    public Song(String title, boolean isLocked, Drawable cardImage) {
//        this.title = title;
//        this.isLocked = isLocked;
//        this.albumArt = cardImage;
//    }
//
//    public Song(int i, Drawable aa) {
//        switch (i){
//            case 1: {
//                this.title = "My Song 5";
//                this.artist = "Haim";
//                break;
//            }
//            case 2: {
//                this.title = "Lungs";
//                this.artist = "CHVRCHES";
//            }
//            case 3: {
//                this.title = "Fake Empire";
//                this.artist = "The National";
//                break;
//            }
//            case 4: {
//                this.title = "Divinity";
//                this.artist = "Porter Robinson";
//                break;
//            }
//            case 5: {
//                this.title = "This Is the Last Time";
//                this.artist = "The National";
//                break;
//            }
//            default: {
//                this.title = "Title";
//                this.artist = "Artist";
//            }
//        }
//        this.albumArt = aa;
//        this.isLocked = false;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public boolean getLocked() {
//        return isLocked;
//    }
//
//    public Drawable getAlbumArt() {
//        return albumArt;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public void setLocked(boolean locked) {
//        this.isLocked = locked;
//    }
//
//    public void setAlbumArt(Drawable albumArt) {
//        this.albumArt = albumArt;
//    }



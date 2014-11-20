package com.merge.hifi.merge;

import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Stephen on 11/16/14.
 */
public class SongFrag extends Fragment {
    private int number;
    private String title;
    private String artist;
    private String friend;
    Drawable aa;
    Song song;

    private Drawable getAA(){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        switch (number){
            case 0: return getResources().getDrawable(R.drawable.haim);
            case 1: return getResources().getDrawable(R.drawable.chvrches);
            case 2: return getResources().getDrawable(R.drawable.national1);
            case 3: return getResources().getDrawable(R.drawable.porter);
            case 4: return getResources().getDrawable(R.drawable.national2);
            default: return getResources().getDrawable(R.drawable.haim);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        number = getArguments().getInt("pos");
        song = getArguments().getParcelable("song");
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.song_fragment_view, container, false);
        Log.i("SongFrag", "pos: " + Integer.toString(number));
        ImageView aav = (ImageView) rootView.findViewById(R.id.songFragAA);

        aav.setImageDrawable(getAA());
        return rootView;
    }




    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);

    }



}

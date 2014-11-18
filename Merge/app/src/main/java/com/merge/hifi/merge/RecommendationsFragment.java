package com.merge.hifi.merge;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Stephen on 11/16/14.
 */
public class RecommendationsFragment extends Fragment {
    private static final int NUM_PAGES = 5;
    private ArrayList<Song> songList;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;



    public RecommendationsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songList = ((MainActivity) getActivity()).getSongList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        // Instantiate a ViewPager and a PagerAdapter.
//
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_now_playing, container, false);
//        mPager = (ViewPager) getView().findViewById(R.id.pager);
//        mPagerAdapter = new SongPagerAdapter(getChildFragmentManager());
//        mPager.setAdapter(mPagerAdapter);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPager = (ViewPager) view.findViewById(R.id.pager);
        SongPagerAdapter spa = new SongPagerAdapter(getChildFragmentManager());
        mPager.setAdapter(spa);
    }

    private class SongPagerAdapter extends FragmentStatePagerAdapter {
        int kNumPages = 5;



        public SongPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putParcelable("song", songList.get(position));
            args.putInt("pos", position);
            SongFrag frag = new SongFrag();
            frag.setArguments(args);
            ((MainActivity) getActivity()).setSong(position);
            return frag;
        }

        @Override
        public int getCount() {
            return kNumPages;
        }
    }

}

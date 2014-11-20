package com.merge.hifi.merge;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

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
        return rootView;
    }

    public class backgroundBlurTask extends AsyncTask {


        private void applyBlur() {
            final View image = getActivity().findViewById(R.id.drawer_layout);
            image.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    image.getViewTreeObserver().removeOnPreDrawListener(this);
                    image.buildDrawingCache();

                    Bitmap bmp = image.getDrawingCache();
                    blur(bmp, image);
                    return true;
                }
            });
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        private void blur(Bitmap bkg, View view) {
            long startMs = System.currentTimeMillis();

            float radius = 25;

            Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()),
                    (int) (view.getMeasuredHeight()), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(overlay);

            canvas.translate(-view.getLeft(), -view.getTop());
            canvas.drawBitmap(bkg, 0, 0, null);

            RenderScript rs = RenderScript.create(getActivity());

            Allocation overlayAlloc = Allocation.createFromBitmap(
                    rs, overlay);

            ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(
                    rs, overlayAlloc.getElement());

            blur.setInput(overlayAlloc);

            blur.setRadius(radius);

            blur.forEach(overlayAlloc);

            overlayAlloc.copyTo(overlay);

            view.setBackground(new BitmapDrawable(
                    getResources(), overlay));

            rs.destroy();
//        statusText.setText(System.currentTimeMillis() - startMs + "ms");
        }

        @Override
        protected Object doInBackground(Object[] params) {
            applyBlur();
            return null;
        }
//        public void execute(){ doInBackground(null);}
    }

    private void setBackground(int number){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        View v = getActivity().findViewById(R.id.drawer_layout);
        Drawable d; //= getResources().getDrawable(R.drawable.chvrches_blur);
        switch (number){
            case 0: d = getResources().getDrawable(R.drawable.haim_blur); break;
            case 1: d = getResources().getDrawable(R.drawable.chvrches_blur); break;
            case 2: d = getResources().getDrawable(R.drawable.national_blur); break;
            case 3: d = getResources().getDrawable(R.drawable.porter_blur); break;
            case 4: d = getResources().getDrawable(R.drawable.national2_blur); break;
            default: d = getResources().getDrawable(R.drawable.haim_blur); break;
        }
        v.setBackground(d);
//        new backgroundBlurTask().execute();
    }

    private Drawable getFriendPic(Song song){
        if (song.getFriend().equals("Jack"))
            return getResources().getDrawable(R.drawable.jack);
        if (song.getFriend().equals("Stephen"))
            return getResources().getDrawable(R.drawable.stephen);
        if (song.getFriend().equals("Sasha"))
            return getResources().getDrawable(R.drawable.jenny);
        if (song.getFriend().equals("Jenny"))
            return getResources().getDrawable(R.drawable.jenny);
        if (song.getFriend().equals("Will"))
            return getResources().getDrawable(R.drawable.will);
        return null;
    }

    private void setTextViews(int pos, Song song){
        Log.i("setTestViews", "song: " + song.getTitle());
        TextView titlev = (TextView) getActivity().findViewById(R.id.recSongTitle);
        TextView artistv = (TextView) getActivity().findViewById(R.id.recArtist);
        TextView friendv = (TextView) getActivity().findViewById(R.id.recFriendName);
        ImageView friendPic = (ImageView) getActivity().findViewById(R.id.imageButton);
        AlphaAnimation fadeIn = new AlphaAnimation(1.0f , 0.0f ) ;
        AlphaAnimation fadeOut = new AlphaAnimation( 0.0f , 1.0f ) ;
//        titlev.startAnimation(fadeIn);
        titlev.startAnimation(fadeOut);
        artistv.startAnimation(fadeOut);
        friendv.startAnimation(fadeOut);
        friendPic.startAnimation(fadeOut);
        fadeOut.setDuration(600);
        fadeOut.setFillAfter(true);

        setBackground(pos);
        if (titlev != null) titlev.setText(song.getTitle());
        if (artistv != null) artistv.setText(song.getArtist());
        if (friendv != null) friendv.setText(song.getFriend());
        if (friendPic != null) friendPic.setImageDrawable(getFriendPic(song));

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPager = (ViewPager) view.findViewById(R.id.pager);
        SongPagerAdapter spa = new SongPagerAdapter(getChildFragmentManager());
        mPager.setAdapter(spa);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                setTextViews(i, songList.get(i));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        setTextViews(0, songList.get(0));

    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
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
            Log.i("RecFrag", "pos: " + Integer.toString(position));
            SongFrag frag = new SongFrag();
            frag.setArguments(args);
//            setTextViews(songList.get(position));
            ((MainActivity) getActivity()).setSong(position);
            return frag;
        }

        @Override
        public int getCount() {
            return kNumPages;
        }
    }

}

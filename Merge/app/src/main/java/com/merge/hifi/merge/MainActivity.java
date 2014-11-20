package com.merge.hifi.merge;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.merge.hifi.merge.MusicService.MusicBinder;

import java.util.ArrayList;


public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    ArrayList<Song> songList;



    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private RecommendationsFragment rf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songList = new ArrayList<Song>();
        populateSongList();
        rf = new RecommendationsFragment();
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    public ArrayList<Song> getSongList(){
        return songList;
    }

    private void populateSongList() {

        for (int i = 0; i < 5; i++) {
            Song s;
            switch (i) {
                case 0: {
                    s = new Song(1, "My Song 5", "Haim", "Jenny");
                    break;
                }
                case 1: {
                    s = new Song(2, "Lungs", "CHVRCHES", "Will");
                    break;
                }
                case 2: {
                    s = new Song(3, "Fake Empire", "The National", "Jack");
                    break;
                }
                case 3: {
                    s = new Song(4, "Sad Machine", "Porter Robinson", "Sasha");
                    break;
                }
                case 4: {
                    s = new Song(5, "This Is The Last Time", "The National", "Stephen");
                    break;
                }
                default: {
                    s = null;
                }
            }
            songList.add(i, s);
        }

    }

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(songList);
            musicBound = true;
            //musicSrv.playSong();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        Log.i("MainActivity", "int position: " + Integer.toString(position));
        if (position == 1){
            Intent intent = new Intent(this, NowPlaying.class);
            startActivity(intent);
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, rf)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.now_playing, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setSong(int id){
        if (musicSrv != null) musicSrv.playSong();
    }



    /**
     * A placeholder fragment containing a simple view.
     */
//    public static class PlaceholderFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        public PlaceholderFragment() {
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_now_playing, container, false);
//            return rootView;
//        }
//
//        @Override
//        public void onAttach(Activity activity) {
//            super.onAttach(activity);
//            ((MainActivity) activity).onSectionAttached(
//                    getArguments().getInt(ARG_SECTION_NUMBER));
//        }
    }

//}

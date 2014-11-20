package com.merge.hifi.merge;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import android.support.v7.app.ActionBarActivity;
//import android.support.v7.widget.Toolbar;


public class NowPlaying extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    // Sub Views
    private RecommendedUserView user1View;
    private RecommendedUserView user2View;
    private RecommendedUserView user3View;
    private LinearLayout recommendedUsersContainer;

    private ImageView backgroundView;

    // Music controls subview
    private ImageButton nextButton;
    private ImageButton playPauseButton;
    private ImageButton favoriteButton;

    private RelativeLayout musicControlsContainer;
    private LinearLayout innerMusicControlsContainer;

    // Music information subviews
    private RelativeLayout currentSongDetailsContainer;
    private ImageView playListOwnerImageView;
    private TextView playListOwnerTextView;

    private TextView currentSongTitleTextView;
    private TextView currentSongArtistTextView;

    // Instance Variables
    private int songIndex;
    private Song currentSong;

    private Person playListOwner;
    private Person randomPerson1;
    private Person randomPerson2;
    private Person randomPerson3;

    private Person userLoggedIn;

    private AssetManager assets;
    private boolean isPlaying;
    private MediaPlayer mp;
    private PeopleCollection peopleCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getResources();
        assets = getAssets();
        setContentView(R.layout.activity_now_playing);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarLayout);
//        setSupportActionBar(toolbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer2);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer2,
                (DrawerLayout) findViewById(R.id.drawer_layout2));
        mNavigationDrawerFragment.setIndex(1);

        // Set up media player.
        mp = new MediaPlayer();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                goToNextSong();

            }
        });

        user1View = (RecommendedUserView) findViewById(R.id.recommendedUser1);
        user2View = (RecommendedUserView) findViewById(R.id.recommendedUser2);
        user3View = (RecommendedUserView) findViewById(R.id.recommendedUser3);

        recommendedUsersContainer = (LinearLayout) findViewById(R.id.recommendedUsersContainer);

        // Bring music controls to the front.
        musicControlsContainer = (RelativeLayout) this.findViewById(R.id.musicControlsContainer);
        musicControlsContainer.bringToFront();



        final DragToShareView dragToShareView = (DragToShareView) findViewById(R.id.dragToShareView);
        dragToShareView.setVisibility(View.INVISIBLE);

        backgroundView = (ImageView) findViewById(R.id.albumArt);
        innerMusicControlsContainer = (LinearLayout)findViewById(R.id.musicControlsContainerActual);

        // Music Information views
        currentSongDetailsContainer = (RelativeLayout) findViewById(R.id.currentSongDetailsContainer);
        playListOwnerImageView = (ImageView) findViewById(R.id.playlistOwnerPicture);
        playListOwnerTextView = (TextView) findViewById(R.id.playlistOwnerName);

        currentSongTitleTextView = (TextView) findViewById(R.id.songTitle);
        currentSongArtistTextView = (TextView) findViewById(R.id.songArtist);

        //Music control views
        nextButton = (ImageButton) findViewById(R.id.nextSong);
        playPauseButton = (ImageButton) findViewById(R.id.playPause);
        favoriteButton = (ImageButton) findViewById(R.id.favoriteButton);




        // Set up Songs
        Song iWonder = new Song("I wonder", "Kanye West", "albumArt/graduation_cover.jpg", "songs/2.mp3");
        Song backstreet = new Song("Backstreet Freestyle", "Kendrick Lamar", "albumArt/good_kid_cover.jpg", "songs/1.mp3");
        Song allOfTheLights = new Song("All of the Lights", "Kanye West", "albumArt/twisted_fantasy_cover.jpg", "songs/4.mp3");
        Song richIsGangsta = new Song("Rich is Gangsta", "Rick Ross", "albumArt/master_mind_cover.jpeg", "songs/3.mp3");

        // Stephen's Songs (Ann)
        Song mySong5 = new Song("My Song 5", "Haim", "albumArt/haim.jpg", "songs/mySong5.mp3");
        Song lungs = new Song("Lungs", "CHVRCHES", "albumArt/chvrches.jpg", "songs/lungs.mp3");
        Song fakeEmpire = new Song("Fake Empire", "The National", "albumArt/national1.jpg", "songs/fakeEmpire.mp3");
        Song sadMachine = new Song("Sad Machine", "Porter Robinson", "albumArt/porter.jpg", "songs/sadMachine.mp3");
        Song lastTime = new Song("This Is The Last Time", "The National", "albumArt/national2.jpg", "songs/thisIsTheLastTime.mp3");

        // Random Songs
        Song virtue = new Song("Virtue", "Jesse Cook", "albumArt/freefall.jpg", "songs/virtue.mp3");
        Song waiting = new Song("Waiting", "Jesse Cook", "albumArt/waiting.jpg", "songs/waiting.mp3");
        Song tempest = new Song("Tempest", "Jesse Cook", "albumArt/tempest.jpg", "songs/tempest.mp3");

        // Add songs to people.
        Person richard = new Person("Richard", "people/person2.png");
        richard.addSong(lungs);
        richard.addSong(iWonder);
        //richard.addSong(backstreet);
        richard.addSong(allOfTheLights);
        //richard.addSong(richIsGangsta);

        Person ann = new Person("Ann", "people/person1.png");
        ann.addSong(mySong5);
        ann.addSong(lungs);
        ann.addSong(fakeEmpire);

        Person thomas = new Person("Thomas", "people/person4.png");
        thomas.addSong(sadMachine);
        thomas.addSong(lastTime);
        thomas.addSong(virtue);


        Person susan = new Person("Susan", "people/person7.png");
        susan.addSong(lastTime);
        susan.addSong(waiting);
        susan.addSong(allOfTheLights);

        Person sara = new Person("Sara", "people/person5.png");
        sara.addSong(tempest);
        sara.addSong(lungs);
        sara.addSong(fakeEmpire);


        // Add people to collection.
        peopleCollection = new PeopleCollection();
        peopleCollection.addPerson(richard);
        peopleCollection.addPerson(ann);
        peopleCollection.addPerson(new Person("Jeff", "people/person3.png"));
        peopleCollection.addPerson(thomas);
        peopleCollection.addPerson(sara);
        peopleCollection.addPerson(new Person("Bill", "people/person6.png"));
        peopleCollection.addPerson(susan);

        // Set up people
        userLoggedIn = peopleCollection.findByName("Jeff");

        // Default set up.
        songIndex = 0;
        currentSong = tempest;
        playListOwner = sara;

        Intent incomingIntent = getIntent();
        if (incomingIntent != null) {
            String songName = incomingIntent.getStringExtra("song");
            String playListOwnerName = incomingIntent.getStringExtra("person");
            if (songName != null && playListOwnerName != null) {
                playListOwner = peopleCollection.findByName(playListOwnerName);
                songIndex = playListOwner.findSongIndexByTitle(songName);
                currentSong = playListOwner.getPlaylist().get(songIndex);
            }
        }

        // Set the user we are currently listening to.
        peopleCollection.setCurrentlyListeningTo(playListOwner);

        // Set the user who is currently logged in.
        peopleCollection.setCurrentlyLoggedIn(userLoggedIn);

        // Randomize people to recommend to.
        refreshAllUsersToRecommendTo();

        // Update song information.
        refreshCurrentlyPlayingSongInformation();

        // Set up favorite button
        setUpFavoriteButton();

        backgroundView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                View dragView = (View)event.getLocalState();
                int dragAction = event.getAction();
                switch (dragAction) {
                    case DragEvent.ACTION_DRAG_ENDED:
                        dragView.setVisibility(View.VISIBLE);
                        dragToShareView.setVisibility(View.INVISIBLE);
                        return true;
                    case DragEvent.ACTION_DRAG_STARTED:
                        dragToShareView.setVisibility(View.VISIBLE);
                        return true;
                    case DragEvent.ACTION_DROP:
                        float droppedY = event.getY();
                        dragToShareView.setDisplayAlpha(DragToShareView.defaultAlpha);
                        Log.d("test", event.getClipData().getItemAt(0).getText().toString());
                        String currName = event.getClipData().getItemAt(0).getText().toString();
                        dragToShareView.invalidate();
                        // Show toast and animate out
                        Person sharedPerson = peopleCollection.getPersonFromName(currName);
                        peopleCollection.addRecommendedPerson(sharedPerson);
                        showSharedToast(sharedPerson.getFullName());
                        getNewPersonToShareWith(dragView);
                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        float currY = event.getY();
                        if (currY <= dragToShareView.getBottom()) {
                            dragToShareView.setDisplayAlpha(DragToShareView.hoverAlpha);
                            dragToShareView.invalidate();
                        } else {
                            dragToShareView.setDisplayAlpha(DragToShareView.defaultAlpha);
                            dragToShareView.invalidate();
                        }
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });


        // Logic for next button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextSong();
            }
        });

        // Logic for play pause button
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    mp.pause();
                    playPauseButton.setImageResource(R.drawable.ic_action_play);
                    isPlaying = false;
                } else {
                    mp.start();
                    playPauseButton.setImageResource(R.drawable.ic_action_pause);
                    isPlaying = true;
                }
            }
        });

        // Dummy logic for like button, no memory
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userLoggedIn.doesFavoriteSong(currentSong)) {
                    userLoggedIn.removeFromFavorites(currentSong);
                } else {
                    userLoggedIn.addToFavorites(currentSong);
                }
                setUpFavoriteButton();
            }
        });

        // Start song on app launch?
        startPlayingSong(currentSong);
        playPauseButton.setImageResource(R.drawable.ic_action_pause);

        backgroundView.setImageDrawable(currentSong.getAlbumArtDrawable(assets));
        backgroundView.invalidate();

        // Set music control background to main color in album art
        setMusicControlsAndInformationToBackgroundColor();
    }

    private void setUpFavoriteButton() {
        if (userLoggedIn.doesFavoriteSong(currentSong)) {
            Drawable favDrawable = getResources().getDrawable(R.drawable.ic_action_favorite);
            favDrawable.setColorFilter(0xffff0000, PorterDuff.Mode.MULTIPLY);
            favoriteButton.setImageDrawable(favDrawable);
            favoriteButton.invalidate();
        } else {
            Drawable favDrawable = getResources().getDrawable(R.drawable.ic_action_favorite);
            favDrawable.setColorFilter(0xffffffff, PorterDuff.Mode.MULTIPLY);
            favoriteButton.setImageDrawable(favDrawable);
            favoriteButton.invalidate();
        }
    }

    private void showSharedToast(String sharedName) {
        Toast toast = Toast.makeText(this, "You shared this song with " + sharedName, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void getNewPersonToShareWith(View view) {
        final RecommendedUserView v = (RecommendedUserView)view;
        Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.user_slide_down);
        final Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.user_slide_up);
        slideOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                refreshView(v);
                v.startAnimation(slideUp);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(slideOut);
        v.invalidate();
    }

    private void refreshView(RecommendedUserView v) {
        String name = v.getNameString();

        if (randomPerson1.getFullName().equals(name)) {
            randomPerson1 = peopleCollection.getRandomPerson();
            user1View.setProfilePicture(randomPerson1.getProfilePictureDrawable(assets));
            user1View.setNameString(randomPerson1.getFullName());
            user1View.invalidate();
        } else if ((randomPerson2.getFullName().equals(name))) {
            randomPerson2 = peopleCollection.getRandomPerson();
            user2View.setProfilePicture(randomPerson2.getProfilePictureDrawable(assets));
            user2View.setNameString(randomPerson2.getFullName());
            user2View.invalidate();
        } else {
            randomPerson3 = peopleCollection.getRandomPerson();
            user3View.setProfilePicture(randomPerson3.getProfilePictureDrawable(assets));
            user3View.setNameString(randomPerson3.getFullName());
            user3View.invalidate();
        }
    }

    private void goToNextSong() {
        Song nextSong = getNextSong();
        refreshCurrentlyPlayingSongInformation();
        // Animate recommended users and change them out
        Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.user_slide_down);
        final Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.user_slide_up);
        slideOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                refreshAllUsersToRecommendTo();
                recommendedUsersContainer.startAnimation(slideUp);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        recommendedUsersContainer.startAnimation(slideOut);

        backgroundView.setImageDrawable(nextSong.getAlbumArtDrawable(assets));
        backgroundView.invalidate();
        setMusicControlsAndInformationToBackgroundColor();
        startPlayingSong(nextSong);
        playPauseButton.setImageResource(R.drawable.ic_action_pause);
        currentSong = nextSong;
        setUpFavoriteButton();
        isPlaying = true;
    }

    private Song getNextSong() {
        if (songIndex + 1 >= playListOwner.getPlaylist().size()) {
            songIndex = 0;
        } else {
            songIndex ++;
        }
        return playListOwner.getPlaylist().get(songIndex);
    }

    private void startPlayingSong(Song currentSong) {
        mp.reset();
        try {
            AssetFileDescriptor afd = assets.openFd(currentSong.getSongPath());

            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            // do nothing
        }
    }

    private void refreshAllUsersToRecommendTo() {
        peopleCollection.resetRandoms();
        randomPerson1 = peopleCollection.getRandomPerson();
        user1View.setProfilePicture(randomPerson1.getProfilePictureDrawable(assets));
        user1View.setNameString(randomPerson1.getFullName());
        user1View.invalidate();

        randomPerson2 = peopleCollection.getRandomPerson();
        user2View.setProfilePicture(randomPerson2.getProfilePictureDrawable(assets));
        user2View.setNameString(randomPerson2.getFullName());
        user2View.invalidate();

        randomPerson3 = peopleCollection.getRandomPerson();
        user3View.setProfilePicture(randomPerson3.getProfilePictureDrawable(assets));
        user3View.setNameString(randomPerson3.getFullName());
        user3View.invalidate();
    }

    /*
     private RelativeLayout currentSongDetailsContainer;
    private ImageView playListOwnerImageView;
    private TextView playListOwnerTextView;

    private TextView currentSongTitleTextView;
    private TextView currentSongArtistTextView;
     */
    public void refreshCurrentlyPlayingSongInformation() {
        Song currentSong = playListOwner.getPlaylist().get(songIndex);

        playListOwnerImageView.setImageDrawable(playListOwner.getProfilePictureDrawable(getAssets()));
        playListOwnerImageView.invalidate();

        playListOwnerTextView.setText(playListOwner.getFullName());
        playListOwnerTextView.invalidate();

        currentSongTitleTextView.setText(currentSong.getName());
        currentSongTitleTextView.invalidate();

        currentSongArtistTextView.setText("by " + currentSong.getArtistName());
        currentSongArtistTextView.invalidate();
    }

    private void setMusicControlsAndInformationToBackgroundColor() {
        Bitmap b =  ((BitmapDrawable)backgroundView.getDrawable()).getBitmap();
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap onePixelBitMap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);

        int pixel = onePixelBitMap.getPixel(0,0);


        innerMusicControlsContainer.setBackgroundColor(pixel);
        //currentSongDetailsContainer.setBackgroundColor(pixel);
        //currentSongDetailsContainer.invalidate();
        innerMusicControlsContainer.invalidate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_now_playing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (position == 0 && NavigationDrawerFragment.isSet){
            //Intent intent = new Intent(this, MainActivity.class);
            //startActivity(intent);
        }
    }
}

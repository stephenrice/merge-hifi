package com.merge.hifi.merge;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
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

//import android.support.v7.app.ActionBarActivity;
//import android.support.v7.widget.Toolbar;


public class NowPlaying extends Activity{

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

        // Set music control background to main color in album art
        setMusicControlsAndInformationToBackgroundColor();


        // Set up Songs
        Song iWonder = new Song("I wonder", "Kanye West", "graduation_cover.jpg", "2.mp3");
        Song backstreet = new Song("Backstreet Freestyle", "Kendrick Lamar", "good_kid_cover.jpg", "1.mp3");
        Song allOfTheLights = new Song("All of the Lights", "Kanye West", "twisted_fantasy_cover.jpg", "4.mp3");
        Song richIsGangsta = new Song("Rich is Gangsta", "Rick Ross", "master_mind_cover.jpeg", "3.mp3");

        // Add songs to people.
        Person richard = new Person("Richard", "people/person2.png");
        richard.addSong(iWonder);
        richard.addSong(backstreet);
        richard.addSong(allOfTheLights);
        richard.addSong(richIsGangsta);

        // Add people to collection.
        peopleCollection = new PeopleCollection();
        peopleCollection.addPerson(richard);
        peopleCollection.addPerson(new Person("Ann", "people/person1.png"));
        peopleCollection.addPerson(new Person("Jeff", "people/person3.png"));
        peopleCollection.addPerson(new Person("Thomas", "people/person4.png"));
        peopleCollection.addPerson(new Person("Sara", "people/person5.png"));
        peopleCollection.addPerson(new Person("Bill", "people/person6.png"));
        peopleCollection.addPerson(new Person("Susan", "people/person7.png"));

        // Set up people
        userLoggedIn = peopleCollection.findByName("Jeff");

        songIndex = 0;
        currentSong = iWonder;
        playListOwner = richard;

        // Set the user we are currently listening to.
        peopleCollection.setCurrentlyListeningTo(richard);

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
                        dragToShareView.invalidate();
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

        peopleCollection.resetRandoms();
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
}

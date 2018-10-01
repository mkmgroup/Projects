package com.example.voices.view;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.example.voices.R;
import com.example.voices.controller.UserController;
import com.example.voices.model.Conversation;
import com.example.voices.model.OtherUser;
import com.example.voices.model.Post;
import com.example.voices.model.User;
import com.example.voices.util.ResultListener;
import com.example.voices.view.direct.ConversationActivity;
import com.example.voices.view.direct.DirectFragment;
import com.example.voices.view.direct.NewMessageFragment;
import com.example.voices.view.hashStories.HashStoriesActivity;
import com.example.voices.view.home.HomeTabsFragment;
import com.example.voices.view.home.hashtags.FollowHashtagFragment;
import com.example.voices.view.home.hashtags.HashtagsFragment;
import com.example.voices.view.home.people.FollowUserFragment;
import com.example.voices.view.home.people.HomeFragment;
import com.example.voices.view.home.people.PostFragment;
import com.example.voices.view.login.LoginActivity;
import com.example.voices.view.profile.ProfileFragment;
import com.example.voices.view.search.SearchFragment;
import com.example.voices.view.search.SearchHashtagsFragment;
import com.example.voices.view.search.SearchPeopleFragment;
import com.example.voices.view.stories.StoriesActivity;
import com.github.piasy.rxandroidaudio.AudioRecorder;
import com.github.piasy.rxandroidaudio.StreamAudioRecorder;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements HomeFragment.HomeOnFragmentInteractionListener, SearchFragment.SearchOnFragmentInteractionListener, DirectFragment.DirecOnFragmentInteractionListener, ProfileFragment.ProfileOnFragmentInteractionListener, RecordFragment.RecordOnFragmentInteractionListener
,SearchHashtagsFragment.SearchHashtagsOnFragmentInteractionListener, SearchPeopleFragment.SearchPeopleOnFragmentInteractionListener, PostFragment.PostFragmentOnFragmentInteractionListener
    , HomeTabsFragment.HomeTabsOnFragmentInteractionListener, HashtagsFragment.HashtagsOnFragmentInteractionListener
    , FollowHashtagFragment.FollowHashOnFragmentInteractionListener, FollowUserFragment.FollowUserOnFragmentInteractionListener, NewMessageFragment.NewMessageOnFragmentInteractionListener

{
    float dX;
    float dY;
    int lastAction;
    float y0;
    StreamAudioRecorder streamAudioRecorder;
    AudioRecorder mAudioRecorder;
    Boolean recording = false;
    BottomNavigationViewEx navigation;
    FloatingActionButton floatingActionButton;
    CoordinatorLayout coordinatorLayout;
    UserController userController;
    public User user;
    FirebaseUser userAuth;
    List<Conversation> conversations;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment nuevoFragment = null;
            FragmentManager unFragmentManager = getSupportFragmentManager();
            FragmentTransaction unFragmentTransaction = unFragmentManager.beginTransaction();
            String fragmentName = null;
            boolean check = false;


            switch (item.getItemId()) {
                case R.id.navigation_home:
                    check = true;
                    nuevoFragment = new HomeTabsFragment();
                    fragmentName = "HOME";
                    break;
                case R.id.navigation_seach:
                    check = true;
                    nuevoFragment = new SearchFragment();
                    fragmentName = "SEARCH";
                    break;
                case R.id.navigation_direct:
                    check = true;
                    nuevoFragment = new DirectFragment();
                    fragmentName = "DIRECT";
                    break;
                case R.id.navigation_profile:
                    check = true;
                    nuevoFragment = ProfileFragment.getProfileFragment(userAuth.getUid());
                    fragmentName = "PROFILE";
                    break;
            }

            if (check){
                unFragmentTransaction.replace(R.id.main_activity_fragment_container, nuevoFragment, fragmentName);
                unFragmentTransaction.commitAllowingStateLoss();
                return true;
            } else {
                return false;
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = new User();
        conversations = new ArrayList<>();
        File voicesDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + "Voices/");
        if(!voicesDirectory.exists()){
            voicesDirectory.mkdirs();
        }
        userController = new UserController();
        FirebaseApp.initializeApp(this);

        userAuth = FirebaseAuth.getInstance().getCurrentUser();
        if (userAuth != null) {
            // User is signed in
            userController.getUser(userAuth.getUid(), new ResultListener<User>() {
                @Override
                public void finish(User resultado) {
                    user = resultado;
                    setHomeFraagment();
                }
            });
        } else {
            // No user is signed in
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }


        mAudioRecorder = AudioRecorder.getInstance();
        streamAudioRecorder = StreamAudioRecorder.getInstance();
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        coordinatorLayout = findViewById(R.id.main_activity_coordinator);

        final Activity activity = this;
        navigation = (BottomNavigationViewEx) findViewById(R.id.navigation);
        navigation.enableShiftingMode(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        floatingActionButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(activity,
                        new PermissionsResultAction() {
                            @Override
                            public void onGranted() {

                                if (!recording){
                                    Fragment nuevoFragment = new RecordFragment();
                                    FragmentManager unFragmentManager = getSupportFragmentManager();
                                    FragmentTransaction unFragmentTransaction = unFragmentManager.beginTransaction();
                                    unFragmentTransaction.replace(R.id.main_activity_fragment_container, nuevoFragment, "RECORD");
                                    unFragmentTransaction.commit();

                                    RecordFragment recordFragment = (RecordFragment) getSupportFragmentManager().findFragmentByTag("RECORD");

                                   /* if (recordFragment != null) {
                                        recordFragment.onStartRecording();
                                    }*/
                                    recording = true;
                                    navigation.setVisibility(View.INVISIBLE);
                                }
                                else {
                                    recording = false;
                                    RecordFragment recordFragment = (RecordFragment) getSupportFragmentManager().findFragmentByTag("RECORD");

                                    if (recordFragment != null) {
                                        String filePath = recordFragment.onStoptRecording();
                                        Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                                        Bundle unBundle = new Bundle();
                                        unBundle.putString(EditActivity.FILEPATH, filePath);
                                        intent.putExtras(unBundle);

                                        startActivityForResult(intent, 7);

                                    }
                                }
                            }

                            @Override
                            public void onDenied(String permission) {
                                // Notify the user that you need all of the permissions
                            }
                        });



                return true;
            }
        });






    }

    @Override
    protected void onPause() {
        super.onPause();
/*
        if (user.getFollowingIds() != null){
            setHomeFraagment();
            navigation.setCurrentItem(0);
        }*/


    }

    @Override
    protected void onResume() {
        super.onResume();
        navigation.setVisibility(View.VISIBLE);
    }

    @Override
    public void homeOnFragmentInteraction(List<Post> posts, String userId, int position) {
        Gson gson = new Gson();
        String postsString = gson.toJson(posts);
        String userSring = gson.toJson(user);


        Intent intent = new Intent(getApplicationContext(), StoriesActivity.class);
        Bundle unBundle = new Bundle();
        unBundle.putString(StoriesActivity.USERIDCLIKED, userId);
        unBundle.putString(StoriesActivity.POSTSCLIKED, postsString);
        unBundle.putString(StoriesActivity.USER, userSring);
        unBundle.putString(StoriesActivity.POSITION, Integer.toString(position));

        intent.putExtras(unBundle);

        startActivity(intent);

    }

    @Override
    public void direcOnFragmentInteraction(Uri uri) {

    }

    @Override
    public void onNewMessageButtonClick() {
        Fragment nuevoFragment = new NewMessageFragment();
        FragmentManager unFragmentManager = getSupportFragmentManager();
        FragmentTransaction unFragmentTransaction = unFragmentManager.beginTransaction();
        unFragmentTransaction.replace(R.id.main_activity_fragment_container, nuevoFragment, "HOME");
        /*unFragmentTransaction.commit();*/
        unFragmentTransaction.commitAllowingStateLoss();
        unFragmentTransaction.addToBackStack(null);
    }

    @Override
    public void updateConvList(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    @Override
    public void onConvClick(Conversation conversation) {
        Intent intent = new Intent(getApplicationContext(), ConversationActivity.class);
        Bundle unBundle = new Bundle();
        String otherUserID = "";
        for (Map.Entry<String, Boolean> user : conversation.getUsers().entrySet()) {
           if (user.getKey() != FirebaseAuth.getInstance().getUid()){
               otherUserID = user.getKey();
               break;
           }
        }

        unBundle.putString(ConversationActivity.OTHERUSERID, otherUserID);

        unBundle.putString(ConversationActivity.CONVERSATIONID, conversation.getId());

        intent.putExtras(unBundle);
        startActivity(intent);
    }

    @Override
    public void recordOnFragmentInteraction(Uri uri) {

    }

    @Override
    public void searchOnFragmentInteraction(Uri uri) {

    }

    @Override
    public void profileOnFragmentInteraction(Uri uri) {

    }
    public void setHomeFraagment(){

        Fragment nuevoFragment = new HomeTabsFragment();
        FragmentManager unFragmentManager = getSupportFragmentManager();
        FragmentTransaction unFragmentTransaction = unFragmentManager.beginTransaction();
        unFragmentTransaction.replace(R.id.main_activity_fragment_container, nuevoFragment, "HOME");
        /*unFragmentTransaction.commit();*/
        unFragmentTransaction.commitAllowingStateLoss();
    }
    public void sendAssetsToFiles(){
        String testApp = Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + "Voices" + File.separator +"aplausos.m4a";
        File mOutputFile = new File(testApp);

        if (!mOutputFile.exists()){
            copyAssets();
        }
    }

    private void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        for(String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);

                String outDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Voices/" ;

                File outFile = new File(outDir, filename);

                out = new FileOutputStream(outFile);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
        }
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 7){
            setHomeFraagment();
            navigation.setCurrentItem(0);
            if (resultCode == 123) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Tu audio se subio con exito!", Snackbar.LENGTH_LONG);


// Changing message text color
                snackbar.setActionTextColor(Color.RED);

// Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                sbView.setBackgroundColor(Color.parseColor("#388E3C"));
                snackbar.show();
            }
        }
    }

    @Override
    public void searchPeopleOnFragmentInteractionPersonClicked(OtherUser otherUser) {
        Fragment nuevoFragment = ProfileFragment.getProfileFragment((String) otherUser.getId());
        FragmentManager unFragmentManager = getSupportFragmentManager();
        FragmentTransaction unFragmentTransaction = unFragmentManager.beginTransaction();
        unFragmentTransaction.replace(R.id.main_activity_fragment_container, nuevoFragment, "PROFILE" + otherUser.getId()).addToBackStack(null);
        unFragmentTransaction.commit();
    }

    @Override
    public void searchPeopleOnFragmentInteractionFollowClick(OtherUser otherUser) {
        user.getFollowingIds().put((String) user.getId(), true);

        userController.followUser((String) user.getId(), user.getId(), new ResultListener<Boolean>() {
            @Override
            public void finish(Boolean resultado) {
                if (resultado){


                }
            }
        });    }

    @Override
    public void searchPeopleOnFragmentInteractionUnfollowClick(OtherUser otherUser) {

    }

    @Override
    public void SearchHashtagsOnFragmentInteraction(Uri uri) {

    }

    @Override
    public void postFragmentOnFragmentInteraction(Post post) {

    }


    @Override
    public void homeTabsOnFragmentInteraction(Uri uri) {

    }

    @Override
    public void hashtagsOnFragmentInteraction(String hash) {
        Gson gson = new Gson();
        String userSring = gson.toJson(user);
        Intent intent = new Intent(getApplicationContext(), HashStoriesActivity.class);
        Bundle unBundle = new Bundle();
        unBundle.putString(HashStoriesActivity.HASHTAG, hash);
        unBundle.putString(HashStoriesActivity.USER, userSring);
        intent.putExtras(unBundle);

        startActivity(intent);
    }

    @Override
    public void followHashOnFragmentInteraction(Uri uri) {

    }

    @Override
    public void followUserOnFragmentInteraction(List<Post> posts, String userId) {

    }

    @Override
    public void newMessageOnFragmentInteraction(OtherUser otherUser) {
        Conversation theConversation = null;
        for (Conversation conversation : conversations) {
            if (conversation.getUsers().containsKey(otherUser.getTheId())){
                theConversation = conversation;
                break;
            }
        }
        Intent intent = new Intent(getApplicationContext(), ConversationActivity.class);
        Bundle unBundle = new Bundle();
        unBundle.putString(ConversationActivity.OTHERUSERID, otherUser.getTheId());
        if (theConversation != null){
            unBundle.putString(ConversationActivity.CONVERSATIONID, theConversation.getId());
        }else {
            unBundle.putString(ConversationActivity.CONVERSATIONID, "0");
        }
        intent.putExtras(unBundle);
        startActivity(intent);
    }
}

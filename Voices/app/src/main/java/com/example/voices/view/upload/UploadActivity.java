package com.example.voices.view.upload;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.voices.R;
import com.example.voices.controller.HashtagContoller;
import com.example.voices.controller.PostController;
import com.example.voices.controller.UserController;
import com.example.voices.model.Hashtag;
import com.example.voices.model.MyGlideEngine;
import com.example.voices.model.OtherUser;
import com.example.voices.model.Post;
import com.example.voices.model.User;
import com.example.voices.util.ResultListener;
import com.example.voices.view.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.pchmn.materialchips.model.Chip;
import com.zhihu.matisse.Matisse;

import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gun0912.tedbottompicker.TedBottomPicker;

public class UploadActivity extends AppCompatActivity implements UploadDetailsFragment.UploadDetailsFragmentInteractionListener,
        SelectHashFragment.SelectHashFragmentInteractionListener,
        SelectUsersFragment.SelectUsersFragmentInteractionListener, SelectLocationFragment.SelectLocationsFragmentInteractionListener
{

    public static final String FILEPATH = "filePath";
    public static final String DURATION = "duration";
    public static final String DURLONG = "durLong";
    private static final int REQUEST_CODE_CHOOSE = 23;


    User user;
    List<Chip> hashesSelected;
    List<OtherUser> usersSelected;
    List<Chip> locationSelected;
    String filePath;
    String dur;
    Long durLong;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        UserController userController = new UserController();
        userController.getUser(FirebaseAuth.getInstance().getUid(), resultado -> {
            user = resultado;
        });

        //Obtener el Intent
        Intent unIntent = getIntent();

        //Obtener el Bundle
        Bundle unBundle = unIntent.getExtras();


        //Obtener el mensaje

        Gson gson = new Gson();

        durLong = gson.fromJson(unBundle.getString(DURLONG), Long.class);


        filePath = unBundle.getString(FILEPATH);
        dur = unBundle.getString(DURATION);
        hashesSelected = new ArrayList<>();
        usersSelected = new ArrayList<>();
        locationSelected = new ArrayList<>();
        Fragment nuevoFragment = new UploadDetailsFragment();
        FragmentManager unFragmentManager = getSupportFragmentManager();
        FragmentTransaction unFragmentTransaction = unFragmentManager.beginTransaction();
        unFragmentTransaction.replace(R.id.upload_activity_fragment_container, nuevoFragment, "DETAILS");
        unFragmentTransaction.commit();


    }

    @Override
    public void uploadDetailsFragmentUpload() {
        Post post = new Post();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        post.setDate(Calendar.getInstance().getTime());
        Map<String, Long> hash = new HashMap<>();

        long cur = System.currentTimeMillis();

        for (Chip chip : hashesSelected) {
            hash.put(chip.getLabel(), cur);
        }
        post.setDurLong(durLong);
        post.setDuration(dur);
        post.setLikes(new HashMap<>());
        post.setHashtags(hash);
        Map<String, Long> userMap = new HashMap<>();



        userMap.put(user.getUid(), cur);
        post.setUser(userMap);
        post.setUserId(user.getUid());
        Map<String, Long> tags = new HashMap<>();
        for (OtherUser chip : usersSelected) {
            tags.put(chip.getInfo(), cur);
        }
        post.setUsersTaggedIds(tags);



        post.setImageURl("https://firebasestorage.googleapis.com/v0/b/voices-ad267.appspot.com/o/dj.png?alt=media&token=dcefe0dd-329d-40dc-b86a-5de04135ddae");

        PostController postController = new PostController();

        UserController userController = new UserController();

        userController.setLastActive(FirebaseAuth.getInstance().getUid(), cur, new ResultListener<Boolean>() {
            @Override
            public void finish(Boolean resultado) {

            }
        });

        if (imageUri != null){
            postController.uploadImageToStorage(imageUri, this.user, new ResultListener<String>() {
                @Override
                public void finish(String resultado) {
                    post.setImageURl(resultado);
                    postController.uploadPost(filePath, post, new ResultListener<Boolean>() {
                        @Override
                        public void finish(Boolean resultado) {
                            if (resultado){

                                finishThisActivity();
                            }
                        }
                    });
                }
            });
        }
        else {
            post.setImageURl("https://firebasestorage.googleapis.com/v0/b/voices-ad267.appspot.com/o/dj.png?alt=media&token=dcefe0dd-329d-40dc-b86a-5de04135ddae");
            postController.uploadPost(filePath, post, new ResultListener<Boolean>() {
                @Override
                public void finish(Boolean resultado) {
                    if (resultado){

                        finishThisActivity();
                    }
                }
            });
        }





    }

    @Override
    public void uploadDetailsFragmentOnClickLinear(int i) {



        Fragment nuevoFragment = null;
        FragmentManager unFragmentManager = getSupportFragmentManager();
        FragmentTransaction unFragmentTransaction = unFragmentManager.beginTransaction();
        String fragmentName = null;


        switch (i) {
            case 0:
                nuevoFragment = new SelectHashFragment();
                fragmentName = "HASH";
                break;
            case 1:
                nuevoFragment = new SelectUsersFragment();
                fragmentName = "USERS";
                break;
            case 2:
                nuevoFragment = new SelectLocationFragment();
                fragmentName = "LOCATION";
                break;
        }


        unFragmentTransaction.replace(R.id.upload_activity_fragment_container, nuevoFragment, fragmentName);
        unFragmentTransaction.addToBackStack(null);
        unFragmentTransaction.commit();


    }

    @Override
    public void getImage() {


        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(UploadActivity.this)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {

                        imageUri = uri;
                    }
                })
                .create();

        tedBottomPicker.show(getSupportFragmentManager());



        /*Matisse.from(UploadActivity.this)
                .choose(MimeType.of(MimeType.GIF, MimeType.JPEG, MimeType.PNG))
                .countable(true)
                .theme(R.style.Matisse_Dracula)
                .maxSelectable(1)
                .spanCount(2)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new MyGlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);*/
    }

    @Override
    public void selectHashFragmentInteraction(Uri uri) {

    }

    @Override
    public void onHashSelected() {
        Fragment nuevoFragment = new UploadDetailsFragment();
        FragmentManager unFragmentManager = getSupportFragmentManager();
        FragmentTransaction unFragmentTransaction = unFragmentManager.beginTransaction();
        unFragmentTransaction.replace(R.id.upload_activity_fragment_container, nuevoFragment, "DETAILS");
        unFragmentTransaction.commit();
        unFragmentManager.popBackStack();
    }

    @Override
    public void selectUserFragmentInteraction(Uri uri) {

    }

    @Override
    public void onUsersSelected() {
        Fragment nuevoFragment = new UploadDetailsFragment();
        FragmentManager unFragmentManager = getSupportFragmentManager();
        FragmentTransaction unFragmentTransaction = unFragmentManager.beginTransaction();
        unFragmentTransaction.replace(R.id.upload_activity_fragment_container, nuevoFragment, "DETAILS");
        unFragmentTransaction.commit();
        unFragmentManager.popBackStack();
    }

    @Override
    public void selectLocationFragmentInteraction(Uri uri) {

    }

    @Override
    public void onLocationSelected() {
        Fragment nuevoFragment = new UploadDetailsFragment();
        FragmentManager unFragmentManager = getSupportFragmentManager();
        FragmentTransaction unFragmentTransaction = unFragmentManager.beginTransaction();
        unFragmentTransaction.replace(R.id.upload_activity_fragment_container, nuevoFragment, "DETAILS");
        unFragmentTransaction.commit();
        unFragmentManager.popBackStack();
    }
    private void finishThisActivity(){
        setResult(123);
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Uri> mSelected;
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            imageUri = mSelected.get(0);
            Log.d("Matisse", "mSelected: " + mSelected);
        }
    }
}

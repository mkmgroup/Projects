package com.example.voices.view.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.voices.R;
import com.example.voices.controller.UserController;
import com.example.voices.model.User;
import com.example.voices.util.ResultListener;
import com.example.voices.view.MainActivity;
import com.example.voices.view.search.SearchViewPagerAdapter;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;



public class LoginActivity extends AppCompatActivity implements LoginFragment.LoginOnFragmentInteractionListener, RegisterFragment.RegisterOnFragmentInteractionListener {

    private static final String TAG2 = "FacebookLogin";
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private CallbackManager mCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    SignInButton googleSingInButton;
    ProfileTracker profileTracker;
    UserController userController;
    Profile fbProfile;
    ViewPager viewPager;
    LoginViewPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userController = new UserController();
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        googleSingInButton = findViewById(R.id.sign_in_button_google);
        viewPager = (ViewPager) findViewById(R.id.viewPagerLogin);
        adapterViewPager = new LoginViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsLogin);
        tabLayout.setupWithViewPager(viewPager);

        googleSingInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                fbProfile = currentProfile;
            }
        };

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_facebook_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG2, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Log.d(TAG2, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG2, "facebook:onError", error);
                // ...
            }
        });

    }


    public void loginToFire(String username, String password){
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), user.getEmail(),Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "No anduvo", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void registerOnFragmentInteraction(String mail, String password, User user) {
        mAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("auth with mail", "createUserWithEmail:success");
                            FirebaseUser userf = mAuth.getCurrentUser();
                            userController.createUserOnDb(user, userf.getUid(), new ResultListener<Boolean>() {
                                @Override
                                public void finish(Boolean resultado) {
                                    if (resultado){
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("auth with mail", "createUserWithEmail:failure", task.getException());

                            Toast.makeText(LoginActivity.this, "Authentication failed. " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }


    @Override
    public void loginOnFragmentInteraction(String username, String password) {
        loginToFire(username, password);
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            user = mAuth.getCurrentUser();
                            userController.checkIfUserExists(user.getUid(), new ResultListener<Boolean>() {
                                @Override
                                public void finish(Boolean resultado) {
                                    if (resultado){
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        User user2 = new User();
                                        user2.setName(acct.getDisplayName());
                                        user2.setAvatarUrl(acct.getPhotoUrl().toString());
                                        user2.setNickname(acct.getDisplayName());
                                        user2.setFirsTime(true);
                                        userController.createUserOnDb(user2, user.getUid(), new ResultListener<Boolean>() {
                                            @Override
                                            public void finish(Boolean resultado) {
                                                if (resultado){
                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });

                                    }
                                }
                            });


                            Log.d(TAG, "signInWithCredential:success");

                            Toast.makeText(getApplicationContext(), user.getEmail(),Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }

                    }
                });
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            user = mAuth.getCurrentUser();
                            userController.checkIfUserExists(user.getUid(), new ResultListener<Boolean>() {
                                @Override
                                public void finish(Boolean resultado) {
                                    if (resultado){
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        User user2 = new User();
                                        user2.setName(fbProfile.getName());
                                        user2.setAvatarUrl("https://graph.facebook.com/" + fbProfile.getId() + "/picture?type=large");
                                        user2.setNickname(fbProfile.getName());
                                        user2.setFirsTime(true);
                                        userController.createUserOnDb(user2, user.getUid(), new ResultListener<Boolean>() {
                                            @Override
                                            public void finish(Boolean resultado) {
                                                if (resultado){
                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(intent);
                                                }

                                            }
                                        });
                                    }

                                }
                            });

                            Log.d(TAG2, "signInWithCredential:success");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG2, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }
}

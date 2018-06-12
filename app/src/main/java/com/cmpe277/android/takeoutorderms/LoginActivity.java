package com.cmpe277.android.takeoutorderms;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cmpe277.android.takeoutorderms.model.Constant;
import com.cmpe277.android.takeoutorderms.model.User;
import com.cmpe277.android.takeoutorderms.service.GMailSender;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
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
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText username;
    private EditText password;

    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    /**
     * For Google Login
     */
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    /**
     * For FB Login
     */
    private static final int FB_SIGN_IN = 9002;
    private CallbackManager mCallbackManager;

    GMailSender sender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        sender = new GMailSender("yaoweibio@gmail.com", Constant.PASSWORD );

        /**
         * Google Sign-in Initiation
         */
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_login_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //initiate App setClient
        App.getInstance().setmGoogleSignInClient(mGoogleSignInClient);
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_google_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(this);

        /**
         * Facebook Sign-in Initiation
         */
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.sign_in_fb_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });
    }

    private void handleFacebookAccessToken (AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    onAuthSuccess(user);
                }else {
                    Log.w(TAG, "signInWithCredential: failure", task.getException());
                    Toast.makeText(LoginActivity.this, "FB Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseAuth.getInstance().signOut();;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_google_button:
                signIn();
                break;

            case R.id.adminlogin:
                adminLogin();
                break;

        }
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
        } else{
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            onAuthSuccess(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.google_sign_in), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void onAuthSuccess(FirebaseUser user){

            if (user != null) {

                // UID specific to the provider
                final String uid = user.getUid();

                // Name, email address, and profile photo Url
                final String name = user.getDisplayName();
                final String email = user.getEmail();

                //Write new user into database
                final User newUser = new User(uid, name, email);
                mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //look through users in db and match with names
                       for (DataSnapshot da : dataSnapshot.getChildren()) {
                            User user = da.getValue(User.class);
                            if (user.getUseId().equalsIgnoreCase(uid)) {
                                return;
                            }
                        }
                        //if no name found, add this new user.
                        mDatabase.child("users").child(uid).setValue(newUser);
                        //send user a welcome email
                        sender = new GMailSender("yaoweibio@gmail.com", Constant.PASSWORD );
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.
                                Builder().permitAll().build();

                        StrictMode.setThreadPolicy(policy);
                        try {
                            new MyAsyncClass().execute(name, email);
                        } catch (Exception e) {
                            Log.e("SendMail", e.getMessage(), e);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                //show customer UI to user
                Intent intent = new Intent(LoginActivity.this, MainCustomerActivity.class);
                intent.putExtra(Constant.USER_ID, uid);
                intent.putExtra(Constant.USER_EMAIL, email);
                startActivity(intent);

            }

    }


    //onclick method for Admin login
    private void adminLogin() {
        String logInName = username.getText().toString().trim();
        String pass= password.getText().toString().trim();

        if (logInName.equalsIgnoreCase(Constant.ADMIN) && pass.equalsIgnoreCase(Constant.ADMIN)) {
            mAuth.signInWithEmailAndPassword(Constant.ADMIN_LOGIN, Constant.ADMIN_PASS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, MainAdminActivity.class));
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Admin Login failed", Toast.LENGTH_SHORT).show();
        }
    }

    class MyAsyncClass extends AsyncTask<String, Void, Void> {

        ProgressDialog pDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... para) {
            try {
                // Add subject, Body, your mail Id, and receiver mail Id.
                sender.sendMail("Welcome to TakeOut App", "Dear "+ para[0] + Constant.WELCOME_EMAIL_BODY , "yaoweibio@gmail.com",para[1]);

            }

            catch (Exception ex) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();
            Toast.makeText(getApplicationContext(), "Email send", Toast.LENGTH_SHORT).show();
        }
    }



}


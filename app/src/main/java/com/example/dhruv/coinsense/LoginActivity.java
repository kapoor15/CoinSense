package com.example.dhruv.coinsense;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private Button reg;
    private Button forgot;
    private EditText user;
    private EditText pass;
    private LoginButton login_fb;
    FirebaseAuth firebaseAuth;
    private ProgressDialog pd;
    CallbackManager mCallbackManager;
    private int attempts;

    DatabaseReference root;
    DatabaseReference usersdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.setApplicationId("1996674077241804");
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        root = FirebaseDatabase.getInstance().getReference();
        usersdb = root.child("users");
        pd = new ProgressDialog(this);
        login = (Button) findViewById(R.id.login_btn);
        reg = (Button) findViewById(R.id.reg_btn);
        forgot = (Button) findViewById(R.id.forgot_btn);
        login_fb = (LoginButton) findViewById(R.id.login_facebook);
        login_fb.setReadPermissions("email", "public_profile");
        attempts = 0;
        user = (EditText) findViewById(R.id.login_email);
        pass = (EditText) findViewById(R.id.login_password);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void LoginUser() {
        String email = user.getText().toString().trim();
        String password = pass.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

       pd.setMessage("Logging in...");
       pd.show();

        firebaseAuth.signInWithEmailAndPassword(email,password).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    startActivity(new Intent(getApplicationContext(), CurrencyActivity.class));
                } else {
                    attempts++;
                    if(attempts == 3) {
                        reg.setVisibility(reg.VISIBLE);
                    }
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    pd.hide();
                }
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            usersdb.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                       @Override
                                                                       public void onDataChange(DataSnapshot snapshot) {

                                                                           System.out.println("LOOOLLLL");
                                                                           boolean flag = false;
                                                                           for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                                               User user = childSnapshot.getValue(User.class);

                                                                               if (user.email.equals(firebaseAuth.getCurrentUser().getEmail())) {
                                                                                   flag = true;
                                                                               }
                                                                           }

                                                                           if (flag) {
                                                                               startActivity(new Intent(getApplicationContext(), CurrencyActivity.class));
                                                                           } else {
                                                                               startActivity(new Intent(getApplicationContext(), FacebookLogin.class));
                                                                           }
                                                                       }

                                    /*
                                    if (snapshot.child(firebaseAuth.getCurrentUser().getEmail().toString()).exists()) {
                                        startActivity(new Intent(getApplicationContext(), CurrencyActivity.class));
                                    }else{
                                        startActivity(new Intent(getApplicationContext(), FacebookLogin.class));
                                    }
                                    */


                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            // Sign in success, update UI with the signed-in user's information

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }



    public void LoginfbUser() {
        login_fb.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                // ...
            }
        });
    }
    public void checkLogin(View v) {
        if (v == login) {
            LoginUser();
        }
    }

    public void checkFacebook(View v) {
        if (v == login_fb) {
            LoginfbUser();
        }
    }

    public void ResetPassword() {
        String email = user.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Check email to reset password",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Email not found. Please register.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void forgotPassword(View v) {
        if (v == forgot) {
            ResetPassword();
        }
    }

    public void regpage(View v) {
        if (v == reg) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
}

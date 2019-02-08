package com.example.pankaj.farmguide;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.app.Dialog;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class  LoginActivity extends AppCompatActivity {
    public static final int RC_SIGN_IN =1;
    FirebaseAuth mFirebaseAuth;
    Button buy,sell;
    ImageButton diseasedetector,buyingselling;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);





        // Choose authentication providers
        FirebaseApp.initializeApp(this);


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {


                    //If there is some user signed in
            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
            diseasedetector = (ImageButton)findViewById(R.id.diseasedetector);
            buyingselling=(ImageButton)findViewById(R.id.buyingselling);


            diseasedetector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(LoginActivity.this, "Already signed in...", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, PhotoPicker.class);
                    startActivity(i);
                }
            });


            buyingselling.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyCustomAlertDialog();
                }
            });

            //  Intent intent = new Intent(LoginActivity.this, AddProduct.class);
    //startActivity(intent);
    //finish();



                }
                else {
// Create and launch sign-in intent
    Toast.makeText(this, "Not Logged in", Toast.LENGTH_SHORT).show();
                    List<AuthUI.IdpConfig> providers = Arrays.asList(

                            new AuthUI.IdpConfig.PhoneBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build()
                    );

                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);

                }
            }


        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == RC_SIGN_IN) {
                IdpResponse response = IdpResponse.fromResultIntent(data);

                if (resultCode == RESULT_OK) {
                    // Successfully signed in
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    // ...

                    Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Sign in failed. If response is null the user canceled the
                    // sign-in flow using the back button. Otherwise check
                    // response.getError().getErrorCode() and handle the error.
                    // ...
                }
            }
        }


        // function to create popup window


    public void MyCustomAlertDialog(){
        final Dialog MyDialog = new Dialog(LoginActivity.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.customdialog);

        Button buy = (Button)MyDialog.findViewById(R.id.buy);
        Button sell = (Button)MyDialog.findViewById(R.id.sell);

        buy.setEnabled(true);
        sell.setEnabled(true);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, BuyerPortal.class);
                startActivity(intent);

            }
        });
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, AddProduct.class);
                startActivity(intent);

            }
        });
        MyDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    }


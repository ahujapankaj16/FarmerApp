package com.example.pankaj.farmguide;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    EditText UserLocation,UserName;
    FirebaseFirestore mFirestore;
    Button AddUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        UserLocation = findViewById(R.id.UserLocation);
        UserName = findViewById(R.id.UserName);
        AddUser = findViewById(R.id.AddUser);
        mFirestore = FirebaseFirestore.getInstance();
        AddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name,location;

                name = UserName.getText().toString();
                location = UserLocation.getText().toString();

                Map<Object, Object> data = new HashMap<>();

                data.put("Name", name);
                data.put("Location", location);
                data.put("Rating", 0);
                mFirestore.collection("Seller").document(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("profile", "DocumentSnapshot successfully written!");

                                Intent intent = new Intent(ProfileActivity.this,AddProduct.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("profile", "Error writing document", e);
                            }
                        });

            }
        });

    }
}

package com.example.pankaj.farmguide;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public class BuyerPortal extends AppCompatActivity {
    Button chats,ShowProducts;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_portal);
        chats = findViewById(R.id.chat);
        ShowProducts = findViewById(R.id.Showproducts);
        db = FirebaseFirestore.getInstance();
        chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //intent transfer
                FirebaseFirestore.getInstance().collection("Seller").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            List<String> group = (List<String>) document.get("chat_id");
                            Toast.makeText(BuyerPortal.this, group.get(0), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(BuyerPortal.this,ChatList.class);
                            intent.putExtra("chatid",group.get(0));
                            startActivity(intent);

                        }
                    }
                });
            }
        });
        ShowProducts.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(BuyerPortal.this,ShowProducts.class);
            startActivity(intent);
        }
    });

    }
}
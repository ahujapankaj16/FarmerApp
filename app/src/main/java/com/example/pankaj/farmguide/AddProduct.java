package com.example.pankaj.farmguide;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class AddProduct extends AppCompatActivity {
    EditText Type,Name,Price,Quantity;
     FirebaseFirestore mFirestore,db;
     Button AddProduct,show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Type = findViewById(R.id.type);
        Name = findViewById(R.id.Name);
        Price = findViewById(R.id.Price);
        Quantity = findViewById(R.id.Quantity);
        AddProduct = findViewById(R.id.AddProduct);
        show = findViewById(R.id.show);
        mFirestore = FirebaseFirestore.getInstance();
        db = mFirestore;
        Toast.makeText(this, FirebaseAuth.getInstance().getCurrentUser().getUid().toString(), Toast.LENGTH_SHORT).show();
        AddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type,name,price,quantity;
                type = Type.getText().toString();
                name = Name.getText().toString();
                price = Price.getText().toString();
                quantity = Quantity.getText().toString();
                Map<String, Object> data = new HashMap<>();
                data.put("Type", type);
                data.put("Name", name);
                data.put("Price", price);
                data.put("Quantity", quantity);
                data.put("Seller_id", FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

                db.collection("product")
                        .add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("tag", "DocumentSnapshot written with ID: " + documentReference.getId());
                                Map<String, Object> pdata = new HashMap<>();
                                pdata.put("name", documentReference.getId().toString());
                                db.collection("Seller").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("product")
                                        .add(pdata)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("tag", "Error adding document", e);
                            }
                        });

            }
        });



        show.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(AddProduct.this,ShowProducts.class);
        startActivity(intent);
    }
});
    }

}

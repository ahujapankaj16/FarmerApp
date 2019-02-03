package com.example.pankaj.farmguide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShowProducts extends AppCompatActivity {
    FirebaseFirestore db;
    TextView show;
    ProductAdapter adapter;
    List<Product> list;
    public static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_products);

        list=new ArrayList<>();
         adapter = new ProductAdapter(list);
        db = FirebaseFirestore.getInstance();
        CollectionReference productRef = db.collection("product");
        context = getApplicationContext();
   //     Query query = productRef.orderBy("Price", Query.Direction.DESCENDING);

//        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
//                .setQuery(query, Product.class)
//                .build();

        //adapter = new ProductAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.show_rcv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        db.collection("product")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String s ="";
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d("Result", document.getId() + " => " + document.getData());
                                Map<String,Object> m = document.getData();
                              //
                                Product p = new Product(m.get("Name").toString(),Integer.parseInt(m.get("Price").toString()),Integer.parseInt(m.get("Quantity").toString()),m.get("Seller_id").toString(),m.get("Type").toString());
                                list.add(p);
                                adapter.notifyDataSetChanged();
                             //
                            }
                        } else {
                            Log.d("Result", "Error getting documents: ", task.getException());
                        }
                    }
                });
}

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}

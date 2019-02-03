package com.example.pankaj.farmguide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChatList extends AppCompatActivity {
    List<chat_list_item> list;
    ChatListAdapter adapter;
    FirebaseFirestore db;
    public static Context Ccontext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        list=new ArrayList<>();
        adapter = new ChatListAdapter(list);
        db = FirebaseFirestore.getInstance();
        Ccontext = getApplicationContext();
       // CollectionReference productRef = db.collection("product");

        //     Query query = productRef.orderBy("Price", Query.Direction.DESCENDING);

//        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
//                .setQuery(query, Product.class)
//                .build();

        //adapter = new ProductAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.chat_rcv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        db.collection("Seller").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            List<String> group = (List<String>) document.get("chat_id");
                            for (int i = 0; i < group.size(); i++)
                                list.add(new chat_list_item(group.get(i)));
                            adapter.notifyDataSetChanged();
                            //

                        } else {
                            Log.d("Result", "Error getting documents: ", task.getException());
                        }
                    }


    });
}
}

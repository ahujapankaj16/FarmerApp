package com.example.pankaj.farmguide;

import android.content.Intent;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    EditText editText;
    ImageView addBtn;
    MessageListAdapter adapter;
    ImageButton mPhotoPickerButton;
    public static final int RC_SIGN_IN = 1;
    public static final int RC_PHOTO_PICKER = 2;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosReference;
    ListView messageListView;
    ArrayList<FriendlyMessage> arrayList;
    private FirebaseFirestore mfirestore;
    private DatabaseReference fireRef;
    private FirebaseAuth firebaseAuth;
    public DatabaseReference chatReference;
    private FirebaseDatabase mfirebase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Bundle bundle = getIntent().getExtras();
        final String chat_id  = bundle.getString("message_id");
        mFirebaseStorage = FirebaseStorage.getInstance();
        mfirestore = FirebaseFirestore.getInstance();
        mfirebase = FirebaseDatabase.getInstance();
        mChatPhotosReference = mFirebaseStorage.getReference().child("chat_photos");
        messageListView = findViewById(R.id.messageListView);

        editText = (EditText) findViewById(R.id.editText1);
        addBtn = findViewById(R.id.fab_img);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mPhotoPickerButton = findViewById(R.id.photoPickerButton);
        arrayList = new ArrayList<>();
        adapter = new  MessageListAdapter(this, R.layout.card_view, arrayList);
        messageListView.setAdapter(adapter);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("chats").child(chat_id);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FriendlyMessage message = (FriendlyMessage) dataSnapshot.getValue(FriendlyMessage.class);
                adapter.add(message);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);

            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d=new Date();
                SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
                String currentDateTimeString = sdf.format(d);

                FriendlyMessage friendlyMessage = new FriendlyMessage(editText.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(),null);

                databaseReference.push().setValue(friendlyMessage);
                // Clear input box
                editText.setText("");
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 11:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                   // Log.d("sst", result.get(0));
                    editText.setText(result.get(0));
                }
                break;
        }

        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            StorageReference photoReference = mChatPhotosReference.child(imageUri.getLastPathSegment());

            //upload file to firebase
            photoReference.putFile(imageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    //Toast.makeText(ChatbotActivity.this, downloadUrl.toString(), Toast.LENGTH_SHORT).show();
                    FriendlyMessage message = new FriendlyMessage(null, "Me", taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                    adapter.add(message);

                     databaseReference.push().setValue(message);
                }
            });
        }
}}

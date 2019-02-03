package com.example.pankaj.farmguide;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PhotoPicker extends AppCompatActivity {

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int RC_PHOTO_PICKER = 101;
    Button btnFromCamera, btnFromGallery;
    private ProgressDialog progressDialog;


    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);


        progressDialog = new ProgressDialog(this);

        storageReference = FirebaseStorage.getInstance().getReference().child("plant_photos");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "I don't have camera permission", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
            }

        }


        btnFromCamera = (Button) findViewById(R.id.btnFromCamera);
        btnFromGallery = (Button) findViewById(R.id.btnFromGallery);

        btnFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



//

            }
        });

        btnFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(intent,"complete action using"),RC_PHOTO_PICKER);
            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (requestCode == MY_CAMERA_REQUEST_CODE){


            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){

            progressDialog.setMessage("Uploading Image....");
            progressDialog.show();

            Uri imageUri = data.getData();
            Log.d("Image_url", String.valueOf(imageUri));

            final StorageReference photoReference = storageReference.child(imageUri.getLastPathSegment());
            Log.d("Image_url",photoReference.toString());

            //upload file to firebase
            photoReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    photoReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            progressDialog.dismiss();
                            Toast.makeText(PhotoPicker.this, "Upload Finished", Toast.LENGTH_SHORT).show();
                            Log.d("Image_url",uri.toString());

                        }
                    });

                }
            });
        }


    }
}

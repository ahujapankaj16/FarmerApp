package com.example.pankaj.farmguide;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

public class PhotoPicker extends AppCompatActivity {

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int RC_PHOTO_PICKER = 101;
    Button btnFromCamera;
    FloatingActionButton fabbtnFromGallery;
    TextView tvPrediction;
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
        fabbtnFromGallery = (FloatingActionButton) findViewById(R.id.fabbtnFromGallery);
        tvPrediction = (TextView) findViewById(R.id.tvPrediction);

        btnFromCamera.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {



//

            }
        });

        fabbtnFromGallery.setOnClickListener(new View.OnClickListener() {
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
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
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
                            String image_url = uri.toString();

                            String URL = "http://farmguide.eastus.cloudapp.azure.com:5000/predict";
                            JSONObject json = new JSONObject();
                            try {
                                //String image_url = "https://firebasestorage.googleapis.com/v0/b/fir-authui-9c7da.appspot.com/o/crop_photo%2F1b32ba52-1a78-4030-9317-ea5d2ea66cd7___Mary_HL%209232.JPG?alt=media&token=23888b0b-7c5b-48fe-97b7-f3d357203e9e";
                                json.put("image_url", image_url);
                                Log.d("Response",json.toString());
                            } catch (JSONException e) {

                                Log.d("Response",e.toString());
                            }
                            RequestQueue requestQueue= Volley.newRequestQueue(PhotoPicker.this);
                            JsonObjectRequest objectRequest = new JsonObjectRequest(
                                    Request.Method.POST,
                                    URL,
                                    json,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {

                                            Log.d("Response_success",response.toString());
                                            tvPrediction.setText(response.toString());
                                            Toast.makeText(PhotoPicker.this, response.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d("Response", error.toString());

                                        }
                                    }

                            );
                            requestQueue.add(objectRequest);

                        }
                    });

                }
            });
        }


    }
}

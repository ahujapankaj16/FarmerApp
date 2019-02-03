package com.example.pankaj.farmguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class APICall extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apicall);


        String URL = "http://192.168.1.11:5000/predict";
        JSONObject json = new JSONObject();
        try {
            String image_url = "https://firebasestorage.googleapis.com/v0/b/fir-authui-9c7da.appspot.com/o/crop_photo%2F1b32ba52-1a78-4030-9317-ea5d2ea66cd7___Mary_HL%209232.JPG?alt=media&token=23888b0b-7c5b-48fe-97b7-f3d357203e9e";
            json.put("image_url", image_url);
            Log.d("Response",json.toString());
        } catch (JSONException e) {

            Log.d("Response",e.toString());
        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Response",response.toString());
                        Toast.makeText(APICall.this, response.toString(), Toast.LENGTH_SHORT).show();
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
}

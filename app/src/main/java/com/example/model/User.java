package com.example.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.*;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private final static String
    url_tuju="authenticate";
    private String email;
    private String password;
    private String role;

    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public String getTokenAuth(Context context){
        String res=null;
        try {

            String json=new Gson().toJson(this);
            JSONObject obj = new JSONObject(json);
            res=this.volleyCall(obj,context);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return res;
    }
    private String volleyCall(JSONObject json, Context context) {
        String res=null;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.BASE_URL + this.url_tuju, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d( "onResponse: ",response.toString());
//                processRes(response.toString());
            }
        },null);
        requestQueue.add(jsonObjectRequest);
        return res;
    }
}

package com.example.traficoreto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class Users {

    private Context context;
    private Activity activity;

    public Users(Context context,Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void login(String email, String password) {
        String url = "http://10.10.12.218:3000/login";
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("fernando",String.valueOf(response));
                        Toast.makeText(context, String.valueOf(response), Toast.LENGTH_LONG).show();
                        try {
                            String token = response.getString("token");
                            Intent intent = new Intent(activity, ListScreen.class);
                            intent.putExtra("TOKEN_KEY", token);
                            activity.startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores en la solicitud
                        // El detalle del error está en el parámetro 'error'
                        Log.d("fernando",String.valueOf(error));
                        Toast.makeText(context, String.valueOf(error), Toast.LENGTH_LONG).show();

                    }
                });
        queue.add(jsonObjectRequest);
    }

    public void register(String mail, String password,String name, String city, String country, int age, String street ) {
        String url = "http://10.10.12.218:3000/register";
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("mail", mail);
            jsonBody.put("password", password);
            jsonBody.put("name", name);
            jsonBody.put("city", city);
            jsonBody.put("country", country);
            jsonBody.put("age", age);
            jsonBody.put("street", street);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("fernando",String.valueOf(response));
                        Toast.makeText(context, String.valueOf(response), Toast.LENGTH_LONG).show();
                        try {
                            String token = response.getString("token");
                            Intent intent = new Intent(activity, ListScreen.class);
                            intent.putExtra("TOKEN_KEY", token);
                            activity.startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores en la solicitud
                        // El detalle del error está en el parámetro 'error'
                        Log.d("fernando",String.valueOf(error));
                        Toast.makeText(context, String.valueOf(error), Toast.LENGTH_LONG).show();

                    }
                });
        queue.add(jsonObjectRequest);
    }
}


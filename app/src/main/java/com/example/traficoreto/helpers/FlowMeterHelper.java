package com.example.traficoreto.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.traficoreto.adapters.FlowMeterAdapter;
import com.example.traficoreto.adapters.IncidenceAdapter;
import com.example.traficoreto.structure.FlowMeterStructure;
import com.example.traficoreto.structure.IncidenceStructure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FlowMeterHelper {
    private Context context;
    private Activity activity;

    public FlowMeterHelper(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }
    public static void getAllItem(String name, String page, RecyclerView recyclerView,String urlRequest,Context context, String userId) {

        String camera_url = urlRequest+name+"/" + page;
        String favourite_url = urlRequest+"fav/"+userId+"/"+name;

        //Log.d("fernando",url);

        // Crear una instancia de RequestQueue
        RequestQueue queue = Volley.newRequestQueue(context);

        // URL de la API

        // Realizar una solicitud JSON
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, camera_url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(JSONObject  response) {

                        JsonArrayRequest secondRequest = new JsonArrayRequest(Request.Method.GET, favourite_url, null,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray  secondResponse) {
                        // Procesar la respuesta JSON aquí
                        //  Log.d("fernando",String.valueOf(response));
                        try {
                            ArrayList<FlowMeterStructure> incidenceArray = new ArrayList<>();
                            int currentPage = response.getInt("currentPage");
                            // Obtener la lista de cámaras del objeto JSON
                            JSONArray camerasArray = response.getJSONArray("features");
                            for (int i = 0; i < camerasArray.length(); i++) {
                                JSONObject cameraObject = camerasArray.getJSONObject(i);
                                if (cameraObject.has("properties")) {
                                    JSONObject propertiesObject = cameraObject.getJSONObject("properties");

                                    String meterId = propertiesObject.optString("meterId", "");
                                    String sourceId = propertiesObject.optString("sourceId", "");
                                    String description = propertiesObject.optString("description", "");
                                    String provinceId = propertiesObject.optString("provinceId", "");
                                    String municipalityId = propertiesObject.optString("municipalityId", "");
                                    String latitude = propertiesObject.optString("latitude", "");
                                    String longitude = propertiesObject.optString("longitude", "");

                                    FlowMeterStructure incidence = new FlowMeterStructure(meterId,sourceId,provinceId,municipalityId,description,latitude,longitude,"",currentPage);
                                        try{
                                        for (int j = 0; j < secondResponse.length(); j++) {

                                            JSONObject jsonObject = secondResponse.getJSONObject(j);
                                            int id2 = jsonObject.getInt("id");
                                            int userId2 = jsonObject.getInt("user_id");
                                            String type2 = jsonObject.getString("type");
                                            String favId2 = jsonObject.getString("fav_id");
                                            String sourceId2 = jsonObject.getString("sourceId");
                                            int page2 = jsonObject.getInt("page");

                                            if (favId2.equals(meterId)&&page2==currentPage){
                                                Log.e("com.example.traficoreto.fernando","entro al fav de flow meter");

                                                incidence.setFavourite(true);
                                            }
                                        }
                                    }catch (Exception e){
                                        Log.e("com.example.traficoreto.fernando", e.toString());

                                    }
                                    //cameraStructure.add(camera1);
                                    incidenceArray.add(incidence);
                                }

                            }
                            //CameraStructure camera1 = new CameraStructure("12", "domo", "Sadas", "sadas", "dasdas", "sadasad");
                            //cameraStructure.add(camera1);
                            FlowMeterAdapter cameraAdapter = new FlowMeterAdapter(incidenceArray,urlRequest,context,userId,false);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(cameraAdapter);
                            // Log.d("fernando", "dasdada");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("fernando",e.toString());
                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError secondError) {
                                        Log.e("com.example.traficoreto.fernando", "Error en la segunda solicitud: " + secondError.toString());

                                    }
                                });
                        queue.add(secondRequest);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud aquí
                        Log.d("com.example.traficoreto.fernando", String.valueOf(error));

                    }
                });
        queue.add(jsonObjectRequest);
    }
}

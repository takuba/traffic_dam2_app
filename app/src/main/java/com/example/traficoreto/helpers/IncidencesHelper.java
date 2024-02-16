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
import com.example.traficoreto.adapters.CameraAdapter;
import com.example.traficoreto.adapters.IncidenceAdapter;
import com.example.traficoreto.structure.CameraStructure;
import com.example.traficoreto.structure.IncidenceStructure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class IncidencesHelper {
    private Context context;
    private Activity activity;

    public IncidencesHelper(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }
    public static void getAllItem(String name, String page, RecyclerView recyclerView,String urlRequest,Context context, String userId) {

        String incidences_url = urlRequest+name+"/" + page;
        String favourite_url = urlRequest+"fav/"+userId+"/"+name;

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, incidences_url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(JSONObject response) {

                        JsonArrayRequest secondRequest = new JsonArrayRequest(Request.Method.GET, favourite_url, null,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray  secondResponse) {
                                        try {
                                            ArrayList<IncidenceStructure> incidenceStructures = new ArrayList<>();
                                            int currentPage = response.getInt("currentPage");
                                            JSONArray camerasArray = response.getJSONArray("incidences");

                                            for (int i = 0; i < camerasArray.length(); i++) {
                                                JSONObject cameraObject = camerasArray.getJSONObject(i);
                                                String incidenceId = cameraObject.has("incidenceId") ? cameraObject.getString("incidenceId") : "";
                                                String sourceId = cameraObject.has("sourceId") ? cameraObject.getString("sourceId") : "";
                                                String incidenceType = cameraObject.has("incidenceType") ? cameraObject.getString("incidenceType") : "";
                                                String autonomousRegion = cameraObject.has("autonomousRegion") ? cameraObject.getString("autonomousRegion") : "";
                                                String province = cameraObject.has("province") ? cameraObject.getString("province") : "";
                                                String cause = cameraObject.has("cause") ? cameraObject.getString("cause") : "";
                                                String startDate = cameraObject.has("startDate") ? cameraObject.getString("startDate") : "";
                                                String latitude = cameraObject.has("latitude") ? cameraObject.getString("latitude") : "";
                                                String longitude = cameraObject.has("longitude") ? cameraObject.getString("longitude") : "";
                                                String incidenceLevel = cameraObject.has("incidenceLevel") ? cameraObject.getString("incidenceLevel") : "";
                                                String direction = cameraObject.has("direction") ? cameraObject.getString("direction") : "";

                                                IncidenceStructure incidence = new IncidenceStructure(incidenceId,sourceId,incidenceType,autonomousRegion,province,cause,startDate,latitude,longitude,incidenceLevel,direction,currentPage);

                                                try{
                                                    for (int j = 0; j < secondResponse.length(); j++) {

                                                        JSONObject jsonObject = secondResponse.getJSONObject(j);
                                                        int id2 = jsonObject.getInt("id");
                                                        int userId2 = jsonObject.getInt("user_id");
                                                        String type2 = jsonObject.getString("type");
                                                        String favId2 = jsonObject.getString("fav_id");
                                                        String sourceId2 = jsonObject.getString("sourceId");
                                                        int page2 = jsonObject.getInt("page");

                                                        if (favId2.equals(incidenceId)&&sourceId2.equals(sourceId)&&page2==currentPage){
                                                            incidence.setFavourite(true);
                                                        }
                                                    }
                                                }catch (Exception e){
                                                    Log.e("com.example.traficoreto.fernando", e.toString());

                                                }
                                                incidenceStructures.add(incidence);
                                            }
                                            IncidenceAdapter cameraAdapter = new IncidenceAdapter(incidenceStructures,urlRequest,context,userId,false);
                                            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                                            recyclerView.setLayoutManager(layoutManager);
                                            recyclerView.setAdapter(cameraAdapter);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Log.d("com.example.traficoreto.fernando", e.toString());
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

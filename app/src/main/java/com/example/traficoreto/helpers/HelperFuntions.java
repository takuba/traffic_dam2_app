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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.traficoreto.ListScreen;
import com.example.traficoreto.structure.CameraStructure;
import com.example.traficoreto.adapters.CameraAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HelperFuntions {
    private Context context;
    private Activity activity;
    private ListScreen listScreen;
    private String url;
    private String userId;


    public HelperFuntions(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public static void getAllItem(String name, String page, RecyclerView recyclerView,String urlRequest,Context context, String userId) {
        Log.d("com.example.traficoreto.fernando", "entro all getAllItem de camaras");

        String camera_url = urlRequest+name+"/" + page;
        String favourite_url = urlRequest+"fav/"+userId+"/"+name;

        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, camera_url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(JSONObject response) {

                        JsonArrayRequest secondRequest = new JsonArrayRequest(Request.Method.GET, favourite_url, null,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray  secondResponse) {
                                        try {
                                            ArrayList<CameraStructure> cameraStructure = new ArrayList<>();
                                            int currentPage = response.getInt("currentPage");
                                            JSONArray camerasArray = response.getJSONArray("cameras");

                                            for (int i = 0; i < camerasArray.length(); i++) {
                                                JSONObject cameraObject = camerasArray.getJSONObject(i);
                                                String cameraId = cameraObject.has("cameraId") ? cameraObject.getString("cameraId") : "";
                                                String cameraName = cameraObject.has("cameraName") ? cameraObject.getString("cameraName") : "";
                                                String urlImage = cameraObject.has("urlImage") ? cameraObject.getString("urlImage") : "";
                                                String latitude = cameraObject.has("latitude") ? cameraObject.getString("latitude") : "";
                                                String longitude = cameraObject.has("longitude") ? cameraObject.getString("longitude") : "";
                                                String sourceId = cameraObject.has("sourceId") ? cameraObject.getString("sourceId") : "";

                                                CameraStructure camera1 = new CameraStructure(cameraId, cameraName, urlImage, latitude, longitude, sourceId, currentPage);
                                                try{
                                                    for (int j = 0; j < secondResponse.length(); j++) {

                                                        JSONObject jsonObject = secondResponse.getJSONObject(j);
                                                        int id2 = jsonObject.getInt("id");
                                                        int userId2 = jsonObject.getInt("user_id");
                                                        String type2 = jsonObject.getString("type");
                                                        String favId2 = jsonObject.getString("fav_id");
                                                        String sourceId2 = jsonObject.getString("sourceId");
                                                        int page2 = jsonObject.getInt("page");

                                                        if (favId2.equals(cameraId)&&sourceId2.equals(sourceId)&&page2==currentPage){
                                                            camera1.setFavourite(true);
                                                        }
                                                    }
                                                }catch (Exception e){
                                                    Log.e("com.example.traficoreto.fernando", e.toString());

                                                }
                                                cameraStructure.add(camera1);
                                            }
                                            CameraAdapter cameraAdapter = new CameraAdapter(cameraStructure,urlRequest,context,userId,false);
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
                        Log.e("com.example.traficoreto.fernando", "Error en la primera solicitud: " + error);


                    }
                });
        queue.add(jsonObjectRequest);
    }

    public static void AddNewFavouriteToDB(String url,String user_id, String fav_id, String sourceId, String name, String type, int page, RecyclerView recyclerView) {
        url = url+"fav";
        Log.d("com.example.traficoreto.fernando","user_id:"+user_id+" type:"+type+" fav_id:"+fav_id+"sourceId: "+sourceId+"page: "+page+"name: "+name);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("user_id", user_id);
            jsonBody.put("type", type);
            jsonBody.put("fav_id", fav_id);
            jsonBody.put("sourceId", sourceId);
            jsonBody.put("page", page);
            jsonBody.put("name", name);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("com.example.traficoreto.fernando",response.toString());
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("com.example.traficoreto.fernando","ERROR en el add fav"+error.toString());

                // Log.e(TAG, "Error en la solicitud: " + error.toString());
                // Manejar errores aquí
            }
        };

        RequestQueue queue = Volley.newRequestQueue(recyclerView.getContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, successListener, errorListener);

        queue.add(jsonObjectRequest);
    }
    public static void deleteFavourite(String url,String userId, String type, String fav_id, String sourceId,Context context){
        RequestQueue queue = Volley.newRequestQueue(context); // Reemplaza AppContext.getInstance() con tu contexto

        String deleteUrlWithId = url + "fav/" + userId+"/"+type+"/"+fav_id+"/"+sourceId;

        // Crear la solicitud DELETE
        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, deleteUrlWithId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("com.example.traficoreto.fernando",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("com.example.traficoreto.fernando","error en el delete de fav: "+error.toString());
                    }
                });
        queue.add(deleteRequest);
    }

}

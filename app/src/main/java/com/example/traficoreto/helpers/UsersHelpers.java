package com.example.traficoreto.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.traficoreto.ListScreen;
import com.example.traficoreto.PaginacionScrollListener;
import com.example.traficoreto.adapters.CameraAdapter;
import com.example.traficoreto.adapters.FlowMeterAdapter;
import com.example.traficoreto.adapters.IncidenceAdapter;
import com.example.traficoreto.structure.CameraStructure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.traficoreto.structure.FlowMeterStructure;
import com.example.traficoreto.structure.IncidenceStructure;
import com.example.traficoreto.structure.User;
import com.example.traficoreto.structure.UserFavouriteStructure;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
public class UsersHelpers {

    private Context context;
    private Activity activity;
    private static String userId = null;
    public UsersHelpers(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public static void login(String url,String router,String email, String password, Context context) {

         url = url+router;
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("mail", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("com.example.traficoreto.fernando","response "+ String.valueOf(response));
                        //Toast.makeText(context, String.valueOf(response), Toast.LENGTH_LONG).show();
                        try {
                            String token = response.getString("token");
                            JSONObject userObject = response.getJSONObject("user");
                            if (userObject.has("id")) {
                                userId = userObject.getString("id");
                            } else {
                                Toast.makeText(context, "user error", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (userId != null) {
                            //save the user id in the preferences
                            MyPreferences.saveUserId(context,userId);
                            Intent intent = new Intent(context, ListScreen.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("com.example.traficoreto.fernando","error  "+String.valueOf(error));
                        Toast.makeText(context, String.valueOf(error), Toast.LENGTH_LONG).show();

                    }
                });
        queue.add(jsonObjectRequest);
    }

    public void register(String mail, String password,String name, String city, String country, int age, String street ) {
        String url = "http:///192.168.0.15:3000/register";
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
                        Log.d("com.example.traficoreto.fernando",String.valueOf(response));
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
                        Log.d("fernando",error.toString());
                        if (error.networkResponse != null) {
                            Log.d("com.example.traficoreto.fernando", "Código de respuesta: " + error.networkResponse.statusCode);
                            Log.d("com.example.traficoreto.fernando", "Datos del error: " + new String(error.networkResponse.data));
                            try {
                                Log.d("com.example.traficoreto.fernando", "Datos del error: " + new JSONObject(new String(error.networkResponse.data)).getString("error"));
                                Toast.makeText(context, new JSONObject(new String(error.networkResponse.data)).getString("error"), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                       // Toast.makeText(context, String.valueOf(error), Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(jsonObjectRequest);
    }
    public static void getUserInfoFromDb(String userId, String url, Context context, UserCallback callback) {
        // Inicializar la cola de solicitudes de Volley
        RequestQueue queue = Volley.newRequestQueue(context);

        // URL de la API
        url = url + "user/" + userId;

        // Hacer la solicitud GET usando JsonArrayRequest
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Manejar la respuesta del servidor
                        try {
                            int id = response.getJSONObject(0).getInt("id");
                            String mail = response.getJSONObject(0).getString("mail");
                            String password = response.getJSONObject(0).getString("password");
                            String name = response.getJSONObject(0).optString("name", "");
                            String city = response.getJSONObject(0).optString("city", "");
                            String country = response.getJSONObject(0).optString("country", "");
                            int age = response.getJSONObject(0).optInt("age", 0);
                            String street = response.getJSONObject(0).optString("street", "");

                            User user = new User(id, mail, password, name, city, country, age, street);

                            // Llamar al método onSuccess en la interfaz de retorno
                            callback.onSuccess(user);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("com.example.traficoreto.fernando", e.toString());

                            // Llamar al método onError en la interfaz de retorno
                            callback.onError(e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud
                        Log.e("com.example.traficoreto.fernando", error.toString());

                        // Llamar al método onError en la interfaz de retorno
                        callback.onError(error.toString());
                    }
                });

        // Agregar la solicitud a la cola de solicitudes
        queue.add(jsonArrayRequest);
    }
    public static void seLanguage(Context context,String currentLang){
        Locale locale = new Locale(currentLang);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        Resources resources = context.getResources();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    public static void getFavourites(String userId, String type, String url, Context context, RecyclerView recyclerView) {
        ArrayList<CameraStructure> cameraStructure = new ArrayList<>();

        // Crear una instancia de RequestQueue
        RequestQueue queue = Volley.newRequestQueue(context);

        // Realizar una solicitud JSON
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            final int finalIndex = response.length() - 1;

                            for (int i = 0; i < response.length(); i++) {
                                final int currentIndex = i;
                                JSONObject item = response.getJSONObject(i);

                                String urlSecundaria = "http://10.10.12.218:3000/cameras/"+item.getString("fav_id")+"/"+item.getString("sourceId");

                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlSecundaria, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response2) {
                                                try {
                                                    JSONArray camerasArray = response2.getJSONArray("cameras");
                                                    for (int i = 0; i < camerasArray.length(); i++) {
                                                        JSONObject cameraObject = camerasArray.getJSONObject(i);
                                                        if (item.getString("name").equals(cameraObject.getString("cameraName")))
                                                        {
                                                            CameraStructure cameraData = procesarRespuestaSecundaria(cameraObject);
                                                            cameraData.setFavourite(true);
                                                            cameraStructure.add(cameraData);
                                                        }
                                                        //Log.d("com.example.traficoreto.fernando", cameraObject.getString("cameraId"));
                                                    }
                                                    // Si es la última iteración del bucle, visualizar los datos
                                                    if (currentIndex == finalIndex) {
                                                        //Log.d("com.example.traficoreto.fernando", "llegoooo1");
                                                        visualizarDatos(cameraStructure, recyclerView, context);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    Log.d("com.example.traficoreto.fernando", e.toString());
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                // Manejar errores de la solicitud secundaria aquí
                                                error.printStackTrace();
                                            }
                                        });

                                // Añadir la solicitud secundaria a la cola
                                queue.add(jsonObjectRequest);
                            }
                        } catch (JSONException e) {
                            Log.d("com.example.traficoreto.fernando", "error: " + e.toString());
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud principal aquí
                        Log.d("com.example.traficoreto.fernando", String.valueOf(error));
                    }
                });
        queue.add(jsonArrayRequest);
    }
    public static void getFavourites2(String userId, String type, String url, Context context, RecyclerView recyclerView) {
        ArrayList<IncidenceStructure> incidenceArray = new ArrayList<>();

        // Crear una instancia de RequestQueue
        RequestQueue queue = Volley.newRequestQueue(context);

        // Realizar una solicitud JSON
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            final int finalIndex = response.length() - 1;

                            for (int i = 0; i < response.length(); i++) {
                                final int currentIndex = i;
                                JSONObject item = response.getJSONObject(i);

                                String urlSecundaria = "http://10.10.12.218:3000/incidences/"+item.getString("fav_id")+"/"+item.getString("sourceId");
                                Log.d("com.example.traficoreto.fernando", urlSecundaria);

                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlSecundaria, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response2) {
                                                try {
                                                    JSONArray camerasArray = response2.getJSONArray("incidences");

                                                    for (int i = 0; i < camerasArray.length(); i++) {
                                                        JSONObject incidenceObject = camerasArray.getJSONObject(i);
                                                        if (item.getString("name").equals(incidenceObject.getString("incidenceType")))
                                                        {
                                                            //Log.d("com.example.traficoreto.fernando", incidenceObject.getString("incidenceType"));
                                                            IncidenceStructure incidenceData = procesarRespuestaSecundaria2(incidenceObject);
                                                            incidenceData.setFavourite(true);
                                                            incidenceArray.add(incidenceData);
                                                        }
                                                        //Log.d("com.example.traficoreto.fernando", cameraObject.getString("incidenceType"));
                                                    }
                                                    // Si es la última iteración del bucle, visualizar los datos
                                                    if (currentIndex == finalIndex) {
                                                        //Log.d("com.example.traficoreto.fernando", "entro al condicional de curren final index");
                                                        visualizarDatos2(incidenceArray, recyclerView, context);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    Log.d("com.example.traficoreto.fernando", e.toString());
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                // Manejar errores de la solicitud secundaria aquí
                                                error.printStackTrace();
                                            }
                                        });

                                // Añadir la solicitud secundaria a la cola
                                queue.add(jsonObjectRequest);
                            }
                        } catch (JSONException e) {
                            Log.d("com.example.traficoreto.fernando", "error: " + e.toString());
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud principal aquí
                        Log.d("com.example.traficoreto.fernando", String.valueOf(error));
                    }
                });
        queue.add(jsonArrayRequest);
    }
    public static void getFavourites3(String userId, String type, String url, Context context, RecyclerView recyclerView) {
        ArrayList<FlowMeterStructure> incidenceArray = new ArrayList<>();

        // Crear una instancia de RequestQueue
        RequestQueue queue = Volley.newRequestQueue(context);

        // Realizar una solicitud JSON
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            final int finalIndex = response.length() - 1;

                            for (int i = 0; i < response.length(); i++) {
                                final int currentIndex = i;
                                JSONObject item = response.getJSONObject(i);

                                String urlSecundaria = "http://10.10.12.218:3000/flowMeter/meterId/"+item.getString("fav_id");
                                Log.d("com.example.traficoreto.fernando", urlSecundaria);

                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlSecundaria, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response2) {
                                                try {

                                                    // Obtener la lista de cámaras del objeto JSON
                                                    JSONArray camerasArray = response2.getJSONArray("features");

                                                    for (int i = 0; i < camerasArray.length(); i++) {
                                                        JSONObject cameraObject = camerasArray.getJSONObject(i);

                                                        if (cameraObject.has("properties")) {
                                                            Log.d("com.example.traficoreto.fernando", cameraObject.toString());

                                                            JSONObject propertiesObject = cameraObject.getJSONObject("properties");
                                                            if (item.getString("name").equals(propertiesObject.optString("description")))
                                                            {
                                                                String meterId = propertiesObject.optString("meterId", "");
                                                                String sourceId = propertiesObject.optString("sourceId", "");
                                                                String description = propertiesObject.optString("description", "");
                                                                String provinceId = propertiesObject.optString("provinceId", "");
                                                                String municipalityId = propertiesObject.optString("municipalityId", "");
                                                                String latitude = propertiesObject.optString("latitude", "");
                                                                String longitude = propertiesObject.optString("longitude", "");
                                                                String geometry = propertiesObject.optString("geometry", "");
                                                                int page = propertiesObject.optInt("page", 0);

                                                                FlowMeterStructure incidenceData = new FlowMeterStructure(meterId,sourceId,provinceId,municipalityId,description,latitude,longitude,geometry,page);
                                                                Log.d("com.example.traficoreto.fernando", description);

                                                                incidenceData.setFavourite(true);
                                                                incidenceArray.add(incidenceData);
                                                            }


                                                        }

                                                    }
                                                    if (currentIndex == finalIndex) {
                                                        //Log.d("com.example.traficoreto.fernando", "entro al condicional de curren final index");
                                                        visualizarDatos3(incidenceArray, recyclerView, context);
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    Log.d("com.example.traficoreto.fernando","error desde response2: "+e.toString());
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                // Manejar errores de la solicitud secundaria aquí
                                                error.printStackTrace();
                                            }
                                        });

                                // Añadir la solicitud secundaria a la cola
                                queue.add(jsonObjectRequest);
                            }
                        } catch (JSONException e) {
                            Log.d("com.example.traficoreto.fernando", "error: " + e.toString());
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud principal aquí
                        Log.d("com.example.traficoreto.fernando", String.valueOf(error));
                    }
                });
        queue.add(jsonArrayRequest);
    }
    private static CameraStructure procesarRespuestaSecundaria(JSONObject response) throws JSONException {
        String cameraId = response.has("cameraId") ? response.getString("cameraId") : "";
        String cameraName = response.has("cameraName") ? response.getString("cameraName") : "";
        String urlImage = response.has("urlImage") ? response.getString("urlImage") : "";
        String latitude = response.has("latitude") ? response.getString("latitude") : "";

        String longitude = response.has("longitude") ? response.getString("longitude") : "";
        String sourceId = response.has("sourceId") ? response.getString("sourceId") : "";
        return new CameraStructure(cameraId, cameraName,urlImage,latitude,longitude,sourceId,0);

        }

    private static void visualizarDatos(ArrayList<CameraStructure> cameraStructure, RecyclerView recyclerView, Context context) {
        Gson gson = new Gson();
        String json = gson.toJson(cameraStructure);
        CameraAdapter cameraAdapter = new CameraAdapter(cameraStructure,context,true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cameraAdapter);
    }
    private static IncidenceStructure procesarRespuestaSecundaria2(JSONObject response) throws JSONException {
        String incidenceId = response.has("incidenceId") ? response.getString("incidenceId") : "";
        String sourceId = response.has("sourceId") ? response.getString("sourceId") : "";
        String incidenceType = response.has("incidenceType") ? response.getString("incidenceType") : "";
        String autonomousRegion = response.has("autonomousRegion") ? response.getString("autonomousRegion") : "";
        String province = response.has("province") ? response.getString("province") : "";
        String cause = response.has("cause") ? response.getString("cause") : "";
        String startDate = response.has("startDate") ? response.getString("startDate") : "";
        String latitude = response.has("latitude") ? response.getString("latitude") : "";
        String longitude = response.has("longitude") ? response.getString("longitude") : "";
        String incidenceLevel = response.has("incidenceLevel") ? response.getString("incidenceLevel") : "";
        String direction = response.has("direction") ? response.getString("direction") : "";
        int page = response.has("page") ? response.getInt("page") : 0;

        // public IncidenceStructure(String incidenceId, String sourceId, String incidenceType, String autonomousRegion, String province, String cause, String startDate, String latitude, String longitude, String incidenceLevel, String direction, int page) {


        Log.d("com.example.traficoreto.fernando", incidenceType);

        return new IncidenceStructure(incidenceId, sourceId,incidenceType,autonomousRegion,province,cause,startDate,latitude,longitude,incidenceLevel,direction,page);
    }
    private static IncidenceStructure procesarRespuestaSecundaria3(JSONObject response) throws JSONException {
        String incidenceId = response.has("incidenceId") ? response.getString("incidenceId") : "";
        String incidenceType = response.has("incidenceType") ? response.getString("incidenceType") : "";
        Log.d("com.example.traficoreto.fernando", incidenceType);

        return new IncidenceStructure(incidenceId, incidenceType);
    }

    private static void visualizarDatos2(ArrayList<IncidenceStructure> cameraStructure, RecyclerView recyclerView, Context context) {
        IncidenceAdapter cameraAdapter = new IncidenceAdapter(cameraStructure,context,true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cameraAdapter);
    }
    private static void visualizarDatos3(ArrayList<FlowMeterStructure> cameraStructure, RecyclerView recyclerView, Context context) {
        FlowMeterAdapter cameraAdapter = new FlowMeterAdapter(cameraStructure,context,true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cameraAdapter);
    }


}


package com.example.traficoreto.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.traficoreto.LoginScreen;
import com.example.traficoreto.MapScreen;
import com.example.traficoreto.OnLoadMoreListener;
import com.example.traficoreto.R;
import com.example.traficoreto.helpers.HelperFuntions;
import com.example.traficoreto.structure.CameraStructure;
import com.example.traficoreto.structure.FlowMeterStructure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FlowMeterAdapter extends RecyclerView.Adapter<FlowMeterAdapter.FlowMeterAdapterViewHolder> implements OnLoadMoreListener {

    private ArrayList<FlowMeterStructure> dataList;
    private boolean isLoading = false;
    private boolean hasMoreData = true;
    public int numeroPagina = 1;
    private Context context;
    public Boolean isFavourite;
    private String url;
    String userId;

    public FlowMeterAdapter(ArrayList<FlowMeterStructure> dataList){
        this.dataList = dataList;
    }
    public FlowMeterAdapter(ArrayList<FlowMeterStructure> dataList,String url,Context context,String userId,Boolean isFavourite){
        this.dataList = dataList;
        this.context = context;
        this.userId = userId;
        this.isFavourite = isFavourite;
        this.url = url;
    }
    public FlowMeterAdapter(ArrayList<FlowMeterStructure> dataList,Context context,Boolean isFavourite){
        this.dataList = dataList;
        this.context = context;
        this.isFavourite = isFavourite;

    }
    @NonNull
    @Override
    public FlowMeterAdapter.FlowMeterAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new FlowMeterAdapter.FlowMeterAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlowMeterAdapter.FlowMeterAdapterViewHolder holder, int position) {
        FlowMeterStructure data = dataList.get(holder.getAdapterPosition());
        if (position == dataList.size() - 1&&!isFavourite) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onLoadMore();
                }
            });
        }
        holder.titleTextView.setText(data.getMeterId());
        holder.descriptionTextView.setText(data.getDescription());
        holder.setImage(data.getIcon());
        if (data.isFavourite()){
            holder.setImageFavourite();
        }else {
            holder.unsetImageFavourite();
        }
        holder.favouriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();

                if (!data.isFavourite())
                {
                    Log.d("fernando", "Image URL: " + data.isFavourite());
                    HelperFuntions.AddNewFavouriteToDB(url,userId,data.getMeterId(),data.getSourceId(),data.getDescription(),"flowmeter",data.getPage(),new RecyclerView(v.getContext()));
                    data.setFavourite(true);
                    notifyItemChanged(adapterPosition);
                }else {
                    data.setFavourite(false);
                    HelperFuntions.deleteFavourite(url,userId, "flowmeter",data.getMeterId(),data.getSourceId(),v.getContext());
                    notifyItemChanged(adapterPosition);
                }
            }
        });
        holder.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("fernando", "Image URL: " + data.isFavourite());
                // Obtener el contexto desde la vista
                Context context = v.getContext();
                // Aquí puedes usar el contexto para lanzar un Intent
                Intent intent = new Intent(context, MapScreen.class);
                intent.putExtra("datos", data);
                intent.putExtra("type", "sensor");
                //intent.putExtra("clave", valor);
                // Lanzar el Intent
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class FlowMeterAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        ImageView imageView,favouriteIcon;
        View clickView;

        public FlowMeterAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textView2);  // Reemplazar con el ID correcto
            descriptionTextView = itemView.findViewById(R.id.textView3);
            imageView = itemView.findViewById(R.id.imageView);
            favouriteIcon =  itemView.findViewById(R.id.imageView2);
            clickView = itemView.findViewById(R.id.view);


        }
        public void setImage(String imageUrl) {
            // Asigna la imagen al ImageView
            imageView.setImageResource(R.drawable.sensor);
        }
        public void setImageFavourite() {
            favouriteIcon.setImageResource(R.drawable.fav2);
        }
        public void unsetImageFavourite() {
            favouriteIcon.setImageResource(R.drawable.fav);
        }
    }
    @Override
    public void onLoadMore() {
        if (!isLoading && hasMoreData) {
            isLoading = true;
            Log.d("com.example.traficoreto.fernando","llego al final");

            String camera_url = "http://10.10.12.218:3000/flowMeter/"+(numeroPagina + 1);
            String favourite_url = "http://10.10.12.218:3000/"+"fav/"+userId+"/flowmeter";
            Log.d("com.example.traficoreto.fernando", camera_url);

            //Log.d("fernando",url);

            // Crear una instancia de RequestQueue
            RequestQueue queue = Volley.newRequestQueue(context);

            // URL de la API

            // Realizar una solicitud JSON
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, camera_url, null,
                    new Response.Listener<JSONObject>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onResponse(JSONObject response) {

                            JsonArrayRequest secondRequest = new JsonArrayRequest(Request.Method.GET, favourite_url, null,
                                    new Response.Listener<JSONArray>() {
                                        @Override
                                        public void onResponse(JSONArray secondResponse) {
                                            // Procesar la respuesta JSON aquí
                                            //  Log.d("fernando",String.valueOf(response));
                                            try {
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

                                                        FlowMeterStructure incidence = new FlowMeterStructure(meterId, sourceId, provinceId, municipalityId, description, latitude, longitude, "",currentPage);
                                                        try {
                                                            for (int j = 0; j < secondResponse.length(); j++) {

                                                                JSONObject jsonObject = secondResponse.getJSONObject(j);
                                                                int id2 = jsonObject.getInt("id");
                                                                int userId2 = jsonObject.getInt("user_id");
                                                                String type2 = jsonObject.getString("type");
                                                                String favId2 = jsonObject.getString("fav_id");
                                                                String sourceId2 = jsonObject.getString("sourceId");
                                                                int page2 = jsonObject.getInt("page");

                                                                if (favId2.equals(meterId) && page2 == currentPage) {
                                                                    incidence.setFavourite(true);
                                                                }
                                                            }
                                                        } catch (Exception e) {
                                                            Log.e("com.example.traficoreto.fernando", e.toString());

                                                        }
                                                        //cameraStructure.add(camera1);
                                                        dataList.add(incidence);
                                                    }

                                                }
                                                notifyDataSetChanged();
                                                numeroPagina++;
                                                isLoading = false;
                                                //CameraStructure camera1 = new CameraStructure("12", "domo", "Sadas", "sadas", "dasdas", "sadasad");
                                                //cameraStructure.add(camera1);
                                                //FlowMeterAdapter cameraAdapter = new FlowMeterAdapter(incidenceArray, urlRequest, context, userId, false);
                                                //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                                                //recyclerView.setLayoutManager(layoutManager);
                                                //recyclerView.setAdapter(cameraAdapter);
                                                // Log.d("fernando", "dasdada");

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                Log.d("fernando", e.toString());
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
                            isLoading = false;
                            hasMoreData = false;
                        }
                    });
            queue.add(jsonObjectRequest);
        }
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public boolean hasMoreData() {
        return hasMoreData;
    }

}

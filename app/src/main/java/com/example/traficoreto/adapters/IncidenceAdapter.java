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
import com.example.traficoreto.helpers.HelperFuntions;
import com.example.traficoreto.structure.CameraStructure;
import com.example.traficoreto.structure.IncidenceStructure;
import com.example.traficoreto.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class IncidenceAdapter extends RecyclerView.Adapter<IncidenceAdapter.IncidenceAdapterViewHolder> implements OnLoadMoreListener {
    private ArrayList<IncidenceStructure> dataList;
    private boolean isLoading = false;
    private boolean hasMoreData = true;
    public int numeroPagina = 1;
    private Context context;
    public Boolean isFavourite;
    private String url;
    String userId;

    public IncidenceAdapter(ArrayList<IncidenceStructure> dataList, String url, Context context, String userId, Boolean isFavourite){
        this.dataList = dataList;
        this.context = context;
        this.userId = userId;
        this.isFavourite = isFavourite;
        this.url = url;


    }
    public IncidenceAdapter(ArrayList<IncidenceStructure> dataList,Context context,Boolean isFavourite){
        this.dataList = dataList;
        this.context = context;
        this.isFavourite = isFavourite;

    }
    @NonNull
    @Override
    public IncidenceAdapter.IncidenceAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new IncidenceAdapter.IncidenceAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncidenceAdapter.IncidenceAdapterViewHolder holder, int position) {
        IncidenceStructure data = dataList.get(position);
        if (position == dataList.size() - 1&&!isFavourite) {
            Log.d("com.example.traficoreto.fernando", "entro al load more");
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onLoadMore();
                }
            });
        }
        holder.titleTextView.setText(data.incidenceId);
        holder.descriptionTextView.setText(data.getIncidenceType());
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
                   HelperFuntions.AddNewFavouriteToDB(url,userId,data.getIncidenceId(),data.getSourceId(),data.getIncidenceType(),"incidences",data.getPage(),new RecyclerView(v.getContext()));
                    Log.d("fernando", "Image URL: " + data.isFavourite());
                    data.setFavourite(true);
                    notifyItemChanged(adapterPosition);
                }else {
                    data.setFavourite(false);
                    HelperFuntions.deleteFavourite(url,userId, "incidences",data.getIncidenceId(),data.getSourceId(),v.getContext());
                    notifyItemChanged(adapterPosition);
                }
            }
        });
        holder.clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, MapScreen.class);
                intent.putExtra("datos", data);
                intent.putExtra("type", "incidence");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public static class IncidenceAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        ImageView imageView,favouriteIcon;
        View clickView;

        public IncidenceAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textView2);  // Reemplazar con el ID correcto
            descriptionTextView = itemView.findViewById(R.id.textView3);
            imageView = itemView.findViewById(R.id.imageView);
            favouriteIcon =  itemView.findViewById(R.id.imageView2);
            clickView = itemView.findViewById(R.id.view);
        }
        public void setImage(String imageUrl) {
            // Asigna la imagen al ImageView
            imageView.setImageResource(R.drawable.incidence);
        }
        public void setImageFavourite() {
            favouriteIcon.setImageResource(R.drawable.fav2);
        }
        public void unsetImageFavourite() {
            favouriteIcon.setImageResource(R.drawable.fav);
        }
    }
    //Method for page navigation
    @Override
    public void onLoadMore() {
        String incidences_url = "http://10.10.12.218:3000/incidences/"+(numeroPagina + 1);
        String favourite_url = "http://10.10.12.218:3000/"+"fav/"+userId+"/incidences";

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
                                                dataList.add(incidence);
                                            }
                                            notifyDataSetChanged();
                                            numeroPagina++;
                                            isLoading = false;
                                            //IncidenceAdapter cameraAdapter = new IncidenceAdapter(incidenceStructures,urlRequest,context,userId,false);
                                            //LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                                            //recyclerView.setLayoutManager(layoutManager);
                                            //recyclerView.setAdapter(cameraAdapter);
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

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public boolean hasMoreData() {
        return hasMoreData;
    }




}

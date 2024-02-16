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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.traficoreto.MapScreen;
import com.example.traficoreto.OnLoadMoreListener;
import com.example.traficoreto.helpers.HelperFuntions;
import com.example.traficoreto.helpers.MyPreferences;
import com.example.traficoreto.structure.CameraStructure;
import com.example.traficoreto.R;
import com.example.traficoreto.structure.UserFavouriteStructure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CameraAdapter extends RecyclerView.Adapter<CameraAdapter.CameraViewHolder> implements OnLoadMoreListener {

    private ArrayList<CameraStructure> dataList;
    private boolean isLoading = false;
    private boolean hasMoreData = true;
    public int numeroPagina = 1;
    private Context context;
    public Boolean isFavourite;
    private String url;
    String userId;
    public CameraAdapter(ArrayList<CameraStructure> dataList,String url,Context context,String userId,Boolean isFavourite){
        this.dataList = dataList;
        this.context = context;
        this.userId = userId;
        this.isFavourite = isFavourite;
        this.url = url;


    }
    public CameraAdapter(ArrayList<CameraStructure> dataList,Context context,Boolean isFavourite){
        this.dataList = dataList;
        this.context = context;
        this.isFavourite = isFavourite;

    }
    @NonNull
    @Override
    public CameraAdapter.CameraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new CameraAdapter.CameraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CameraAdapter.CameraViewHolder holder, int position) {
        CameraStructure data = dataList.get(holder.getAdapterPosition());
        if (position == dataList.size() - 1&&!isFavourite) {
            Log.d("com.example.traficoreto.fernando", "entro al load more");

            // Último elemento visible, cargar más datos si es necesario
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onLoadMore();
                }
            });
        }


        holder.titleTextView.setText(data.getCameraId());
        holder.descriptionTextView.setText(data.getCameraName());
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
                    HelperFuntions.AddNewFavouriteToDB(url,userId,data.getCameraId(),data.getSourceId(),data.getCameraName(),"cameras",data.getPage(),new RecyclerView(v.getContext()));
                    data.setFavourite(true);
                    notifyItemChanged(adapterPosition);
                }
                else
                {
                    data.setFavourite(false);
                    HelperFuntions.deleteFavourite(url,userId, "camera",data.getCameraId(),data.getSourceId(),v.getContext());
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
                intent.putExtra("type", "camera");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onLoadMore() {
        if (!isLoading && hasMoreData) {
            isLoading = true;

            String camera_url = url+"cameras/"+(numeroPagina + 1);
            String favourite_url = url+"fav/"+userId+"/cameras";

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

                                                    CameraStructure camera1 = new CameraStructure(cameraId, cameraName, urlImage, latitude, longitude, sourceId,currentPage);

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
                                                                //Log.d("com.example.traficoreto.fernando", "Son iguales");
                                                                camera1.setFavourite(true);
                                                            }
                                                        }
                                                    }catch (Exception e){
                                                        Log.e("com.example.traficoreto.fernando", e.toString());

                                                    }
                                                    dataList.add(camera1);

                                                }
                                                notifyDataSetChanged();
                                                numeroPagina++;
                                                isLoading = false;
                                                //CameraStructure camera1 = new CameraStructure("12", "domo", "Sadas", "sadas", "dasdas", "sadasad");
                                                //cameraStructure.add(camera1);
                                                /*CameraAdapter cameraAdapter = new CameraAdapter(cameraStructure,context,userId);
                                                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                                                recyclerView.setLayoutManager(layoutManager);
                                                recyclerView.setAdapter(cameraAdapter);*/


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                Log.d("com.example.traficoreto.fernando", e.toString());
                                            }


                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError secondError) {
                                            isLoading = false;
                                            hasMoreData = false;
                                            Log.e("com.example.traficoreto.fernando", "Error en la segunda solicitud: " + secondError.toString());

                                        }
                                    });

                            // Añadir la segunda solicitud a la cola de Volley
                            queue.add(secondRequest);
                            // Procesar la respuesta JSON aquí
                            //  Log.d("fernando",String.valueOf(response));


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            isLoading = false;
                            hasMoreData = false;
                            // Manejar errores de la solicitud aquí
                            Log.d("com.example.traficoreto.fernando", String.valueOf(error));

                        }
                    });

            // Añadir la solicitud a la cola
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


    public static class CameraViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        ImageView imageView,favouriteIcon;
        View clickView;

        public CameraViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textView2);  // Reemplazar con el ID correcto
            descriptionTextView = itemView.findViewById(R.id.textView3);
            imageView = itemView.findViewById(R.id.imageView);
            favouriteIcon =  itemView.findViewById(R.id.imageView2);
            clickView = itemView.findViewById(R.id.view);
        }
        public void setImage(String imageUrl) {
            imageView.setImageResource(R.drawable.camera);
        }
        public void setImageFavourite() {
            favouriteIcon.setImageResource(R.drawable.fav2);
        }
        public void unsetImageFavourite() {
            favouriteIcon.setImageResource(R.drawable.fav);
        }
    }

}

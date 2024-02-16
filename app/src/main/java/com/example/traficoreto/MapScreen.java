package com.example.traficoreto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.traficoreto.structure.CameraStructure;
import com.example.traficoreto.structure.FlowMeterStructure;
import com.example.traficoreto.structure.IncidenceStructure;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import android.os.Handler;

public class MapScreen extends AppCompatActivity implements OnMapReadyCallback , GoogleMap.InfoWindowAdapter{
    private GoogleMap mMap;
    private double latitud ;
    private double longitud ;
    private String itemName,cameraImage,sourceId,latitudeText,longitudeText;
    private Handler handler = new Handler();
    private Marker currentMarker;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_screen);

        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getStringExtra("type");
            if (type.equals("camera")){
                CameraStructure datos = (CameraStructure) intent.getSerializableExtra("datos");
                Log.d("com.example.traficoreto.fernando",type);

                if (datos != null) {
                    // Ahora puedes acceder a todos los métodos y atributos de TuDatos
                    String nombre = datos.getCameraName();
                    String edad = datos.getLatitude();
                    latitud = Double.parseDouble(datos.getLatitude());
                    longitud = Double.parseDouble(datos.getLongitude());
                    sourceId = datos.getSourceId();
                    cameraImage = datos.getUrlImage();

                    itemName = nombre;
                    // Realizar acciones con los datos recuperados
                }
            }
            if (type.equals("incidence")){
                IncidenceStructure datos = (IncidenceStructure) intent.getSerializableExtra("datos");
                Log.d("com.example.traficoreto.fernando",type);

                if (datos != null) {
                    // Ahora puedes acceder a todos los métodos y atributos de TuDatos
                    String nombre = datos.getCause();
                    String edad = datos.getLatitude();
                    latitud = Double.parseDouble(datos.getLatitude());
                    longitud = Double.parseDouble(datos.getLongitude());
                    sourceId = datos.getSourceId();
                    //cameraImage = datos.getUrlImage();
                    Log.d("com.example.traficoreto.fernando",String.valueOf(latitud));
                    itemName = nombre;
                    // Realizar acciones con los datos recuperados
                }
            }
            if (type.equals("sensor")){
                FlowMeterStructure datos = (FlowMeterStructure) intent.getSerializableExtra("datos");
                Log.d("com.example.traficoreto.fernando",type);

                if (datos != null) {
                    // Ahora puedes acceder a todos los métodos y atributos de TuDatos
                    String nombre = datos.getDescription();
                    String edad = datos.getLatitude();
                    latitud = Double.parseDouble(datos.getLatitude());
                    longitud = Double.parseDouble(datos.getLongitude());
                    sourceId = datos.getSourceId();
                    //cameraImage = datos.getUrlImage();
                    Log.d("com.example.traficoreto.fernando",String.valueOf(latitud));
                    itemName = nombre;
                    // Realizar acciones con los datos recuperados
                }
            }

        }


        // Obtiene el SupportMapFragment y notifica cuando el mapa está listo para ser usado
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }
    @Override
    public void onMapReady(@NonNull com.google.android.gms.maps.GoogleMap googleMap) {
        BitmapDescriptor icon = null;
        if(type.equals("camera")){
            Bitmap yourBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.camera);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(yourBitmap, 120, 120, false);
            icon = BitmapDescriptorFactory.fromBitmap(resizedBitmap);
        }
        if (type.equals("incidence")){
            Bitmap yourBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.incidence);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(yourBitmap, 120, 120, false);
            icon = BitmapDescriptorFactory.fromBitmap(resizedBitmap);
        }
        if (type.equals("sensor")){
            Bitmap yourBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sensor);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(yourBitmap, 120, 120, false);
            icon = BitmapDescriptorFactory.fromBitmap(resizedBitmap);
        }


        mMap = googleMap;
        // Agrega un marcador en la ubicación y mueve la cámara
        LatLng ubicacion = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(ubicacion).icon(icon));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ubicacion));
        mMap.setInfoWindowAdapter(this);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                currentMarker = marker;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (currentMarker != null && currentMarker.isInfoWindowShown()) {
                            currentMarker.hideInfoWindow();
                            currentMarker.showInfoWindow();
                        }
                    }
                }, 1000); // 1000 milisegundos = 1 segundo

                return false;
            }
        });
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        return null;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.custom_info_window, null);
        // Obtener el TextView del título
        TextView titleTextView = view.findViewById(R.id.titleTextView);
        TextView latitudeTextView = view.findViewById(R.id.textView4);
        TextView longitudeTextView = view.findViewById(R.id.textView5);
        TextView sourceTextview = view.findViewById(R.id.textView6);
        ImageView img = view.findViewById(R.id.imageView_details);
        int placeholderResourceId = R.drawable.loading;
        int error_placeholder = R.drawable.nocamera;
        switch(sourceId) {
            case "1":
                sourceId="Gobierno País Vasco";
                break;
            case "2":
                sourceId="Diputación Foral de Bizkaia";
                break;
            case "3":
                sourceId="Diputación Foral de Álava";
                break;
            case "4":
                sourceId="Diputación Foral de Gipuzkoa";
                break;
            case "5":
                sourceId="Ayuntamiento Bilbao";
                break;
            case "6":
                sourceId="Ayuntamiento Vitoria-Gasteiz";
                break;
            case "7":
                sourceId="Ayuntamiento de Donostia-San Sebastián";
                break;
            default:
                // code block
        }


        if (cameraImage != null && !cameraImage.isEmpty()) {
        Picasso.get()
                .load(cameraImage)
                .placeholder(placeholderResourceId)
                .error(error_placeholder)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        img.setImageBitmap(bitmap);
                    }
                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        //Log.d("com.example.traficoreto.fernando",errorDrawable.toString());
                        img.setImageDrawable(errorDrawable);
                    }
                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        img.setImageDrawable(placeHolderDrawable);
                    }
                });

        } else {
            img.setImageResource(R.drawable.nocamera);

        }
        titleTextView.setText(itemName);
        latitudeTextView.setText(String.valueOf(latitud));
        longitudeTextView.setText(String.valueOf(longitud));
        sourceTextview.setText(sourceId);
        return view;
    }
}
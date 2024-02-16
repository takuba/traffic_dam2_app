package com.example.traficoreto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.traficoreto.adapters.CameraAdapter;
import com.example.traficoreto.helpers.AlertDialogHelper;
import com.example.traficoreto.helpers.HelperFuntions;
import com.example.traficoreto.helpers.FlowMeterHelper;
import com.example.traficoreto.helpers.IncidencesHelper;
import com.example.traficoreto.helpers.MyPreferences;
import com.example.traficoreto.structure.CameraStructure;

import java.util.ArrayList;
import java.util.List;

public class ListScreen extends AppCompatActivity  implements OnLoadMoreListener {
    private RecyclerView recyclerView;
    private RadioGroup elements;
    private TextView exam;
    private String variable;
    private ArrayList<CameraStructure> cameraStructure = new ArrayList<>();
    private CameraAdapter cameraAdapter;
    private int numeroPagina = 1;
    private boolean isLoading = false;
    private Button maps,settings,userInfo;
    String urlRequest,userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_screen);

        recyclerView = findViewById(R.id.recyvlerview);
        ImageView filter = findViewById(R.id.imageView3);
        urlRequest =  MyPreferences.getUrlRequest(ListScreen.this);
        userId = MyPreferences.getUserId(getApplicationContext());
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] items = {getString(R.string.app_item_camera), getString(R.string.app_item_incidences), getString(R.string.app_item_meter)};
                AlertDialogHelper.showCheckboxDialog(ListScreen.this, getString(R.string.app_fav_title), items,  recyclerView,ListScreen.this,urlRequest,userId);
            }
        });
        Spinner spinner = findViewById(R.id.spinner);

        List<String> categories = new ArrayList<>();
        categories.add(getString(R.string.app_item_camera));
        categories.add(getString(R.string.app_item_incidences));
        categories.add(getString(R.string.app_item_meter));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedOption = (String) parentView.getItemAtPosition(position);
                switch(selectedOption){
                    case "Cameras":
                    case "Camaras":
                        HelperFuntions.getAllItem("cameras","1",recyclerView,urlRequest,getApplicationContext(),userId);
                        break;
                    case "Incidences":
                    case "Incidencias":
                        IncidencesHelper.getAllItem("incidences","1",recyclerView,urlRequest,getApplicationContext(),userId);
                        break;
                    case "Flow Meter":
                    case "Sensores":
                        FlowMeterHelper.getAllItem("flowMeter","1",recyclerView,urlRequest,getApplicationContext(),userId);

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Acciones a realizar cuando no se selecciona nada
            }
        });

        settings = findViewById(R.id.setting_btn);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListScreen.this, SettingScreen.class);
                startActivity(intent);
            }
        });

        userInfo = findViewById(R.id.user_btn);
        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ListScreen.this, UserScreen.class);
                startActivity(intent2);
            }
        });

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public boolean hasMoreData() {
        return false;
    }




}
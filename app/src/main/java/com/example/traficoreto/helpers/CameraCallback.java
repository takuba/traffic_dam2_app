package com.example.traficoreto.helpers;

import com.example.traficoreto.structure.CameraStructure;

import java.util.ArrayList;

public interface CameraCallback {
    void onCameraListReceived(ArrayList<CameraStructure> cameraList);
}

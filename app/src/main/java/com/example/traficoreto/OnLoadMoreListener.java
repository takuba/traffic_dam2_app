package com.example.traficoreto;

import com.example.traficoreto.structure.CameraStructure;

import java.util.ArrayList;
import java.util.List;

public interface OnLoadMoreListener {
    void onLoadMore();
    boolean isLoading();
    boolean hasMoreData();

}

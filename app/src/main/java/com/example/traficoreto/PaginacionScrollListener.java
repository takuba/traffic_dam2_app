package com.example.traficoreto;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PaginacionScrollListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager layoutManager;
    private OnLoadMoreListener onLoadMoreListener;

    public PaginacionScrollListener(LinearLayoutManager layoutManager, OnLoadMoreListener onLoadMoreListener) {
        this.layoutManager = layoutManager;
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!onLoadMoreListener.isLoading() && onLoadMoreListener.hasMoreData()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                onLoadMoreListener.onLoadMore();
            }
        }
    }
}


package com.vedangj044.statusview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewCountBottomSheet extends BottomSheetDialogFragment {

    private List<ViewCountObject> mDataset = new ArrayList<>();
    private onDismissEvent listener;

    public interface onDismissEvent{
        void onDismissE();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_count_bottom_sheet, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.view_count_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mDataset.add(new ViewCountObject("https://i.pinimg.com/474x/bc/d4/ac/bcd4ac32cc7d3f98b5e54bde37d6b09e.jpg", "Vedang Joshi", "12:47"));
        mDataset.add(new ViewCountObject("https://i.pinimg.com/474x/bc/d4/ac/bcd4ac32cc7d3f98b5e54bde37d6b09e.jpg", "Vedang Joshi", "12:47"));
        mDataset.add(new ViewCountObject("https://i.pinimg.com/474x/bc/d4/ac/bcd4ac32cc7d3f98b5e54bde37d6b09e.jpg", "Vedang Joshi", "12:47"));
        mDataset.add(new ViewCountObject("https://i.pinimg.com/474x/bc/d4/ac/bcd4ac32cc7d3f98b5e54bde37d6b09e.jpg", "Vedang Joshi", "12:47"));
        mDataset.add(new ViewCountObject("https://i.pinimg.com/474x/bc/d4/ac/bcd4ac32cc7d3f98b5e54bde37d6b09e.jpg", "Vedang Joshi", "12:47"));

        TextView viewCount = view.findViewById(R.id.view_count_text);
        viewCount.setText(String.valueOf(mDataset.size()));

        ViewCountAdapter vAdapter = new ViewCountAdapter(mDataset);

        recyclerView.setAdapter(vAdapter);

        return view;
    }

    public void setOnDismissEvent(onDismissEvent listener){
        this.listener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listener.onDismissE();
    }

    public void setmDataset(List<ViewCountObject> mDataset) {
        this.mDataset = mDataset;
    }
}

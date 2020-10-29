package com.vedangj044.statusview.Stickers;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.vedangj044.statusview.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MyStickersFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyStickerRecyclerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sticker_recycler_view, container, false);

        recyclerView = view.findViewById(R.id.sticker_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        StickerDatabase stickerDatabase = StickerDatabase.getInstance(view.getContext());

        mAdapter = new MyStickerRecyclerAdapter(view.getContext(), this);

        stickerDatabase.stickerCategoryDAO().getStickerCategory().observe(this, new Observer<List<StickerCategoryModel>>() {
            @Override
            public void onChanged(List<StickerCategoryModel> allStickerModels) {
                mAdapter.mDataset = allStickerModels;
                mAdapter.notifyDataSetChanged();


            }
        });




        recyclerView.setAdapter(mAdapter);

        return view;
    }


}


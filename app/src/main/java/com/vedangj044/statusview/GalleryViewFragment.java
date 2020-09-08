package com.vedangj044.statusview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vedangj044.statusview.Adapters.GalleryImageAdapter;
import com.vedangj044.statusview.R;

public class GalleryViewFragment extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery_fragment, container, false);
        recyclerView = view.findViewById(R.id.gallery_recycle);

        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 6));

        GalleryImageAdapter grp = new GalleryImageAdapter(view.getContext(), 0);
        recyclerView.setAdapter(grp);

        return view;
    }
}

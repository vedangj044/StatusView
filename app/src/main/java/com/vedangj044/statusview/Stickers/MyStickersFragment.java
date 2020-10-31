package com.vedangj044.statusview.Stickers;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.vedangj044.statusview.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MyStickersFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView noStickerTextView;
    private MyStickerRecyclerAdapter mAdapter;
    private ExecutorService executor = MyStickerRecyclerAdapter.ExecutorHelper.getInstanceExecutor();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sticker_recycler_view, container, false);

        recyclerView = view.findViewById(R.id.sticker_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        noStickerTextView = view.findViewById(R.id.no_sticker_text);

        StickerDatabase stickerDatabase = StickerDatabase.getInstance(view.getContext());

        mAdapter = new MyStickerRecyclerAdapter(view.getContext(), this);

        stickerDatabase.stickerCategoryDAO().getStickerCategory().observe(this, new Observer<List<StickerCategoryModel>>() {
            @Override
            public void onChanged(List<StickerCategoryModel> allStickerModels) {

                if(allStickerModels.size() == 0){
                    noStickerTextView.setVisibility(View.VISIBLE);
                }
                else{
                    noStickerTextView.setVisibility(View.GONE);
                }

                for(StickerCategoryModel st : allStickerModels){
                    if(!mAdapter.mDataset.contains(st)){

                        Future<Void> populate = executor.submit(new Callable<Void>() {
                            @Override
                            public Void call() throws Exception {

                                List<String> urls =  stickerDatabase.stickerImageDAO().getStickerURLById(st.getId());

                                List<StickerModel> obj = new ArrayList<>();
                                for(String url : urls){
                                    obj.add(new StickerModel(url));
                                }

                                st.setImages(obj);


                                return null;
                            }
                        });

                    }
                }
                mAdapter.mDataset = allStickerModels;
                mAdapter.notifyDataSetChanged();

            }
        });




        recyclerView.setAdapter(mAdapter);

        return view;
    }

    public static class ExecutorHelper{

        private static ExecutorService instanceExecutor;

        public static synchronized ExecutorService getInstanceExecutor(){
            if(instanceExecutor == null){
                instanceExecutor = Executors.newSingleThreadExecutor();
            }
            return instanceExecutor;
        }
    }

}


package com.vedangj044.statusview.Stickers;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.vedangj044.statusview.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MyStickerRecyclerAdapter extends RecyclerView.Adapter<MyStickerRecyclerAdapter.ViewHolder> {

    public List<StickerCategoryModel> mDataset = new ArrayList<>();
    private Context context;
    private LifecycleOwner lifecycleOwner;
    private StickerDatabase stickerDatabase;

    private ExecutorService executor = AllStickerRecyclerAdapter.ExecutorHelper.getInstanceExecutor();


    public MyStickerRecyclerAdapter(Context context, LifecycleOwner lifecycleOwner) {
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        this.stickerDatabase = StickerDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_card_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        StickerCategoryModel arr = mDataset.get(position);
        holder.deleteIcon.setImageResource(R.drawable.camera_delete_foreground);

        holder.titleTextView.setText(arr.getName());

        holder.titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.context.getApplicationContext(), FullStickerPack.class);

                List<String> urls = new ArrayList<>();
                for(StickerModel model: arr.getImages()){
                    urls.add(model.getImage());
                }

                intent.putStringArrayListExtra("urllist", (ArrayList<String>) urls);
                intent.putExtra("title", arr.getName());
                holder.context.startActivity(intent);
            }
        });
        
        List<ImageView> imageObject = new ArrayList<>();
        imageObject.add(holder.icon1);
        imageObject.add(holder.icon2);
        imageObject.add(holder.icon3);
        imageObject.add(holder.icon4);
        imageObject.add(holder.icon5);

        stickerDatabase.stickerImageDAO().getFullStickerByID(arr.getId()).observe(lifecycleOwner, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {

                List<StickerModel> models = new ArrayList<>();
                for(String s: strings){
                    models.add(new StickerModel(s));
                }
                arr.setImages(models);

                CountDownTimer ctx = new CountDownTimer(2000, 2000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        for(int i = 0; i < Math.min(5, strings.size()); i++){

                            File file = new File(strings.get(i));
                            if(file.exists()){
                                Glide.with(holder.context).load(strings.get(i))
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(imageObject.get(i));
                            }
                            else{

                                Future<Void> del = executor.submit(new Callable<Void>() {
                                    @Override
                                    public Void call() throws Exception {
                                        stickerDatabase.stickerCategoryDAO().deleteStickerCategory(arr);
                                        return null;
                                    }
                                });

                                break;
                            }

                        }
                    }
                };

                for(int i = 0; i < Math.min(5, strings.size()); i++){

                    File file = new File(strings.get(i));
                    if(file.exists()){
                        Glide.with(holder.context).load(strings.get(i))
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(imageObject.get(i));
                    }
                    else{
                        ctx.start();
                        break;
                    }

                }

            }
        });

        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(ImageView v1: imageObject){
                    v1.setImageResource(0);
                }

                Future<Void> delete = executor.submit(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {

                        for(StickerModel imageURL: arr.getImages()){
                            File file = new File(imageURL.getImage());
                            file.delete();
                        }

                        stickerDatabase.stickerCategoryDAO().deleteStickerCategory(arr);

                        return null;
                    }
                });

                try {
                    delete.get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView titleTextView;
        private TextView sizeTextView;

        private ImageView icon1;
        private ImageView icon2;
        private ImageView icon3;
        private ImageView icon4;
        private ImageView icon5;

        private ImageView deleteIcon;

        private Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.sticker_pack_title);
            sizeTextView = itemView.findViewById(R.id.sticker_pack_size);

            icon1 = itemView.findViewById(R.id.sticker_icon_1);
            icon2 = itemView.findViewById(R.id.sticker_icon_2);
            icon3 = itemView.findViewById(R.id.sticker_icon_3);
            icon4 = itemView.findViewById(R.id.sticker_icon_4);
            icon5 = itemView.findViewById(R.id.sticker_icon_5);

            deleteIcon = itemView.findViewById(R.id.sticker_download_state);

            context = itemView.getContext();
        }
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

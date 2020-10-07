package com.vedangj044.statusview.Stickers;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.vedangj044.statusview.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AllStickerRecyclerAdapter extends RecyclerView.Adapter<AllStickerRecyclerAdapter.ViewHolder> {

    private List<AllStickerModel> mDataset;
    private List<Integer> already;
    private StickerDatabase stickerDatabase;
    private Context mContext;

    public AllStickerRecyclerAdapter(Context context, List<AllStickerModel> mDataset) {
        this.mDataset = mDataset;
        this.stickerDatabase = StickerDatabase.getInstance(context);
        this.mContext = context;
        this.already = stickerDatabase.stickerCategoryDAO().getAllStickerId();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_card_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AllStickerModel arr = mDataset.get(position);

        holder.titleTextView.setText(arr.getName());

        holder.titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getApplicationContext(), FullStickerPack.class);
                intent.putStringArrayListExtra("urllist", (ArrayList<String>) arr.getImages());
                intent.putExtra("title", arr.getName());
                mContext.startActivity(intent);
            }
        });
        holder.downloadIcon.setImageResource(R.drawable.sticker_downlad_foreground);

        List<ImageView> imageObject = new ArrayList<>();
        imageObject.add(holder.icon1);
        imageObject.add(holder.icon2);
        imageObject.add(holder.icon3);
        imageObject.add(holder.icon4);
        imageObject.add(holder.icon5);

        for(int i = 0; i < Math.min(5, arr.getImages().size()); i++){
            Glide.with(holder.context).load(arr.getImages().get(i))
                    .into(imageObject.get(i));

        }

        if(already.contains(arr.getId())){
            holder.downloadIcon.setImageResource(R.drawable.sticker_already_download_foreground);
        }
        else{

            holder.downloadIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AllStickerModel stm = new AllStickerModel(arr.getId(), arr.getName(), arr.getLogo(), arr.getStatus());

                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            stickerDatabase.stickerCategoryDAO().insertStickerCategory(stm);

                            for(String url: arr.getImages()){

                                String filename = getFileName();
                                File path = new File(holder.context.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());

//                                downloadFile(url, path, filename);
                                Glide.with(holder.context)
                                        .asBitmap()
                                        .thumbnail(0.5f)
                                        .load(url)
                                        .into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                saveImage(resource, filename, path);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });
                                stickerDatabase.stickerImageDAO().insertStickerImages(new StickerImageModel(arr.getId(), path.getAbsolutePath()+"/"+filename));
                            }

                        }
                    });

                    holder.downloadIcon.setImageResource(R.drawable.sticker_already_download_foreground);
                }
            });

        }
    }

//    public void downloadFile(String uRl, File path, String name) {
//        File direct = path;
//        File gh = new File(direct + "/" + name);
//        if (!direct.exists()) {
//            direct.mkdirs();
//        }
//
//        DownloadManager mgr = (DownloadManager) this.mContext.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
//
//        Uri downloadUri = Uri.parse(uRl);
//        DownloadManager.Request request = new DownloadManager.Request(
//                downloadUri);
//
//        request.setDestinationUri(Uri.parse(direct.getAbsolutePath()));
//
//        mgr.enqueue(request);
//    }

    private void saveImage(Bitmap resource, String filename, File path) {
        boolean success = true;
        if (!path.exists()) {
            success = path.mkdirs();
        }
        if (success) {
            File imageFile = new File(path, filename);
            String savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                resource.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
                Log.v("ala", savedImagePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getFileName() {

        Random r = new Random();
        String alphabet = String.valueOf((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));

        String name = "STICKER__" +
                new SimpleDateFormat("yyyyMMdd_HHmmss_", Locale.US).format(new Date()) +
                alphabet + ".png";

        return name;
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

        private ImageView downloadIcon;

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

            downloadIcon = itemView.findViewById(R.id.sticker_download_state);

            context = itemView.getContext();
        }
    }
}

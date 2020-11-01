package com.vedangj044.statusview.Stickers;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.badoo.mobile.util.WeakHandler;
import com.bumptech.glide.Glide;
import com.vedangj044.statusview.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AllStickerRecyclerAdapter extends RecyclerView.Adapter<AllStickerRecyclerAdapter.ViewHolder> {

    private List<StickerCategoryModel> mDataset;
    private StickerDatabase stickerDatabase;
    private Context mContext;
    private ExecutorService executor = ExecutorHelper.getInstanceExecutor();
    private LifecycleOwner lifecycleOwner;

    public AllStickerRecyclerAdapter(Context context, List<StickerCategoryModel> mDataset, LifecycleOwner lifecycleOwner) {
        this.mDataset = mDataset;
        this.stickerDatabase = StickerDatabase.getInstance(context);
        this.mContext = context;
        this.lifecycleOwner = lifecycleOwner;
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
        Log.v("a", ""+arr.getId());

        holder.titleTextView.setText(arr.getName());

        holder.titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getApplicationContext(), FullStickerPack.class);

                List<String> urls = new ArrayList<>();
                for(StickerModel model: arr.getImages()){
                    urls.add(model.getImage());
                }

                intent.putStringArrayListExtra("urllist", (ArrayList<String>) urls);
                intent.putExtra("title", arr.getName());
                mContext.startActivity(intent);
            }
        });



        List<ImageView> imageObject = new ArrayList<>();
        imageObject.add(holder.icon1);
        imageObject.add(holder.icon2);
        imageObject.add(holder.icon3);
        imageObject.add(holder.icon4);
        imageObject.add(holder.icon5);

        for(int i = 0; i < Math.min(5, arr.getImages().size()); i++){
            Glide.with(holder.context).load(arr.getImages().get(i).getImage())
                    .into(imageObject.get(i));

        }

        stickerDatabase.stickerImageDAO().getCountOfStickerByCategoryID(arr.getId()).observe(lifecycleOwner, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                if(arr.getImages().size() == integer){
                    holder.downloadIcon.setImageResource(R.drawable.sticker_already_download_foreground);
                }

                if(arr.getImages().size() > integer && integer > 0){
                    holder.downloadIcon.setImageResource(R.drawable.ic_baseline_system_update_alt_24);

                    Future<Void> task = executor.submit(new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {

                            List<String> alreadyDownloadedStickers = stickerDatabase.stickerImageDAO().getAllStickerId(arr.getId());

                            for(StickerModel models: arr.getImages()){
                                if(!alreadyDownloadedStickers.contains(models.getCode())){

                                    String filename = getFileName();
                                    File path = new File(holder.context.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());

                                    downloadFile(models.getImage(), path, filename);
                                    stickerDatabase.stickerImageDAO().insertStickerImages(new StickerModel(models.getCode(), models.getStickerCategoryId(), path.getAbsolutePath()+"/"+filename));

                                }
                            }

                            return null;
                        }
                    });

                    try {
                        task.get();
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                if(integer == 0){
                    holder.downloadIcon.setImageResource(R.drawable.sticker_downlad_foreground);
                    holder.downloadIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            StickerCategoryModel stm = new StickerCategoryModel(arr.getId(), arr.getName(), arr.getLogo(), arr.getStatus());

                            holder.isDownloading.setVisibility(View.VISIBLE);
                            holder.downloadIcon.setVisibility(View.GONE);

                            File path = new File(holder.context.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
                            final String[] lastFileName = {""};

                            Future<Void> task = executor.submit(new Callable<Void>() {
                                @Override
                                public Void call() throws Exception {

                                    stickerDatabase.stickerCategoryDAO().insertStickerCategory(stm);


                                    for(StickerModel url: arr.getImages()) {

                                        String filename = getFileName();

                                        downloadFile(url.getImage(), path, filename);
                                        stickerDatabase.stickerImageDAO().insertStickerImages(new StickerModel(url.getCode(), url.getStickerCategoryId(), path.getAbsolutePath() + "/" + filename));

                                        lastFileName[0] = filename;
                                    }


                                    holder.downloadIcon.setImageResource(R.drawable.sticker_already_download_foreground);

                                    return null;
                                }
                            });

                            DownladCompleteCheck(holder, path.getAbsolutePath()+"/"+lastFileName[0]);



                        }
                    });

                }

            }
        });

    }

    private static void DownladCompleteCheck(ViewHolder holder, String s) {



    }


    public void downloadFile(String uRl, File path, String name) {
        File direct = path;
        String gh = "file://" + direct.getAbsolutePath() + "/" + name;
        if (!direct.exists()) {
            direct.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) this.mContext.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setDestinationUri(Uri.parse(gh));

        mgr.enqueue(request);

    }

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

        String name = "STICKER_" +
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

        private ProgressBar isDownloading;

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
            isDownloading = itemView.findViewById(R.id.is_download);

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

package com.vedangj044.statusview.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.CancellationSignal;
import android.provider.MediaStore;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.RecyclerView;

import com.vedangj044.statusview.R;
import com.vedangj044.statusview.Activity.UploadActivity;

import java.io.File;
import java.io.IOException;

public class GalleryImageAdapter extends RecyclerView.Adapter<GalleryImageAdapter.ViewHolder> {

    private String[] arrPass;
    private int Count = 100;
    private Context mContext;

    public GalleryImageAdapter(Context context, int count) {
        Count = count;
        this.mContext = context;
        this.arrPass = populateThumbnail();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_image_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Bitmap bmp =  ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(arrPass[position]), 128 ,128);;

        if(arrPass[position].endsWith("mp4")){
            bmp = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(arrPass[position]), 128 ,128);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                try {
                    bmp =  ThumbnailUtils.createVideoThumbnail(new File(arrPass[position]), new Size(128, 128), new CancellationSignal());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                bmp =  ThumbnailUtils.createVideoThumbnail(arrPass[position], MediaStore.Video.Thumbnails.MICRO_KIND);
            }
        }

        holder.imageView.setImageBitmap(bmp);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UploadActivity.class);
                intent.putExtra("image", arrPass[position]);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrPass.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.thumbnail_image);
        }
    }

    public String[] populateThumbnail(){
        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media._ID;
        //Stores all the images from the gallery in Cursor

        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

        CursorLoader cursorLoader = new CursorLoader(mContext, MediaStore.Files.getContentUri("external"),
                columns,
                selection,
                null,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC");

        Cursor cursor = cursorLoader.loadInBackground();

        //Total number of images
        int count = 100;
        if(Count == 0){
            count = cursor.getCount();
        }

        //Create an array to store path to all the images
        String[] arrPath = new String[count];

        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            //Store the path of the image
            arrPath[i]= cursor.getString(dataColumnIndex);
        }
        // The cursor should be freed up after use with close()
        cursor.close();

        return arrPath;
    }
}

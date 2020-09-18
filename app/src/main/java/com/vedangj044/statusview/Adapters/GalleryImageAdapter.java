package com.vedangj044.statusview.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.CancellationSignal;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vedangj044.statusview.R;
import com.vedangj044.statusview.Activity.UploadActivity;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class GalleryImageAdapter extends RecyclerView.Adapter<GalleryImageAdapter.ViewHolder> {

    // Loads URI of all images fetched from the MediaStore
    private String[] arrPass;

    private int Count = 100;
    private Context mContext;

    // Array holds the state of all images either selected or not selected
    private Boolean[] selectedState;

    // URI of all the selected images or videos
    private List<String> selectionPath;

    // Cache Limit Only recent 100 images are cached
    private final int CacheLimit = 100;

    // Maximum number of media that can be selected at once
    private final int MaxSelectionLimit = 5;

    public GalleryImageAdapter(Context context, int count) {

        /*
        * Count variable is set either to 0 or 100
        * Based on the value of count variable we confirm where this adapter
        * is called from.
        *
        * IF COUNT == 100 -> this adapter is called from CameraActivity thus only 100 most recent
        * images are fetched
        *
        * IF COUNT == 0 -> this adapter is called from GalleryViewFragment thus all the Media
        * is fetched
        * */

        Count = count;
        this.mContext = context;
        this.selectionPath = new ArrayList<>();
        this.arrPass = populateThumbnail();
    }

    private notifyImageDate listener;

    // set listened to this instance
    public void setOnNotifyImageData(notifyImageDate listener){
        this.listener = listener;
    }


    // This listener notifies the GalleryViewFragment of
    // The month of the current image displayed in recyclerView
    // The List of selected images
    public interface notifyImageDate{
        void sendSignal(String text);
        void sendSelectedItem(List<String> s);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        // Based on the count variable Layout is decided
        if(Count == 100){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_image_card, parent, false);
        }
        else{
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_image_card_large, parent, false);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Month - When the image was captured calculated from path and sent back to GalleryViewFragment
        String timePeriod = calculateTimePeriod(arrPass[position]);
        listener.sendSignal(timePeriod);

        // If the position index exceeds the CacheLimit then image is not cached
        if(position > CacheLimit){
            Glide.with(mContext).load(arrPass[position])
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.imageView);
        }
        else{
            Glide.with(mContext).load(arrPass[position]).into(holder.imageView);
        }


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // When any media is already selected then others can be selected just by clicking
                // or long press
                if(selectionPath.size() > 0){
                    setState(position, holder.isSelected);
                }
                else {
                    // When no media is selected then path of the image is sent to the UploadActivity
                    Intent intent = new Intent(mContext, UploadActivity.class);
                    List<String> itemList = new ArrayList<>();
                    itemList.add(arrPass[position]);
                    intent.putStringArrayListExtra("imageList", (ArrayList<String>) itemList);
                    mContext.startActivity(intent);
                }

            }
        });

        // LongClick to select media
        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(Count == 100){
                    // When called from CameraActivity
                    Toast.makeText(mContext, "Select Multiple by swiping up", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return setState(position, holder.isSelected);
            }
        });
    }

    // Function changes the styling according to the state (selected or not selected) of the ImageView
    private boolean setState(int position, ImageView isSelected){

        // When this position is not selected in the selectedState Array
        if(!selectedState[position]){

            // Number of selections is more than max
            if(selectionPath.size() == MaxSelectionLimit){
                Toast.makeText(mContext, "Can't select more than 5", Toast.LENGTH_SHORT).show();
                return true;
            }

            // path of the current Image is added to the List
            selectionPath.add(arrPass[position]);

            // A tick is displayed over the image
            isSelected.setImageResource(R.drawable.ic_tick_foreground);

            // opacity is set to 60%
            isSelected.setBackgroundColor(Color.parseColor("#99000000"));
        }
        else{

            // path is removed from the selectionPath array
            selectionPath.remove(arrPass[position]);

            // Styling is restored
            isSelected.setImageResource(0);
            isSelected.setBackgroundResource(0);

        }

        // The GalleryViewFragment is notified of all the changes in selectionPath array
        listener.sendSelectedItem(selectionPath);

        // state is toggled in the selectedState Array
        selectedState[position] = !selectedState[position];
        return true;
    }


    // Calculate the Month of capture of the Media from its path
    private String calculateTimePeriod(String arrPass) {

        Calendar cal = Calendar.getInstance();

        File s = new File(arrPass);
        cal.setTimeInMillis(s.lastModified());
        return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    }

    @Override
    public int getItemCount() {
        return arrPass.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private ImageView isSelected;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.thumbnail_image);
            isSelected = itemView.findViewById(R.id.selected_tick);
        }
    }

    // Path of all the images are fetched from the MediaStore and stored in argPath
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
            selectedState = new Boolean[count];
            Log.v("Askce", String.valueOf(selectedState.length));
            Arrays.fill(selectedState, false);
        }
        else{
            selectedState = new Boolean[100];
            Arrays.fill(selectedState, false);
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

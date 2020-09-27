package com.vedangj044.statusview.Stickers;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.vedangj044.statusview.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public class AllStickerFragment extends Fragment {

    public static String getBasicAuthenticationString() {
        String basicAuth = "";
        String baseData = basicAuthUsername + ":" + basicAuthPassword;
        basicAuth = "Basic " + Base64.encodeToString(baseData.getBytes(), Base64.NO_WRAP);
        return basicAuth;
    }

    // Basic authentication UserName and password
    public static final String basicAuthUsername = "admin";
    public static final String basicAuthPassword = "password";

    public interface StickerApiHolder{
        @POST("stickerCategory/getAllStickerCategory")
        Call<StickerApiModel> getStickers(@Header("Authorization") String authHeader, @Body JsonObject body);
    }


    private RecyclerView recyclerView;
    private List<AllStickerModel> mDataset = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sticker_recycler_view, container, false);


        recyclerView = view.findViewById(R.id.sticker_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://voip.vortexvt.com:8082/ItHub/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        StickerApiHolder stickerApiHolder = retrofit.create(StickerApiHolder.class);

        JsonObject body = new JsonObject();
        body.addProperty( "username", "000000012506");


        Call<StickerApiModel> call = stickerApiHolder.getStickers(getBasicAuthenticationString(), body);

        call.enqueue(new Callback<StickerApiModel>() {
            @Override
            public void onResponse(Call<StickerApiModel> call, Response<StickerApiModel> response) {
                if(!response.isSuccessful()){
                    return;
                }
                Toast.makeText(view.getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                StickerApiModel arr1 = response.body();
                mDataset = arr1.getData();

                recyclerView.setAdapter(new AllStickerRecyclerAdapter(view.getContext(), mDataset));
            }

            @Override
            public void onFailure(Call<StickerApiModel> call, Throwable t) {
                Toast.makeText(view.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}

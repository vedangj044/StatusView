package com.vedangj044.statusview.Stickers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vedangj044.statusview.R;

import java.util.ArrayList;
import java.util.List;

public class AllStickerFragment extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sticker_recycler_view, container, false);

        recyclerView = view.findViewById(R.id.sticker_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        List<AllStickerModel> arr = new ArrayList<>();

        List<String> pack1 = new ArrayList<>();
        pack1.add("https://voip.vortexvt.com:8082/ithubfiles/sticker/advVort/1599119206812/images/15991197464015.png");
        pack1.add("https://voip.vortexvt.com:8082/ithubfiles/sticker/advVort/1599119206812/images/15991198925573.png");
        pack1.add("https://voip.vortexvt.com:8082/ithubfiles/sticker/advVort/1599119206812/images/15991198349162.png");
        pack1.add("https://voip.vortexvt.com:8082/ithubfiles/sticker/advVort/1599119206812/images/15991196856461.png");
        arr.add(new AllStickerModel("Mini & Minnie", "https://voip.vortexvt.com:8082/ithubfiles/sticker/advVort/1599119206812/4.png", pack1));


        List<String> pack2 = new ArrayList<>();
        pack2.add("https://voip.vortexvt.com:8082/ithubfiles/sticker/advVort/1599119289252/images/15991200601322.png");
        arr.add(new AllStickerModel("Tom & Jerry", "https://voip.vortexvt.com:8082/ithubfiles/sticker/advVort/1599119289252/4.png", pack2));

        arr.add(new AllStickerModel("Twetty", "https://voip.vortexvt.com:8082/ithubfiles/sticker/advVort/1599119356504/3.png", null));

        recyclerView.setAdapter(new AllStickerRecyclerAdapter(arr));


        return view;
    }
}

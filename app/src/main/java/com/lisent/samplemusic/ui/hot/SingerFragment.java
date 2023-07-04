package com.lisent.samplemusic.ui.hot;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lisent.samplemusic.R;
import com.lisent.samplemusic.Util.Tag;
import com.lisent.samplemusic.adapter.HotSingerAdapter;
import com.lisent.samplemusic.databinding.FragmentHotSingerBinding;
import com.lisent.samplemusic.entity.HotSingerBaseData;
import com.lisent.samplemusic.entity.Singer;
import com.lisent.samplemusic.network.HotService;
import com.lisent.samplemusic.network.NetEaseBaseApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 热门歌曲展示页面
 */
public class SingerFragment extends Fragment {
    private String TAG = "SingerFragment";
    private FragmentHotSingerBinding binding;
    private HotSingerAdapter adapter;
    private NetEaseBaseApi api = new NetEaseBaseApi();


    private void loadSingers(int page ,int limit){
        Call<HotSingerBaseData> call = api.hotService.getHotSinger(page, limit);
        Log.i(TAG, "loadSingers: ");
        call.enqueue(new Callback<HotSingerBaseData>() {
            @Override
            public void onResponse(Call<HotSingerBaseData> call, Response<HotSingerBaseData> response) {
                HotSingerBaseData body = response.body();
                int code = body.getCode();
                if (code == 200){
                    List<Singer> artists = body.getArtists();
                    adapter.setList(artists);
                }
            }

            @Override
            public void onFailure(Call<HotSingerBaseData> call, Throwable t) {

            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHotSingerBinding.inflate(inflater,container,false);

        RecyclerView recyclerView = binding.recyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new HotSingerAdapter(R.layout.item_hot_singer);

        recyclerView.setAdapter(adapter);

        loadSingers(0,20);


        return binding.getRoot();
    }
}
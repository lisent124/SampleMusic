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
import com.lisent.samplemusic.adapter.HotSearchAdapter;
import com.lisent.samplemusic.databinding.FragmentHotSearchBinding;
import com.lisent.samplemusic.entity.Data;
import com.lisent.samplemusic.entity.HotSearch;
import com.lisent.samplemusic.network.NetEaseBaseApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 热门 搜索关键词 展示页面
 */
public class SearchFragment extends Fragment {

    private FragmentHotSearchBinding binding;
    private HotSearchAdapter adapter;
    private NetEaseBaseApi api = new NetEaseBaseApi();

    private void loadHotSearch(){
        Call<Data<List<HotSearch>>> call = api.hotService.getHotSearch();
        Log.i(Tag.load, "loadHotSearch: ");
       call.enqueue(new Callback<Data<List<HotSearch>>>() {
           @Override
           public void onResponse(Call<Data<List<HotSearch>>> call, Response<Data<List<HotSearch>>> response) {
               Data<List<HotSearch>> body = response.body();
               if (body.getCode()==200){
                   adapter.setList(body.getData());
               }
           }

           @Override
           public void onFailure(Call<Data<List<HotSearch>>> call, Throwable t) {

           }
       });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHotSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerView;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new HotSearchAdapter(R.layout.item_hot_search);

        recyclerView.setAdapter(adapter);

        loadHotSearch();

        return root;
    }
}
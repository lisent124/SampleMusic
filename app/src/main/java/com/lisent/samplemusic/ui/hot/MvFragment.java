package com.lisent.samplemusic.ui.hot;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lisent.samplemusic.R;
import com.lisent.samplemusic.Util.Tag;
import com.lisent.samplemusic.adapter.HotMvAdapter;
import com.lisent.samplemusic.databinding.FragmentHotMvBinding;
import com.lisent.samplemusic.entity.Data;
import com.lisent.samplemusic.entity.Mv;
import com.lisent.samplemusic.network.HotService;
import com.lisent.samplemusic.network.NetEaseBaseApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 热门MV展示页面
 */
public class MvFragment extends Fragment {

    private FragmentHotMvBinding binding;
    private HotMvAdapter adapter;

    private NetEaseBaseApi api = new NetEaseBaseApi();

    private void loadMv(int page,int limit){
        Log.i(Tag.load, "loadMv: ");
        HotService hotService = api.hotService;
        Call<Data<List<Mv>>> call = hotService.getHotMv(page, limit);

        call.enqueue(new Callback<Data<List<Mv>>>() {
            @Override
            public void onResponse(Call<Data<List<Mv>>> call, Response<Data<List<Mv>>> response) {
                Data<List<Mv>> body = response.body();
                if (body.getCode() == 200){
                    adapter.setList(body.getData());
                }

            }

            @Override
            public void onFailure(Call<Data<List<Mv>>> call, Throwable t) {

            }
        });
    }

    public MvFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            binding = FragmentHotMvBinding.inflate(inflater,container,false);

            RecyclerView recyclerView = binding.recyclerView;
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
            recyclerView.setLayoutManager(layoutManager);

            adapter = new HotMvAdapter(R.layout.item_hot_mv);

            recyclerView.setAdapter(adapter);

            loadMv(0,30);

            return binding.getRoot();
    }
}
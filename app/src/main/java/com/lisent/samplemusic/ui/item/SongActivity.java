package com.lisent.samplemusic.ui.item;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lisent.samplemusic.R;
import com.lisent.samplemusic.adapter.SongAdapter;
import com.lisent.samplemusic.databinding.ActivitySongBinding;
import com.lisent.samplemusic.entity.SearchBaseData;
import com.lisent.samplemusic.entity.SingerDetailBaseData;
import com.lisent.samplemusic.entity.Song;
import com.lisent.samplemusic.entity.SongBaseData;
import com.lisent.samplemusic.network.HotService;
import com.lisent.samplemusic.network.NetEaseBaseApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 展示歌曲列表
 * 可以有两个方向的class 而来
 * loadSongFromSearch
 * loadSongFromSinger
 */
public class SongActivity extends AppCompatActivity {

    String TAG = "SongActivity";
    private ActivitySongBinding binding;
    private SongAdapter adapter;
    NetEaseBaseApi api = new NetEaseBaseApi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");

        RecyclerView recyclerView = binding.recyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SongAdapter(R.layout.item_song);
        recyclerView.setAdapter(adapter);

        if(type.equals("search")){
            String keyWord = intent.getStringExtra("keyWord");
            loadSongFromSearch(keyWord);
        }
        else if (type.equals("singer")) {
            int singerId = intent.getIntExtra("singerId", 0);
            loadSongFromSinger(singerId);
        }
        Log.i(TAG, "onCreate: Get Intent "+type);
    }


    private void loadSongFromSearch(String keyWord){
        api.hotService.getSearchInfo(keyWord)
                .enqueue(new Callback<SearchBaseData>() {
            @Override
            public void onResponse(Call<SearchBaseData> call, Response<SearchBaseData> response) {
                SearchBaseData body = response.body();
                if (body.getCode() == 200){
                    List<Song> songList = new ArrayList<>();
                    List<SearchBaseData.ResultDTO.SongsDTO> songs = body.getResult().getSongs();
                    for (SearchBaseData.ResultDTO.SongsDTO songsDTO :
                            songs) {
                        Song song = new Song(songsDTO);
                        api.hotService.getSongDetail(song.getId())
                                .enqueue(new Callback<songDetail>() {
                            @Override
                            public void onResponse(Call<songDetail> call, Response<songDetail> response) {
                                songDetail body1 = response.body();
                                if (body1.getCode() ==200){
                                    String picUrl = body1.getSongs().get(0).getAl().getPicUrl();
                                    song.setAlbumPicUrl(picUrl);
                                }
                            }
                            @Override
                            public void onFailure(Call<songDetail> call, Throwable t) {

                            }
                        });

                        songList.add(song);
                    }
                    adapter.setList(songList);
                }
            }

            @Override
            public void onFailure(Call<SearchBaseData> call, Throwable t) {
                Log.w(TAG, "onFailure: loadSongFromSearch",t);
            }
        });
    }
    private void loadSongFromSinger(int singerId) {
        Call<SingerDetailBaseData> call = api.hotService.getSongOfSinger(singerId);

        call.enqueue(new Callback<SingerDetailBaseData>() {
            @Override
            public void onResponse(Call<SingerDetailBaseData> call, Response<SingerDetailBaseData> response) {
                SingerDetailBaseData body = response.body();

                if (body.getCode() == 200){
                    List<Song> songList = new ArrayList<>();
                    List<SongBaseData> hotSongs = body.getHotSongs();
                    for (SongBaseData songDTO: hotSongs) {
                        Song song = new Song(songDTO);
                        songList.add(song);
                    }
                    adapter.setList(songList);
                }
            }

            @Override
            public void onFailure(Call<SingerDetailBaseData> call, Throwable t) {
                Log.w(TAG, "onFailure: loadSongFromSinger",t);
            }
        });
    }


    public static class songDetail {
        private List<SongBaseData> songs;
        private int code;

        public List<SongBaseData> getSongs() {
            return songs;
        }

        public void setSongs(List<SongBaseData> songs) {
            this.songs = songs;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}
package com.lisent.samplemusic.ui.item;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.GsonUtils;
import com.lisent.samplemusic.databinding.ActivityMvPlayBinding;
import com.lisent.samplemusic.entity.ComUrl;
import com.lisent.samplemusic.entity.Data;
import com.lisent.samplemusic.entity.Mv;
import com.lisent.samplemusic.network.NetEaseBaseApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 播放MV的页面
 */
public class MvPlayActivity extends AppCompatActivity {

    private ActivityMvPlayBinding binding;
    private NetEaseBaseApi api = NetEaseBaseApi.getInstance();
    private int position = 0;
    private Boolean isPlay = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMvPlayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String mvJson = intent.getStringExtra("mv");
        Mv mv = GsonUtils.fromJson(mvJson, Mv.class);

        binding.artistName.setText(mv.getArtistName());
        binding.mvName.setText(mv.getName());
        binding.playCount.setText(mv.getPlayCount().toString());


        binding.play.setOnClickListener(this.playButton());
        binding.pause.setOnClickListener(this.pauseButton());
        binding.seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        loadMvPlay(mv);
    }

    private View.OnClickListener playButton(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TAG", "onClick: playButton" +binding.mvPlay.isPlaying());
                isPlay = true;
                binding.mvPlay.start();
                handler.sendEmptyMessage(1);
            }
        };
    }
    private View.OnClickListener pauseButton(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlay = false;
                binding.mvPlay.pause();
            }
        };
    }


    @Override
    protected void onDestroy() {
        Log.i("TAG", "onDestroy: MvPlayActivity");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        isPlay = false;
        binding.mvPlay.pause();

        super.onPause();
    }


    private SeekBar.OnSeekBarChangeListener
            seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if (b){
                binding.mvPlay.seekTo(i);
                progress();
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            binding.mvPlay.pause();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            binding.mvPlay.start();
        }
    };

    Handler handler =new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if(msg.what==1){
                handler.removeMessages(1);
                progress();//执行更新
                if(isPlay){
                    handler.sendEmptyMessageDelayed(1,1000);
                }
            }

            return false;
        }
    });

    private void progress(){
        position=binding.mvPlay.getCurrentPosition();//现在的播放位置
        binding.seekBar.setProgress(position); //进度条更细
    }

    private void loadMvPlay(Mv mv){
        Call<Data<ComUrl>> call = api.hotService.getMvUrl(mv.getId());

        call.enqueue(new Callback<Data<ComUrl>>() {
            @Override
            public void onResponse(Call<Data<ComUrl>> call, Response<Data<ComUrl>> response) {
                Data<ComUrl> body = response.body();

                if( body.getCode() ==200){
                    mv.setUrl(body.getData().getUrl());
                    mv.setUrl(mv.getUrl());
                    binding.mvPlay.setVideoPath(mv.getUrl());
                    binding.mvPlay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            int max = binding.mvPlay.getDuration();
                            binding.seekBar.setMax(max);
                            handler.sendEmptyMessage(1);
                        }
                    });

//                    Log.i("TAG", "onResponse: "+mv.getUrl());

                }
            }

            @Override
            public void onFailure(Call<Data<ComUrl>> call, Throwable t) {

            }
        });


    }

}
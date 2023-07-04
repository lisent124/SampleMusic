package com.lisent.samplemusic.ui.music;


import static com.blankj.utilcode.util.ServiceUtils.bindService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.GsonUtils;
import com.bumptech.glide.Glide;
import com.lisent.samplemusic.PlayMusicService;
import com.lisent.samplemusic.R;
import com.lisent.samplemusic.Util.Tag;
import com.lisent.samplemusic.databinding.FragmentMusicBinding;
import com.lisent.samplemusic.entity.Song;
import com.lisent.samplemusic.ui.item.SongActivity;


/**
 * 歌曲的播放界面
 * 包含广播的接收
 */
public class MusicFragment extends Fragment {
    private String TAG = "MusicFragment";
    private SongReceiver songInitReceiver = null;
    private FragmentMusicBinding binding;
    private Song song;
    private boolean isPlay = false;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        songInitReceiver = new SongReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.lisent.samplemusic.playInit");
        intentFilter.addAction("com.lisent.samplemusic.playPosition");
        context.registerReceiver(songInitReceiver,intentFilter);

        onCreateBroadcast();

        Log.i(TAG, "onAttach: ");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMusicBinding.inflate(inflater,container,false);
        binding.play.setOnClickListener(playMusic);
        binding.prevSong.setOnClickListener(prevSong);
        binding.nextSong.setOnClickListener(nextSong);
        binding.seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        binding.searchView.setOnQueryTextListener(searchKeywordListener);
        Log.i(TAG, "onCreateView: ");

        return binding.getRoot();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause: ");
        handler.removeMessages(1);
        super.onPause();
    }

    @Override
    public void onResume() {
        String songName = binding.songName.getText().toString();
        if (song != null && songName == song.getName()){
            handler.sendEmptyMessage(1);
            super.onResume();
            return;
        }
        // 当显示的歌曲与播放不同时重新发送onCreateBroadcast并刷新页面
        onCreateBroadcast();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(songInitReceiver);
        handler.removeMessages(1);
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 1:
                    handler.removeMessages(1);
                    requestBroadcast();
                    handler.sendEmptyMessageDelayed(1,1000);
                    break;
            }
            return false;
        }
    });

    class SongReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra("type");
            switch (type){
                case "onCreate":
                    String songJson = intent.getStringExtra("song");
                    song = GsonUtils.fromJson(songJson, Song.class);
                    binding.seekBar.setProgress(song.getPosition());
                    binding.songName.setText(song.getName());
                    binding.albumName.setText(song.getAlbumName());
                    binding.singerName.setText(song.getArtistName());
                    binding.seekBar.setMax(song.getDuration());
                    Glide.with(getContext()).load(song.getAlbumPicUrl()).into(binding.songCover);
                    binding.play.setImageResource(R.drawable.icon_pause);
                    isPlay = true;
                    handler.sendEmptyMessage(1);
                case "position":
                    int position = intent.getIntExtra("position", 0);
                    song.setPosition(position);
                    binding.seekBar.setProgress(song.getPosition());
                    break;
                default:
                    break;
            }
        }
    }

    private SeekBar.OnSeekBarChangeListener
            onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if (b){
                binding.seekBar.setProgress(i);
                Intent intent = new Intent(getContext(),PlayMusicService.class);
                intent.putExtra("type","seekTo");
                intent.putExtra("position",i);
                getContext().startService(intent);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private SearchView.OnQueryTextListener searchKeywordListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            Log.i(TAG, "onQueryTextSubmit: keyword is "+ s);
            Intent intent = new Intent(getContext(), SongActivity.class);
            intent.putExtra("type","search");
            intent.putExtra("keyWord",s);
            startActivity(intent);
            binding.searchView.clearFocus();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };

    private View.OnClickListener playMusic = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String type = null;
            if (isPlay){
                binding.play.setImageResource(R.drawable.icon_play);
                isPlay = false;
                type = "pause";
            }
            else {
                binding.play.setImageResource(R.drawable.icon_pause);
                isPlay = true;
                type = "play";
            }
            Intent intent = new Intent(getContext(), PlayMusicService.class);
            intent.putExtra("type",type);
            getContext().startService(intent);
        }
    };

    private View.OnClickListener prevSong = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), PlayMusicService.class);
            intent.putExtra("type","prevSong");
            getContext().startService(intent);
        }
    };
    private View.OnClickListener nextSong = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), PlayMusicService.class);
            intent.putExtra("type","nextSong");
            getContext().startService(intent);
        }
    };
    private void requestBroadcast(){
        Intent intent = new Intent(getContext(),PlayMusicService.class);
        intent.putExtra("type","broadcast");
        getContext().startService(intent);
    }
    private void onCreateBroadcast(){
        Context context = getContext();
        Intent intent = new Intent(context,PlayMusicService.class);
        intent.putExtra("type","onCreate");
        context.startService(intent);
    }
}
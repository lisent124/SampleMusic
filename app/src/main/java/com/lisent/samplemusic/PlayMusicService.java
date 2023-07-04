package com.lisent.samplemusic;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.GsonUtils;
import com.google.gson.Gson;
import com.lisent.samplemusic.Util.Tag;
import com.lisent.samplemusic.entity.ComUrl;
import com.lisent.samplemusic.entity.Data;
import com.lisent.samplemusic.entity.Song;
import com.lisent.samplemusic.network.NetEaseBaseApi;
import com.lisent.samplemusic.sqlite.SongRecord;

import java.util.List;
import java.util.Stack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayMusicService extends Service {
    private String TAG = "PlayMusicService";
    private MediaPlayer mediaPlayer;
    private MusicBinder musicBinder;
    private boolean isSetData = false; // 是否设置歌曲
    private Song song = null;
    private SongRecord record;
    private Stack<Song> songStack;
    private NetEaseBaseApi api = NetEaseBaseApi.getInstance();
    private String type;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.w(TAG , "onCreate: service init");
        //初始化数据
        isSetData = false;
        mediaPlayer = new MediaPlayer();
        musicBinder = new MusicBinder();
        songStack = new Stack<>();
        record = new SongRecord(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null){
            type = intent.getStringExtra("type");
            switch (type){
                case "nextSong":
                    musicBinder.playNext();break;
                case "prevSong":
                    musicBinder.playPrev();break;
                case "onCreate":
                    if (!isSetData) break;
                    int duration = musicBinder.getDuration();
                    sendInitSong(duration);
                case "broadcast":
                    int current = musicBinder.getCurrent();
                    sendSongPosition(current);
                    break;
                case "play":
                    musicBinder.play();break;
                case "pause":
                    musicBinder.pause();break;
                case "seekTo":
                    int position = intent.getIntExtra("position", 0);
                    musicBinder.seekTo(position);
                    break;
                case "init":
                    String songJson = intent.getStringExtra("song");
                    song = GsonUtils.fromJson(songJson, Song.class);
                    api.hotService.getSongUrl(song.getId()).enqueue(callback);
                    sendInitSong(musicBinder.getDuration());
                    Log.i(TAG, "onStartCommand: song played");
                    break;
                default:
                    mediaPlayer.stop();
            }
//            Log.i(Tag.test, "onStartCommand: get Intent "+type);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private Callback callback = new Callback<Data<List<ComUrl>>>() {
        @Override
        public void onResponse(Call<Data<List<ComUrl>>> call, Response<Data<List<ComUrl>>> response) {
            Data<List<ComUrl>> body = response.body();
            if (body.getCode()==200){
                String url = body.getData().get(0).getUrl();
                song.setUrl(url);
                musicBinder.start(url);
            }
        }
        @Override
        public void onFailure(Call<Data<List<ComUrl>>> call, Throwable t) {
            Toast.makeText(PlayMusicService.this, "网络错误请重试", Toast.LENGTH_SHORT).show();
            Log.e(Tag.netWork, "onFailure: ",t );
        }
    };

    private void sendInitSong(int duration){
        Intent i = new Intent("com.lisent.samplemusic.playInit");
        song.setDuration(duration);
        String s = GsonUtils.toJson(song);
        i.putExtra("song",s);
        i.putExtra("type","onCreate");
        sendBroadcast(i);
        Log.i(TAG, "sendInitSong: ");
    }
    private void sendSongPosition(int position){
        Intent intent = new Intent("com.lisent.samplemusic.playPosition");
        intent.putExtra("position",position);
        intent.putExtra("type","position");
        sendBroadcast(intent);
    }

    private void playMusic(String path) {
        try {
            //设置资源
            mediaPlayer.reset();
            mediaPlayer.setLooping(false);
            mediaPlayer.setDataSource(path);
            isSetData = true;

            //异步缓冲准备及监听
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                    int duration = musicBinder.getDuration();
                    sendInitSong(duration);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            isSetData = false;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        isSetData = false;
    }

    public class MusicBinder extends Binder {

        //开始播放
        void start(String songUrl){
            playMusic(songUrl);
            record.uniqueInsertSong(song);
        }

        //获取资源状态
        boolean isSetData(){
            return isSetData;
        }

        //获取当前播放状态
        boolean isPlaying(){
            if (isSetData){
                return mediaPlayer.isPlaying();
            }
            return false;
        }
        //继续播放
        boolean play(){
            if (isSetData) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
            }
            return mediaPlayer.isPlaying();
        }

        //暂停播放
        boolean pause(){
            if (isSetData) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
            return mediaPlayer.isPlaying();
        }
        boolean seekTo(int position){
            if (isSetData) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(position);
                }
            }
            return mediaPlayer.isPlaying();
        }

        /**
         * 获取歌曲当前时长位置
         * 如果返回-1，则mediaplayer没有缓冲歌曲
         * @return
         */
        int getCurrent(){
            if (isSetData) {
                return mediaPlayer.getCurrentPosition();
            } else {
                return -1;
            }
        }

        /**
         * 获取歌曲总时长
         * 如果返回-1，则mediaplayer没有缓冲歌曲
         * @return
         */
        int getDuration(){
            if (isSetData) {
                return mediaPlayer.getDuration();
            } else {
                return -1;
            }
        }

        /**
         * 播放下一首
         */
        void playNext(){
//            添加到上一首
            songStack.push(song);
            Song nextSong = record.getNextSong(song.getId());
            if (nextSong == null) {
                Toast.makeText(PlayMusicService.this, "没有下一首", Toast.LENGTH_SHORT).show();
                return;
            };
            musicBinder.start(nextSong.getUrl());
            song = nextSong;
        }

        /**
         * 播放上一首
         */
        void playPrev(){
            Song prevSong ;
            if (songStack == null){
                prevSong = record.getPrevSong(song.getId());
            }else {
                prevSong = songStack.pop();
            }
            if (prevSong == null){
                Toast.makeText(PlayMusicService.this,"没有上一首",Toast.LENGTH_SHORT).show();
                return;
            }
            musicBinder.start(prevSong.getUrl());
            song = prevSong;
        }




    }
    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }
}
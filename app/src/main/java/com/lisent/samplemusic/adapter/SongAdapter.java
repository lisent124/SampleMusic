package com.lisent.samplemusic.adapter;

import static com.blankj.utilcode.util.ActivityUtils.getLauncherActivity;
import static com.blankj.utilcode.util.ActivityUtils.startActivity;
import static com.blankj.utilcode.util.ServiceUtils.startService;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.GsonUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lisent.samplemusic.PlayMusicService;
import com.lisent.samplemusic.R;
import com.lisent.samplemusic.Util.Tag;
import com.lisent.samplemusic.entity.Song;
import com.lisent.samplemusic.entity.SongBaseData;
import com.lisent.samplemusic.network.NetEaseBaseApi;
import com.lisent.samplemusic.sqlite.SongRecord;
import com.lisent.samplemusic.ui.music.MusicFragment;
import com.lisent.samplemusic.ui.record.RecordFragment;

import java.util.List;

public class SongAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {

    private String type = "play";
    private SongRecord record;
    public SongAdapter(int layoutResId) {
        super(layoutResId);
    }
    public SongAdapter(int layoutResId,String type) {
        super(layoutResId);
        this.type = type;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, Song song) {
        holder.setText(R.id.songName,song.getName());
        holder.setText(R.id.singerName,song.getArtistName());
        holder.setText(R.id.albumName,song.getAlbumName());
        switch (type) {
            case "play":
                break;
            case "record":
                ImageView imageView = holder.findView(R.id.modeImage);
                imageView.setImageResource(R.drawable.icon_delete);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        record = new SongRecord(getContext());
                        record.deleteSong(song.getId());
                        Toast.makeText(getContext(), "成功移除" + song.getName(), Toast.LENGTH_SHORT).show();
                        remove(song);
                    }
                });
                break;
        }
        holder.findView(R.id.songItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(Tag.tag, "onClick: song");
                Context context = getContext();
                Intent intent = new Intent(context, PlayMusicService.class);
                intent.putExtra("song", GsonUtils.toJson(song));
                intent.putExtra("type","init");
                context.startService(intent);
            }
        });

    }
}

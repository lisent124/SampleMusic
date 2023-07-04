package com.lisent.samplemusic.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.GsonUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lisent.samplemusic.PlayMusicService;
import com.lisent.samplemusic.R;
import com.lisent.samplemusic.entity.Mv;
import com.lisent.samplemusic.ui.item.MvPlayActivity;

import java.util.List;


public class HotMvAdapter extends BaseQuickAdapter<Mv, BaseViewHolder> {
    public HotMvAdapter(int layoutResId) {
        super(layoutResId);
    }

    public HotMvAdapter(int layoutResId, @Nullable List<Mv> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, Mv mv) {
        holder.setText(R.id.singerName,mv.getArtistName());
        holder.setText(R.id.mvName,mv.getName());
        holder.setText(R.id.playCount,mv.getPlayCount().toString());
        ImageView imageView = holder.getView(R.id.coverImage);
        Glide.with(getContext()).load(mv.getCover()).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getContext();
                Intent intent = new Intent(context, MvPlayActivity.class);
                intent.putExtra("mv", GsonUtils.toJson(mv));
                context.startActivity(intent);
                Intent service = new Intent(context, PlayMusicService.class);
                service.putExtra("type","pause");
                context.startService(service);
            }
        });

    }
}

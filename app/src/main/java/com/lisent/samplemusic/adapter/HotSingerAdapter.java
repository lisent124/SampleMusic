package com.lisent.samplemusic.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lisent.samplemusic.MainActivity;
import com.lisent.samplemusic.R;
import com.lisent.samplemusic.entity.Singer;
import com.lisent.samplemusic.ui.hot.HotFragment;
import com.lisent.samplemusic.ui.item.SongActivity;

import java.util.Collection;
import java.util.List;

public class HotSingerAdapter extends BaseQuickAdapter<Singer, BaseViewHolder> {
    public HotSingerAdapter(int layoutResId) {
        super(layoutResId);
    }

    public HotSingerAdapter(int layoutResId, @Nullable List<Singer> data) {
        super(layoutResId, data);
    }
    @Override
    public void setList(@Nullable Collection<? extends Singer> list) {
        super.setList(list);
    }
    @Override
    protected void convert(@NonNull BaseViewHolder holder, Singer singer) {
        holder.setText(R.id.singer_name,singer.getName());
        holder.getView(R.id.detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getContext();
                Intent intent = new Intent(context,SongActivity.class);
                intent.putExtra("type","singer");
                intent.putExtra("singerId",singer.getId());
                context.startActivity(intent);
            }
        });
        ImageView imageView = holder.getView(R.id.img_pic);
        Glide.with(getContext()).load(singer.getPicUrl()).into(imageView);

    }
}

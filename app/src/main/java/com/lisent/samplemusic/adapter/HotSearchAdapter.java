package com.lisent.samplemusic.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lisent.samplemusic.R;
import com.lisent.samplemusic.entity.HotSearch;
import com.lisent.samplemusic.ui.item.SongActivity;

import java.util.List;

public class HotSearchAdapter extends BaseQuickAdapter<HotSearch, BaseViewHolder> {
    public HotSearchAdapter(int layoutResId) {
        super(layoutResId);
    }

    public HotSearchAdapter(int layoutResId, @Nullable List<HotSearch> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder holder, HotSearch hotSearch) {
        holder.setText(R.id.searchWord,hotSearch.getSearchWord());
        holder.setText(R.id.searchContent,hotSearch.getContent());

        holder.findView(R.id.hotSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getContext();
                Intent intent = new Intent(context, SongActivity.class);
                intent.putExtra("type","search");
                intent.putExtra("keyWord",hotSearch.getSearchWord());
                context.startActivity(intent);
            }
        });
    }
}

package com.lisent.samplemusic.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.lisent.samplemusic.ui.hot.MvFragment;
import com.lisent.samplemusic.ui.hot.SearchFragment;
import com.lisent.samplemusic.ui.hot.SingerFragment;

public class HotPagerAdapter extends FragmentStateAdapter {

    public HotPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public HotPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new SearchFragment();
            case 1:
                return new SingerFragment();
            default:
                return new MvFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

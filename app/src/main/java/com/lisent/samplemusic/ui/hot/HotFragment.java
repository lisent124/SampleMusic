package com.lisent.samplemusic.ui.hot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.lisent.samplemusic.adapter.HotPagerAdapter;
import com.lisent.samplemusic.databinding.FragmentHotBinding;


public class HotFragment extends Fragment {

    private FragmentHotBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHotBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        HotPagerAdapter hotPagerAdapter = new HotPagerAdapter(this);

        ViewPager2 viewPager = binding.hotPager;

        viewPager.setAdapter(hotPagerAdapter);

        TabLayout tabLayout = binding.tabLayout;
//        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
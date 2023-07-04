package com.lisent.samplemusic.ui.record;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lisent.samplemusic.R;
import com.lisent.samplemusic.adapter.SongAdapter;
import com.lisent.samplemusic.databinding.FragmentRecordBinding;
import com.lisent.samplemusic.entity.Song;
import com.lisent.samplemusic.sqlite.SongRecord;

import java.util.List;

public class RecordFragment extends Fragment {
    private String TAG = "RecordFragment";

    private FragmentRecordBinding binding;
    private SongAdapter adapter;
    private SongRecord record;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        record = new SongRecord(getContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRecordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        RecyclerView recyclerView = binding.recyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SongAdapter(R.layout.item_song,"record");
        recyclerView.setAdapter(adapter);


        List<Song> songs = record.getSongs(0, 10);
        adapter.setList(songs);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
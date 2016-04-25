package com.example.lrc.module;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lrc.Application.Application;
import com.example.lrc.adapter.MusicListAdapter;
import com.example.lrc.presenter.MusicListPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 音乐列表
 */
public class MusicListFragment extends Fragment {

    @Bind(R.id.musicList_recyclerVeiw)
    public RecyclerView mRecyclerView;

    @Bind(R.id.noMusic) TextView textView;

    private MusicListPresenter presenter;
    private RecyclerView.LayoutManager mLayoutManager;
    MusicListAdapter adapter;
    Application app;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_music_list,container,false);

        ButterKnife.bind(this,view);
        init(view);

        return view;
    }


    public void init(View view) {
        presenter = new MusicListPresenter(this);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new MusicListAdapter(presenter.getMusicList(), getActivity());

        mRecyclerView.setAdapter(adapter);

        if(presenter.getMusicList().size()==0){
            textView.setVisibility(View.VISIBLE);

        }
    }

//    @OnClick(R.id.musicList_button)
//    void OnClick() {
//        Intent i = new Intent(getActivity(), MusicPlayerService.class);
//        i.putExtra("MSG", ConstantSet.PLAYER_PAUSE_MSG);
//        getActivity().startService(i);
//    }

}

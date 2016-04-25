package com.example.lrc.presenter;

import com.example.lrc.Application.Application;
import com.example.lrc.module.MusicListFragment;
import com.example.lrc.entity.music;

import java.util.List;

/**
 *
 * Created by space on 16/3/3.
 */
public class MusicListPresenter {

    private MusicListFragment musicListActivity;
    Application app;
    List<music> list;//显示的歌曲信息

    public MusicListPresenter(MusicListFragment musicListActivity) {
        this.musicListActivity = musicListActivity;
        app = (Application) musicListActivity.getActivity().getApplication();
        list=app.getList_music();
    }


    public List<music> getMusicList() {

        return list;
    }

    public void setList(List<music> list) {
        this.list = list;
    }
}

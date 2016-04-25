package com.example.lrc.Application;

import com.example.lrc.entity.music;
import com.example.lrc.presenter.MusicLoader;

import java.util.List;

/**
 * Created by space on 16/3/3.
 * <p/>
 * <p/>
 * 1.在开启软件后从数据库中获取歌曲
 * 2.把歌曲作为全局变量保存在application中
 * 3.后续的播放和删除都删除临时的全局变量
 */
public class Application extends android.app.Application {

    private List<music> list_music;
    private MusicLoader musicLoader;
    private static Application application;
    private List<music> musics_affable;
    private List<music> musics_movement;

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
        /**
         * 初始化musicloader，在软件启动后扫描获取歌曲
         */
        musicLoader = MusicLoader.instance(getContentResolver());

        musics_affable=musicLoader.getMusics_affable();
        musics_movement=musicLoader.getMusics_movement();

        list_music=musicLoader.getMusicList();

        //开启播放音乐服务
//        startService(new Intent(this, MusicPlayerService.class));
    }

    /**
     * 把扫描到的文件保存到application中，作为临时文件，后续的删除等都操作该临时数据。保证数据的同步
     */
    public void getMusic() {

        setList_music(musicLoader.getMusicList());
    }

    public static Application getInstance() {

        return application;
    }

    //-------get and set--------
    public List<music> getList_music() {
        return list_music;
    }

    public void setList_music(List<music> list_music) {
        this.list_music = list_music;
    }

    public List<music> getMusics_affable() {
        return musics_affable;
    }

    public void setMusics_affable(List<music> musics_affable) {
        this.musics_affable = musics_affable;
    }

    public List<music> getMusics_movement() {
        return musics_movement;
    }

    public void setMusics_movement(List<music> musics_movement) {
        this.musics_movement = musics_movement;
    }
}

package com.example.lrc.presenter;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;

import com.example.lrc.Application.Application;
import com.example.lrc.common.ConstantSet;
import com.example.lrc.entity.music;
import com.example.lrc.module.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 打开app后开始扫描本地的歌曲。//////
 * <p/>
 * 手机内存中某个位置上的某个database中存放图片或者音乐的信息，通过ContentResolver查询这个database来获取所有的歌曲
 * <p/>
 * 参考：http://blog.csdn.net/zhang31jian/article/details/21231467
 * <p/>
 * Created by space on 16/3/1.
 */
public class MusicLoader {

    private static final String TAG = "com.example.lrc.presenter.MusicLoader";

    private static List<music> musicList = new ArrayList<music>();

    private static MusicLoader musicLoader;

    private static ContentResolver contentResolver;

    private Uri contentUri = Media.EXTERNAL_CONTENT_URI;//指向external的database

    //选择的数据列
    private String[] projection = {Media._ID, Media.DISPLAY_NAME, Media.DATA, Media.ALBUM, Media.ARTIST, Media.DURATION, Media.SIZE};

    //where:过滤的条件
    private String where = "mime_type in ('audio/mpeg','audio/x-ms-wma')";

    //sortOrder:排序
    private String sortOrder = Media.DATA;

    private static String[] list_affable;
    private static String[] list_movement;

    private List<music> musics_affable;
    private List<music> musics_movement;


    /**
     * 使用单例模式，保证musicLoader唯一
     *
     * @param mContentResolver
     * @return
     */
    public static MusicLoader instance(ContentResolver mContentResolver) {

        if (musicLoader == null) {
            contentResolver = mContentResolver;
            list_affable = Application.getInstance().getResources().getStringArray(R.array.music_affable);
            list_movement = Application.getInstance().getResources().getStringArray(R.array.music_movement);
            musicLoader = new MusicLoader();
        }
        return musicLoader;
    }


    public MusicLoader() {

        musics_affable=new ArrayList<>();
        musics_movement=new ArrayList<>();
        Cursor cursor = contentResolver.query(contentUri, projection, where, null, sortOrder);

        if (cursor == null) {
            Log.d(TAG, "Line(64) Music Loader cursor == null");
        } else if (!cursor.moveToFirst()) {
            Log.v(TAG, "Line(66) Music Loader cursor.moveToFirst() return false.");
        } else {
            int displayNameCol = cursor.getColumnIndex(Media.DISPLAY_NAME);//获得列名索引
            int albumCol = cursor.getColumnIndex(Media.ALBUM);
            int idCol = cursor.getColumnIndex(Media._ID);
            int durationCol = cursor.getColumnIndex(Media.DURATION);
            int sizeCol = cursor.getColumnIndex(Media.SIZE);
            int artistCol = cursor.getColumnIndex(Media.ARTIST);
            int urlCol = cursor.getColumnIndex(Media.DATA);
            do {
                String title = cursor.getString(displayNameCol);
                String album = cursor.getString(albumCol);
                long id = cursor.getLong(idCol);
                int duration = cursor.getInt(durationCol);
                long size = cursor.getLong(sizeCol);
                String artist = cursor.getString(artistCol);
                String url = cursor.getString(urlCol);

                music musicInfo = new music();
                musicInfo.setId(id);
                musicInfo.setTitle(title);
                musicInfo.setAlbum(album);
                musicInfo.setDuration(duration);
                musicInfo.setSize(size);
                musicInfo.setArtist(artist);
                musicInfo.setUrl(url);

                int type=getMusicType(title);
                if (type==ConstantSet.HEARTTYPE_AFFABLE){
                    musics_affable.add(musicInfo);
                }else if(type==ConstantSet.HEARTTYPE_MOVEMENT){
                    musics_movement.add(musicInfo);
                }
                musicList.add(musicInfo);


            } while (cursor.moveToNext());
        }
    }

    public int getMusicType(String musicName) {
        int r=0;
        for (int i=0;i<list_affable.length;i++){
            if (musicName.contains(list_affable[i])){
                r= ConstantSet.HEARTTYPE_AFFABLE;
                break;
            }
        }

        for (int j=0;j<list_movement.length;j++){
            if (musicName.contains(list_movement[j])){
                r=ConstantSet.HEARTTYPE_MOVEMENT;
                break;
            }
        }
        return r;
    }

    /**
     * 获取本地的音乐
     *
     * @return
     */
    public List<music> getMusicList() {

        return musicList;
    }

    public List<music> getMusics_movement() {
        return musics_movement;
    }

    public void setMusics_movement(List<music> musics_movement) {
        this.musics_movement = musics_movement;
    }

    public List<music> getMusics_affable() {
        return musics_affable;
    }

    public void setMusics_affable(List<music> musics_affable) {
        this.musics_affable = musics_affable;
    }

    /**
     * 通过id获取音乐的路径，进行播放
     *
     * @param id
     * @return
     */
    public Uri getMusicUriById(long id) {
        Uri uri = ContentUris.withAppendedId(contentUri, id);
        return uri;
    }


}

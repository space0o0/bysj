package com.example.lrc.module;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.lrc.Application.Application;
import com.example.lrc.annotation.ActivityFragmentInject;
import com.example.lrc.base.BaseActivity;
import com.example.lrc.base.BaseRecyclerAdapter;
import com.example.lrc.base.BaseRecyclerViewHolder;
import com.example.lrc.callback.OnItemClickListener;
import com.example.lrc.common.ConstantSet;
import com.example.lrc.entity.music;
import com.example.lrc.service.MusicPlayerService;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

@ActivityFragmentInject(contentViewId = R.layout.activity_introduction,
        toolbarIndicator = R.drawable.ic_menu_back)
public class IntroductionActivity extends BaseActivity {

    @Bind(R.id.introduction)
    TextView introduction;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;

    BaseRecyclerAdapter<music> adapter;
    private int heartType;
    private LinearLayoutManager manager;
    List<music> musics;

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        heartType = getIntent().getIntExtra(ConstantSet.INTENT_HEART_TYPE, ConstantSet.HEARTTYPE_AFFABLE);

        setIntroduction();

    }

    public void setIntroduction() {
        musics = new ArrayList<>();
        if (heartType == ConstantSet.HEARTTYPE_AFFABLE) {
            introduction.setText(R.string.introduction_affable);
            musics = Application.getInstance().getMusics_affable();

        } else if (heartType == ConstantSet.HEARTTYPE_MOVEMENT) {
            introduction.setText(R.string.introduction_movement);
            musics = Application.getInstance().getMusics_movement();

        } else {
            musics = Application.getInstance().getList_music();
        }

        if (musics.size() == 0) {
            Log.i("musics is empty","~~~~~~~~~~~~~~~~~~~");
            musics.addAll(Application.getInstance().getList_music());
        }

        initRecyclerView(musics);
    }


    public void initRecyclerView(List<music> musics) {

        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(manager);
        adapter=new BaseRecyclerAdapter<music>(this,musics) {
            @Override
            public int getItemLayoutID(int viewType) {
                return R.layout.layout_music_list_item;
            }

            @Override
            public void bindData(BaseRecyclerViewHolder holder, int position, music item) {
                holder.getTextView(R.id.musicNo).setText(position+1+"");
                holder.getTextView(R.id.musicName).setText(item.getTitle());
                holder.getTextView(R.id.musicAlbum).setText(item.getAlbum());
            }
        };
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d("-----onclick", "onClick: ");
                Intent i = new Intent(IntroductionActivity.this, MusicPlayerService.class);
                i.putExtra("url", adapter.getData().get(position).getUrl());
                i.putExtra("MSG", ConstantSet.PLAYER_PLAY_MSG);
//                startService(i);

                Intent j=new Intent(IntroductionActivity.this,PlayMusicActivity.class);
                j.putExtra(ConstantSet.INTENT_MUSIC_CURR,position);
                j.putExtra(ConstantSet.INTENT_HEART_TYPE,heartType);
                startActivity(j);
            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });
        recyclerview.setAdapter(adapter);
    }

}

package com.example.lrc.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lrc.module.R;
import com.example.lrc.common.ConstantSet;
import com.example.lrc.entity.music;
import com.example.lrc.service.MusicPlayerService;

import java.util.List;

/**
 *
 * Created by space on 16/3/3.
 */
public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.viewHolder> {
    public List<music> list;
    public Activity activity;

    public MusicListAdapter(List<music> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_music_list_item, null);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, final int position) {
        holder.musicNo.setText((position + 1) + "");
        holder.musicName.setText(list.get(position).getTitle());
        holder.musicAlbum.setText(list.get(position).getAlbum());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("-----onclick", "onClick: ");
                Intent i = new Intent(activity.getApplication(), MusicPlayerService.class);
                i.putExtra("url", list.get(position).getUrl());
                i.putExtra("MSG", ConstantSet.PLAYER_PLAY_MSG);
                activity.startService(i);

                Log.i("url", list.get(position).getUrl().toString());//打印url地址
            }
        });
        holder.itemView.setTag(list.get(position));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        public TextView musicNo;
        public TextView musicName;
        public TextView musicAlbum;
        public RelativeLayout layout;

        public viewHolder(View itemView) {
            super(itemView);

            musicNo = (TextView) itemView.findViewById(R.id.musicNo);
            musicName = (TextView) itemView.findViewById(R.id.musicName);
            musicAlbum = (TextView) itemView.findViewById(R.id.musicAlbum);
            layout = (RelativeLayout) itemView.findViewById(R.id.musicLayout);

        }
    }


}

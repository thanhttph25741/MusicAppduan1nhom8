package com.Fpoly.music143.Fragment.Search.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.Fpoly.music143.Activity.MainActivity;
import com.Fpoly.music143.Fragment.Music.PlayMusicFragment;
import com.Fpoly.music143.Interface.ItemClickListener;
import com.Fpoly.music143.Fragment.Search.SearchFragment;
import com.Fpoly.music143.Model.Song;
import com.Fpoly.music143.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{
    Context context;
    ArrayList<Song> songArrayList;
    SearchFragment searchFragment;
    public SearchAdapter(Context context, ArrayList<Song> songArrayList, SearchFragment searchFragment) {
        this.context = context;
        this.songArrayList = songArrayList;
        this.searchFragment = searchFragment;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_song_info,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songArrayList.get(position);
        holder.tvsongsinger.setText(song.getSinger());
        holder.tvsongname.setText(song.getName());
        if (song.getImage().isEmpty()){
            Toast.makeText(context,"Không có hình",Toast.LENGTH_SHORT).show();
        }
        else{
            Picasso.get().load(song.getImage()).into(holder.imghinh);
        }
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                ChangeFragment(position, view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout like_layout;
        TextView tvsongname, tvsongsinger,tvindex;
        ImageView imghinh, imgrank;
        ItemClickListener itemClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvsongname = itemView.findViewById(R.id.tvsongname);
            tvsongsinger = itemView.findViewById(R.id.tvsongsinger);
            imghinh = itemView.findViewById(R.id.imgsong);
            imgrank = itemView.findViewById(R.id.imgrank);
            tvindex = itemView.findViewById(R.id.tvindex);
            like_layout = itemView.findViewById(R.id.like_layout);
            like_layout.setVisibility(View.GONE);
            tvindex.setVisibility(View.GONE);
            imgrank.setVisibility(View.GONE);
            itemView.setOnClickListener(this);
        }
        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }
    }

    private void ChangeFragment(int position, View view){
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Songs",songArrayList.get(position));
        bundle.putInt("fragment",2);
        Fragment myFragment = new PlayMusicFragment();
        myFragment.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
    }
}
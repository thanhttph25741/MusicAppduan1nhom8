package com.Fpoly.music143.Fragment.UserPlayList.Apdater;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Fpoly.music143.Fragment.Music.PlayMusicFragment;
import com.Fpoly.music143.Fragment.UserPlayList.AddItemPlaylistFragment;
import com.Fpoly.music143.Interface.ItemClickListener;
import com.Fpoly.music143.Database.DAO.PlayListDAO;
import com.Fpoly.music143.Database.Services.CallBack.PlayListCallBack;
import com.Fpoly.music143.Model.PlayList;
import com.Fpoly.music143.Model.UserInfor;
import com.Fpoly.music143.R;

import java.util.ArrayList;

public class AddItemPlayListAdapter extends RecyclerView.Adapter<AddItemPlayListAdapter.ViewHolder> {
    Context context;
    ArrayList<PlayList> playlist;
    ArrayList<String> songArrayList = new ArrayList<>() ;
    AddItemPlaylistFragment addItemPlaylistFragment;
    public AddItemPlayListAdapter(Context context, ArrayList<PlayList> playlist, AddItemPlaylistFragment addItemPlaylistFragment) {
        this.context = context;
        this.playlist = playlist;
        this.addItemPlaylistFragment = addItemPlaylistFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_playlist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        songArrayList = playlist.get(position).getSongs() ;
        Log.d("testSongs", String.valueOf(playlist.get(position).getSongs())) ;
        if (songArrayList == null) {
            holder.count_song.setText("Bài hát: 0" );
        }else {
            holder.count_song.setText("Bài hát: " + String.valueOf(songArrayList.size()));
            songArrayList.clear();
        }
        holder.tvname.setText(playlist.get(position).getName());
        holder.btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoDelete(position);
            }
        });
        holder.btn_rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialog(position);
            }
        });
        //sự kiện khi nhấn vào một phần tử trong danh sách, gọi đến hàm thêm vào bài hát vào playlist
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
               DoAddItemPlayList( playlist.get(position).getID());
            }
        });

    }

    @Override
    public int getItemCount() {
        return playlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvname,count_song;
        ImageView btn_rename,btn_del;
        private ItemClickListener itemClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            count_song = itemView.findViewById(R.id.count_song);
            tvname = itemView.findViewById(R.id.tvplaylist_name);
            btn_del = itemView.findViewById(R.id.btn_del);
            btn_rename = itemView.findViewById(R.id.btn_rename);
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

    // Them playlist
    private void ShowDialog(final int position) {
        androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(context);
        LinearLayout linearLayout = new LinearLayout(context);
        final EditText name = new EditText(context);
        name.setHint("Nhập Tên PlayList");
        name.setMinEms(16);
        linearLayout.addView(name);
        linearLayout.setPadding(10,50,10,10);
        builder.setView(linearLayout);
        //button Rename
        builder.setPositiveButton("Đổi Tên", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input email
                DoRename(position, name.getText().toString());
                dialog.dismiss();
            }
        });
        //button Cancel
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //Show Dialog
        builder.create().show();

    }

    //Hàm đổi tên playlist, được gọi khi nhấn vào nút hình cây bút trên giao diện
    private void DoRename(int position, String name) {
        PlayListDAO playListDAO = new PlayListDAO(context);
//        UserInfor UserInfor = UserInfor.getInstance();
        UserInfor userInfor = UserInfor.getInstance();
        playListDAO.renamePlayList(userInfor.getID(),playlist.get(position).getID(),name,new PlayListCallBack() {
            @Override
            public void getCallBack(ArrayList<PlayList> playLists) {
                playlist.clear();
                playlist.addAll(playLists);
                notifyDataSetChanged();

            }
        });
    }

    //Hàm xóa playlist, được gọi khi nhấn vào nút hình thùng rác trên giao diện
    private void DoDelete( final int position) {
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(context)
                .setMessage("Bạn Có Muốn Xóa Không")
                .setTitle("Thông Báo")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.loading);
                        dialog.show();
                        PlayListDAO playListDAO = new PlayListDAO(context);
//                        UserInfor UserInfor = UserInfor.getInstance();
                        UserInfor userInfor = UserInfor.getInstance();
                        playListDAO.deletePlaylist(userInfor.getID(), playlist.get(position).getID(), new PlayListCallBack() {
                            @Override
                            public void getCallBack(ArrayList<PlayList> playLists) {
                                playlist.clear();
                                playlist.addAll(playLists);
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                })
                .show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00ACC1"));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#00ACC1"));
    }

    //Hàm Thêm bài hát vào playlist, được gọi khi người dùng nhấn vào tên playlist cần thêm vào
    private void DoAddItemPlayList(String PlayListID){
        UserInfor userInfor = UserInfor.getInstance();
        PlayListDAO playListDAO = new PlayListDAO(context);
        Log.e("chuyenPlaylist",userInfor.getID() + " " + PlayListID + " "+ userInfor.getTempID()) ;
        playListDAO.addItemPlayList(userInfor.getID(),PlayListID,userInfor.getTempID());
        PlayMusicFragment.bottomSheetFragment.dismiss();
    }

}

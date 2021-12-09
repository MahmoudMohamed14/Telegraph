package com.example.telegraph.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telegraph.R;
import com.example.telegraph.UsersFragmentDirections;
import com.example.telegraph.model.Chat_Info;
import com.example.telegraph.model.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Recycler_Users_Adapter extends RecyclerView.Adapter<Recycler_Users_Adapter.Holder> {
    List<UserInfo> list;
    String thelastMessage;


    public void setList(List<UserInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_recycler_layout,parent,false);
        return new Holder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.name.setText(list.get(position).getUser_name());
        //lastmessage
        LastMessage(list.get(position).user_id,holder.lastMessage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(UsersFragmentDirections.actionUsersFragmentToChatFragment()
                .setName(list.get(position).getUser_name())
                        .setId(list.get(position).getUser_id())
                        .setStatus(list.get(position).getStatus())
                );
            }
        });
    }
    @Override
    public int getItemCount() {
        if(list == null) return 0;
        else return list.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView name;
        TextView lastMessage;
        public Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_antheruser);
            lastMessage = itemView.findViewById(R.id.lastmesg);
        }
    }
    //lastmessage
    private  void LastMessage(String id,TextView last_mag){
        thelastMessage="default";
        FirebaseUser userId= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot DS:snapshot.getChildren()){
                    Chat_Info chat_info=DS.getValue(Chat_Info.class);
                    if (chat_info.getSender().equals(id) &&
                            chat_info.getReceiver().equals(userId.getUid())
                            || chat_info.getSender().equals(userId.getUid()) && chat_info.getReceiver().equals(id)) {

                        thelastMessage = chat_info.getMessage();
                        last_mag.setText(thelastMessage);
                    }

                }
                switch (thelastMessage){
                    case "default"  :last_mag.setText("No Message");
                    break;
                    default:
                        last_mag.setText(thelastMessage);
                        break;
                }
                thelastMessage="default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

package com.example.telegraph.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telegraph.R;
import com.example.telegraph.model.Chat_Info;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recycler_Chat_Adapter extends RecyclerView.Adapter<Recycler_Chat_Adapter.Holder> {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    List<Chat_Info> messages;
    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    public void setMessages(List<Chat_Info> messages) {
        this.messages = messages;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if( viewType == RIGHT){
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.right_chat,parent,false));
        } else

            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.left_chat,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
                holder.chat_text.setText(messages.get(position).getMessage());
                String last=messages.get(position).getMessage();
        Map<String,Object> store = new HashMap<>();
        store.put("lastmessage",last);
        if(!last.isEmpty()) {
            FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser()
                    .getUid()).updateChildren(store);
        }
    }

    @Override
    public int getItemCount() {
        if(messages == null)
            return 0;
        else
            return messages.size();
    }

    static class Holder extends RecyclerView.ViewHolder{
        TextView chat_text;

        public Holder(@NonNull View itemView) {
            super(itemView);
            chat_text = itemView.findViewById(R.id.chat_text);
        }
    }

    @Override
    public int getItemViewType(int position) {
        String  current_user = auth.getCurrentUser().getUid();
        if(messages.get(position).getSender().equals(current_user)){
            return RIGHT;
        }else {
            return LEFT;
        }
    }
}

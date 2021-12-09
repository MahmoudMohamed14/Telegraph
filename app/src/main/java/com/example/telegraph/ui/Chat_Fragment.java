package com.example.telegraph.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.telegraph.R;
import com.example.telegraph.UsersFragmentDirections;
import com.example.telegraph.model.Chat_Info;
import com.example.telegraph.data.ApiService;
import com.example.telegraph.data.Client;
import com.example.telegraph.model.Data;
import com.example.telegraph.notification.MyResponse;
import com.example.telegraph.notification.NotificationSender;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chat_Fragment extends Fragment {
    FirebaseAuth auth ;
    Recycler_Chat_Adapter recycler_chat_adapter;
    RecyclerView recyclerView;
    ImageView back;
    EditText send_text;
    TextView chatUserName;
    FloatingActionButton send;
    DatabaseReference reference ;
    List<Chat_Info> chat_infos;
    ApiService apiService;

    String name;
    String userId;
    TextView status;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         name = Chat_FragmentArgs.fromBundle(getArguments()).getName();
         userId = Chat_FragmentArgs.fromBundle(getArguments()).getId();
        auth = FirebaseAuth.getInstance();
        status = view.findViewById(R.id.status);
        reference = FirebaseDatabase.getInstance().getReference();


        reference.child("users").child(userId).child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String user_status = snapshot.getValue().toString();
                status.setText(user_status);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        chatUserName = view.findViewById(R.id.name);
        chatUserName.setText(name);
        recyclerView = view.findViewById(R.id.recyclerChat);
        recyclerView.setHasFixedSize(true);
        back = view.findViewById(R.id.back);
        send_text = view.findViewById(R.id.send_text);
        send = view.findViewById(R.id.send);
        reference = FirebaseDatabase.getInstance().getReference();
        recycler_chat_adapter= new Recycler_Chat_Adapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recycler_chat_adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(Chat_FragmentDirections.actionChatFragmentToUsersFragment3());
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String current = auth.getCurrentUser().getUid();
                String msg = send_text.getText().toString().trim();
                if(msg.isEmpty()) return;
                send_message(current,userId,msg);
                send_text.setText("");
               recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        read_message(auth.getCurrentUser().getUid(),userId);
        reference.child("users").child(auth.getCurrentUser().getUid()).child("status").setValue("Online");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_, container, false);
    }
    private void send_message(String sender, String receiver , String msg ){
        Map<String,String> store = new HashMap<>();
        store.put("sender",sender);
        store.put("receiver",receiver);
        store.put("message",msg);
        reference.child("chats").push().setValue(store);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(ApiService.class);
        Data data = new Data("new message",msg);
        reference.child("users").child(userId).child("token").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String token = snapshot.getValue().toString();
                NotificationSender sender1 = new NotificationSender(data,token);
                apiService.sendNotification(sender1).enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {
                        Toast.makeText(getContext(),"Failed to send",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void  read_message(String current,String other){

        reference.child("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 chat_infos = new ArrayList<>();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                        //chat_infos.clear();
                    Chat_Info userinfo = snapshot1.getValue(Chat_Info.class);
                    if (userinfo.getSender().equals(current) &&
                            userinfo.getReceiver().equals(other)
                            || userinfo.getSender().equals(other) && userinfo.getReceiver().equals(current)
                    )
                    {
                        chat_infos.add(userinfo);
                    }

                    }
                recycler_chat_adapter.setMessages(chat_infos);

               recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());

                // Log.d("chat", "onDataChange: " + chat_infos.get(chat_infos.size()-1).getSender());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_layout,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.logout_meun){
            FirebaseAuth.getInstance().signOut();
            Navigation.findNavController(getView()).navigate(UsersFragmentDirections.actionUsersFragmentToLoginFragment5());
        }
        return super.onOptionsItemSelected(item);
    }
}
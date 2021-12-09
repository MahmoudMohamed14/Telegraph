package com.example.telegraph.ui;

import android.annotation.SuppressLint;
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
import android.widget.Button;

import com.example.telegraph.R;
import com.example.telegraph.model.UserInfo;
import com.example.telegraph.ui.Recycler_Users_Adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {
    RecyclerView recyclerView;
    Recycler_Users_Adapter adapter ;
    DatabaseReference reference;
    List<UserInfo> list;
    FirebaseAuth auth;
    Button logout;
    Menu menu;
    MenuInflater menuInflater;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        return  inflater.inflate(R.layout.fragment_users, container, false);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users");
        recyclerView = view.findViewById(R.id.recyclerview_chats);
        adapter = new Recycler_Users_Adapter();
        logout=view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               auth.signOut();
                Navigation.findNavController(getView()).navigate(UsersFragmentDirections.actionUsersFragmentToLoginFragment5());
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(layoutManager);
        get_users();



    }

    private void get_users(){
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    UserInfo usersInformation = dataSnapshot.getValue(UserInfo.class);

                    if(usersInformation.getUser_id()!=null&&!usersInformation.getUser_id().equals(auth.getCurrentUser().getUid())){

                        list.add(usersInformation);

                    }
                    adapter.setList(list);
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        reference.child(auth.getCurrentUser().getUid()).child("status").setValue("Online");
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
package com.example.telegraph.ui;



import androidx.appcompat.app.AppCompatActivity;
import androidx.customview.widget.Openable;



import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Menu;

import com.example.telegraph.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    AppBarConfiguration appBar;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navController = Navigation.findNavController(this,R.id.nav_fragment);

//        NavigationUI.setupActionBarWithNavController(this,navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_fragment);
        return NavigationUI.navigateUp(navController,(Openable) null)|| super.onSupportNavigateUp();
    }


    @Override
    protected void onStop() {
        super.onStop();
        //reference.child(auth.getCurrentUser().getUid()).child("status").setValue("Offline");
    }





}


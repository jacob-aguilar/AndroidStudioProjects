package com.example.coordinatorlayoutfb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    //BottomAppBar bottomAppBar;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // bottomAppBar = findViewById(R.id.botomAppbar);
        //floatingActionButton = findViewById(R.id.add_chat);

        if (getSupportActionBar()!=null)
            getSupportActionBar().hide();
            BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);

            bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavigationDrawerFragment bottomNavFragment = new NavigationDrawerFragment();
                    bottomNavFragment.show(getSupportFragmentManager(), "TAG");
                }
            });

    }
}

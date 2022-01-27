package com.example.coordinatorlayoutfb;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends BottomSheetDialogFragment {


    public NavigationDrawerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        NavigationView navigationView = view.findViewById(R.id.navegationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.nav1:
                        Toast.makeText(getActivity(), "111111111", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav2:
                        Toast.makeText(getActivity(), "22222222", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav3:
                        Toast.makeText(getActivity(), "3333333333", Toast.LENGTH_SHORT).show();
                        break;
                }

                return false;
            }
        });
        return view;
    }


}

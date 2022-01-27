package com.example.sesiamendoza.proyectomiagenda.Fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sesiamendoza.proyectomiagenda.Activity_Contenido;
import com.example.sesiamendoza.proyectomiagenda.Nota_Activity;
import com.example.sesiamendoza.proyectomiagenda.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragmento_Notas extends Fragment {


 private FloatingActionButton notaFab;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragmento_2, container, false);
        notaFab = view.findViewById(R.id.fab_2);

        notaFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),Nota_Activity.class);
                startActivity(intent);

            }
        });
        return view;
    }

}



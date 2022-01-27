package com.example.sesiamendoza.proyectomiagenda.Fragmentos;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sesiamendoza.proyectomiagenda.Activity_Contenido;
import com.example.sesiamendoza.proyectomiagenda.Agenda;
import com.example.sesiamendoza.proyectomiagenda.Agenda_Adapter;
import com.example.sesiamendoza.proyectomiagenda.OperacionesDB;
import com.example.sesiamendoza.proyectomiagenda.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragmento_Agenda extends Fragment {

    private FloatingActionButton addFabFloatingButton;
    private ListView lista_agenda;
    OperacionesDB operacionesDB;
    Agenda_Adapter agenda_adapter;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragmento_1, container, false);
        lista_agenda = view.findViewById(R.id.lista_agenda);
        operacionesDB = new OperacionesDB(getContext());
        agenda_adapter = new Agenda_Adapter(getContext(), operacionesDB.SelectAgenda());
        lista_agenda.setAdapter(agenda_adapter);

        lista_agenda.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        lista_agenda.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Eliminar");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Agenda agenda = operacionesDB.SelectAgenda().get(position);
                        operacionesDB.deleteAgenda(agenda);
                        Toast.makeText(getContext(), "Se ha borrado la agenda", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        agenda_adapter.notifyDataSetChanged();
                    }
                });
                builder.show();
                return true;
            }
        });


       addFabFloatingButton = view.findViewById(R.id.fab_1);

       addFabFloatingButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getContext(),Activity_Contenido.class);
               startActivity(intent);

           }
       });
       return view;


    }


    }




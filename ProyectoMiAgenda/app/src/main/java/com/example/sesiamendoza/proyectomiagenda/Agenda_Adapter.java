package com.example.sesiamendoza.proyectomiagenda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Agenda_Adapter extends ArrayAdapter<Agenda> {
    public Agenda_Adapter(Context context, ArrayList<Agenda>lista) {
        super(context, R.layout.item_lista_agenda,lista);
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent) {

        if(convertView==null)
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_lista_agenda,null);

        TextView titulo = convertView.findViewById(R.id.titulo);
        TextView categoria = convertView.findViewById(R.id.categoria);
        TextView fecha = convertView.findViewById(R.id.fecha);
        TextView hora = convertView.findViewById(R.id.hora);

        Agenda agenda = getItem(position);

        titulo.setText(agenda.getTitulo());
        categoria.setText(agenda.getCategoria());
        fecha.setText(agenda.getFecha());
        hora.setText(agenda.getHora());


        return convertView;
    }

    @Override
    public void remove(Agenda object) {
        super.remove(object);
    }
}

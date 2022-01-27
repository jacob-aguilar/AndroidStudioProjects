package com.example.sesiamendoza.proyectomiagenda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Notas_Adapter  extends ArrayAdapter<Notas> {
    public Notas_Adapter(Context context, ArrayList<Notas> lista) {
        super(context, R.layout.item_lista_notas,lista);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_lista_notas,null);

        TextView escribir = convertView.findViewById(R.id.escribir_nota);

        Notas notas = getItem(position);

        escribir.setText(notas.getNota());

        return convertView;
}

    @Override
    public void remove(Notas object) { super.remove(object);
    }
}















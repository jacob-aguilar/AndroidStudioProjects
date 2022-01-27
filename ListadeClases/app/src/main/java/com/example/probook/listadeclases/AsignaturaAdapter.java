package com.example.probook.listadeclases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AsignaturaAdapter extends ArrayAdapter <Asignatura>{

    public AsignaturaAdapter(Context context, List<Asignatura> objects) {
        super(context, R.layout.asignatura_item, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.asignatura_item, null);
        //recupear el elemento
        Asignatura asi = getItem(position);

        //recupearr las visras necesarias
        TextView codigo = convertView.findViewById(R.id.codigo_asignatura);
        TextView descripcion = convertView.findViewById(R.id.descripcion_asignatura);

        //asignar a los atributos las vistas
        codigo.setText(asi.getCodigo());
        descripcion.setText(asi.getDescripcion());

        return convertView;
    }
}

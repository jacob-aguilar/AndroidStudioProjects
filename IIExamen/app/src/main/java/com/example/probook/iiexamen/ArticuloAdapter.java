package com.example.probook.iiexamen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ArticuloAdapter extends ArrayAdapter<Articulos> {

    public ArticuloAdapter(Context context, List<Articulos> objects) {
        super(context, R.layout.articulo_item, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.articulo_item, null);
        //recupear el elemento
        Articulos arts = getItem(position);

        //recupearr las visras necesarias
        TextView nombre = convertView.findViewById(R.id.articulo);
        TextView descripcion = convertView.findViewById(R.id.descripcion);
        TextView precio = convertView.findViewById(R.id.precio);


        //asignar a los atributos las vistas
        nombre.setText(arts.getNombre());
        descripcion.setText(arts.getDescripcion());
        precio.setText(arts.getPrecio());


        return convertView;
    }
}
package com.example.user.agenda3;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ContactoAdapter extends ArrayAdapter<Contacto> {

    public ContactoAdapter(Context context,  List<Contacto> objects) {
        super(context, R.layout.item_contacto, objects);
    }
     //LLAMA LAS VISTAS DENTRO DEL MODELO
    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_contacto, null);

        Contacto c = getItem(position);
        TextView nombre = convertView.findViewById(R.id.nombre_completo);
        TextView telefono = convertView.findViewById(R.id.telefono);
        nombre.setText(c.getNombre()+" "+c.getApellido());
        telefono.setText("Tel. "+c.getTelefono());

        return convertView;
    }
}

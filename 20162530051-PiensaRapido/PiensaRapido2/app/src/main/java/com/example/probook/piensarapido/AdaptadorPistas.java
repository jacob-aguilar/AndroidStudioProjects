package com.example.probook.piensarapido;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdaptadorPistas extends BaseAdapter {

    private Context contexto;
    private ArrayList<Pistas> listItems;

    public AdaptadorPistas(Context contexto, ArrayList<Pistas> listItems) {
        this.contexto = contexto;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Pistas item = (Pistas) getItem(position);

        convertView = LayoutInflater.from(contexto).inflate(R.layout.lista_pistas, null);

        ImageView foto = (ImageView) convertView.findViewById(R.id.foto);
        TextView titulo_nivel = (TextView) convertView.findViewById(R.id.titulo_nivel);
        TextView pista_nivel = (TextView) convertView.findViewById(R.id.pista_nivel);

        foto.setImageResource(item.getFoto());
        titulo_nivel.setText(item.getTitulo());
        pista_nivel.setText(item.getPistapista());

        return convertView;
    }
}

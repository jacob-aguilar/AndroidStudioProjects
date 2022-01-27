package hn.unah.tareas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TareasAdapter extends ArrayAdapter<Tarea> {

    public TareasAdapter(Context context, List<Tarea> items) {
        super(context, R.layout.tarea_item,items);
    }

    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tarea_item, null);

        Tarea t = getItem(position);
        //ImageView icono = convertView.findViewById(R.id.icono);
        TextView titulo = convertView.findViewById(R.id.titulo);
        TextView descripcion  = convertView.findViewById(R.id.descripcion);

        //icono.setImageResource(t.getIcono());
        titulo.setText(t.getTitulo());
        descripcion.setText(t.getDescripcion());

        return convertView;
    }
}

package proyectod.permisosunahtec.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import proyectod.permisosunahtec.R;
import proyectod.permisosunahtec.web.DepartamentosGetYSet;


public class AdapterListDeptos  extends ArrayAdapter<DepartamentosGetYSet>

{

    Context cont;
    private ArrayList<DepartamentosGetYSet> usuarios;

    public AdapterListDeptos(Context cont, ArrayList < DepartamentosGetYSet > arrayList) {
        super(cont, R.layout.modelolistadepartamentos, arrayList);
        this.cont = cont;
        this.usuarios = arrayList;
    }


    @NonNull
    @Override

    public View getView ( int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflar = (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View fila;
        fila = inflar.inflate(R.layout.modelolistadepartamentos, parent, false);
        TextView nombreDepto=(TextView)fila.findViewById(R.id.txtDepartamento);
        nombreDepto.setText(usuarios.get(position).getNombreDepto());
         return fila;


    }

    @Override
    public int getCount() {
        return usuarios.size();
    }


    @Nullable
    @Override
    public DepartamentosGetYSet getItem(int position) {
        return usuarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }


    public void filtrar(ArrayList<DepartamentosGetYSet> listaDeptosFiltrada) {
        this.usuarios = new ArrayList<>();
        this.usuarios.addAll(listaDeptosFiltrada);
        notifyDataSetChanged();

    }
}



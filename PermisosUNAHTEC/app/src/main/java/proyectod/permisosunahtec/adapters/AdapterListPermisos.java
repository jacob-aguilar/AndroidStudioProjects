package proyectod.permisosunahtec.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import proyectod.permisosunahtec.pojo.PermisosGetYSet;
import proyectod.permisosunahtec.R;
import proyectod.permisosunahtec.web.PojoEmpleados;

public class AdapterListPermisos extends ArrayAdapter<PermisosGetYSet>

{

    Context cont;
    private ArrayList<PermisosGetYSet> usuarios;

    public AdapterListPermisos(Context cont, ArrayList < PermisosGetYSet > arrayList) {
        super(cont, R.layout.modelo_list_permiso, arrayList);
        this.cont = cont;
        this.usuarios = arrayList;
    }


    @NonNull
    @Override

    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflar = (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View fila = inflar.inflate(R.layout.modelo_all_permisos, parent, false);

        TextView nombreEmpleados=(TextView)fila.findViewById(R.id.nombreAllpermisos);
        TextView tipoPermiso=(TextView)fila.findViewById(R.id.tipoPermisoAllpermisos);
        TextView fechaInicio=(TextView)fila.findViewById(R.id.fechaInicioAllpermisos);
        TextView fechaFin=(TextView)fila.findViewById(R.id.fechaFinAllpermisos);


        try {
            nombreEmpleados.setText("" + usuarios.get(position).getNameEmpleado());
            fechaInicio.setText("" + usuarios.get(position).getFechaInicio());
            tipoPermiso.setText(""+ usuarios.get(position).getTipoPermiso());
            fechaFin.setText("" + usuarios.get(position).getFechaFin());
        }catch (Exception e){

        }





        return fila;


    }
    @Override
    public int getCount() {
        return usuarios.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Nullable
    @Override
    public PermisosGetYSet getItem(int position) {
        return usuarios.get(position);
    }
    public void filtrar(ArrayList<PermisosGetYSet> permisosgetYset) {
        this.usuarios = new ArrayList<>();
        this.usuarios.addAll(permisosgetYset);
        notifyDataSetChanged();


    }


}
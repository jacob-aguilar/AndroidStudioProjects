package proyectod.permisosunahtec.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import proyectod.permisosunahtec.R;
import proyectod.permisosunahtec.providers.ContractParaPermisos;
import proyectod.permisosunahtec.providers.OperacionesBaseDatos;

public class AdaptadorDeAllPermisos extends  RecyclerView.Adapter<AdaptadorDeAllPermisos.ExpenseViewHolder> {

    private Cursor cursor;
    private Context context;
    OperacionesBaseDatos   datos = OperacionesBaseDatos
            .obtenerInstancia(context);


    private OnItemClickListener escucha;
    private OnLongClickListener escucha2;

    public interface OnItemClickListener {
        public void onClick(RecyclerView.ViewHolder holder, int idDepartamento);
    }

    public interface OnLongClickListener {
        public void onLongClick(RecyclerView.ViewHolder holder, int idDepartamento);
    }

    private int obtenerIdDepto(int posicion) {
        if (cursor != null) {
            if (cursor.moveToPosition(posicion)) {
                return cursor.getInt(ConsultaAlquileres.ID);
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }


    public void filtrar(String query) {
        String[] args = new String[]{query};
        String[] proyeccion =
                new String[]{
                        ContractParaPermisos.Columnas.ID,
                        ContractParaPermisos.Columnas.ID_EMPLEADO,
                        ContractParaPermisos.Columnas.ID_DEPTO,
                        ContractParaPermisos.Columnas.ESTADO,
                        ContractParaPermisos.Columnas.OBSERVACIONES,
                        ContractParaPermisos.Columnas.FECHA_INICIO,
                        ContractParaPermisos.Columnas.FECHA_FIN,
                        ContractParaPermisos.Columnas.ID_REMOTA,
                        ContractParaPermisos.Columnas.TIPO_PERMISO,
                        ContractParaPermisos.Columnas.PENDIENTE_ELIMINACION,
                        ContractParaPermisos.Columnas.PENDIENTE_INSERCION,
                };
        if (args != null) {
            //TODO Revisar la consulta para poder hacer el filtro de los permisos NO OLVIDAR HACERLO!!!!
          cursor = datos.obtenerJoinsDePermisosFiltrado("%"+query+"%");
        }else{
            cursor = datos.obtenerJoinsDePermisos();
        }
    }



    public  class ExpenseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,OnLongClickListener, View.OnLongClickListener {
        // Campos respectivos de un item
        public TextView texNombre;
        public TextView texTipoPermiso;
        public TextView texFechaInicio;
        public TextView texFechaFin;
        public ImageView imagenAllPErmisos;


        public ExpenseViewHolder(View v) {
            super(v);
            texNombre = (TextView) v.findViewById(R.id.nombreAllpermisos);
            texTipoPermiso = (TextView) v.findViewById(R.id.tipoPermisoAllpermisos);
            texFechaInicio = (TextView) v.findViewById(R.id.fechaInicioAllpermisos);
            texFechaFin = (TextView) v.findViewById(R.id.fechaFinAllpermisos);
            imagenAllPErmisos = (ImageView) v.findViewById(R.id.imagenAllpermisos);

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(escucha !=null) {
                escucha.onClick(this, obtenerIdDepto(getAdapterPosition()));
            }else{

            }

        }








        @Override
        public boolean onLongClick(View v) {
            if(escucha2 !=null) {
                escucha2.onLongClick(this, obtenerIdDepto(getAdapterPosition()));
            }else{

            }
            return false;
        }

        @Override
        public void onLongClick(RecyclerView.ViewHolder holder, int idDepartamento) {

        }
    }
    public AdaptadorDeAllPermisos(Context context, OnItemClickListener escucha, OnLongClickListener escucha2  ) {
        this.context= context;
        this.escucha = escucha;
        this.escucha2 =  escucha2;

    }
    @Override
    public int getItemCount() {
        if (cursor!=null)
            return cursor.getCount();
        return 0;
    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.modelo_all_permisos, viewGroup, false);


        return new ExpenseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ExpenseViewHolder viewHolder, int i) {

        cursor.moveToPosition(i);
        if (cursor.getCount()>= 1){
            String nombre;
            String tipoPermiso;
            String fechInicio;
            String fechaFin;
            int idRemota =0;

            nombre = cursor.getString(cursor.getColumnIndex("name"));
            viewHolder.texNombre.setText(nombre);

            tipoPermiso = cursor.getString(cursor.getColumnIndex("tipoPermiso"));
            viewHolder.texTipoPermiso.setText(tipoPermiso);

            fechInicio = cursor.getString(cursor.getColumnIndex("fechaInicio"));
            viewHolder.texFechaInicio.setText(fechInicio);

            fechaFin = cursor.getString(cursor.getColumnIndex("fechaFin"));

            if(fechInicio.equals(fechaFin)){
                viewHolder.texFechaFin.setVisibility(View.GONE);
            }else {
                viewHolder.texFechaFin.setVisibility(View.VISIBLE);
                viewHolder.texFechaFin.setText(fechaFin);
            }

            try {
                idRemota = cursor.getInt(cursor.getColumnIndex("pendiente_insercion"));
            }catch (Exception e){

            }

            if (idRemota ==0){
                viewHolder.imagenAllPErmisos.setImageResource(R.drawable.ok_db);

            }else {
                viewHolder.imagenAllPErmisos.setImageResource(R.drawable.ic_stat_ic_subido);
            }





        }
    }


    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }
    interface ConsultaAlquileres {
        int ID = 0;
        int NAME = 1;
        int IDREMOTA = 2;
        int EMAIL = 3;
        int PICTURE = 4;
        int PRECIO = 5;
        int URL = 6;
    }


}

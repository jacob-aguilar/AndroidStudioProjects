package proyectod.permisosunahtec.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import proyectod.permisosunahtec.R;
import proyectod.permisosunahtec.providers.ContractParaEmpleados;
import proyectod.permisosunahtec.providers.ContractParaPermisos;


/**
 * Adaptador del recycler view
 */
public class AdaptadorDePermisos extends RecyclerView.Adapter<AdaptadorDePermisos.ExpenseViewHolder>   {
    private Cursor cursor;
    private Context context;




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


    public  class ExpenseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,OnLongClickListener, View.OnLongClickListener {
        // Campos respectivos de un item
        public TextView texTipoPermiso;
        public TextView fechaInicio;
        public TextView fechaFin;
        ImageView imagenPErmisos;

        public ExpenseViewHolder(View v) {
            super(v);
            texTipoPermiso = (TextView) v.findViewById(R.id.tipoPermiso);
            fechaInicio = (TextView) v.findViewById(R.id.txtfechaIniciomodelo);
            fechaFin = (TextView) v.findViewById(R.id.txtfechaFin);
            imagenPErmisos=(ImageView) v.findViewById(R.id.imagenPermisos);

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


    public AdaptadorDePermisos(Context context, OnItemClickListener escucha, OnLongClickListener escucha2  ) {
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
                .inflate(R.layout.modelo_list_permiso, viewGroup, false);


        return new ExpenseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ExpenseViewHolder viewHolder, int i) {
        String imagenDatos =null;
        cursor.moveToPosition(i);
        if (cursor.getCount()>= 1){
            String tipoPermiso;
            String fechaInicio;
            String fechaFIn;






            tipoPermiso= cursor.getString(cursor.getColumnIndex("tipoPermiso"));

            viewHolder.texTipoPermiso.setText(tipoPermiso);

            fechaInicio = cursor.getString(cursor.getColumnIndex("fechaInicio"));

            viewHolder.fechaInicio.setText(fechaInicio);

            fechaFIn = cursor.getString(cursor.getColumnIndex("fechaFin"));

            if(fechaInicio.equals(fechaFIn)){
                viewHolder.fechaFin.setVisibility(View.INVISIBLE);
            }else{
                viewHolder.fechaFin.setVisibility(View.VISIBLE);
                viewHolder.fechaFin.setText(fechaFIn);

            }

            int idRemota = cursor.getInt(cursor.getColumnIndex("pendiente_insercion"));
            if (idRemota ==0){
                viewHolder.imagenPErmisos.setImageResource(R.drawable.ok_db);

            }else {
                viewHolder.imagenPErmisos.setImageResource(R.drawable.ic_stat_ic_subido);
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
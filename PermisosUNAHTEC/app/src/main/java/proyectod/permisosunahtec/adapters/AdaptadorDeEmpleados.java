package proyectod.permisosunahtec.adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import proyectod.permisosunahtec.R;
import proyectod.permisosunahtec.providers.ContractParaDepartamentos;
import proyectod.permisosunahtec.providers.ContractParaEmpleados;


/**
 * Adaptador del recycler view
 */
public class AdaptadorDeEmpleados extends RecyclerView.Adapter<AdaptadorDeEmpleados.ExpenseViewHolder>   {
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

    public void filtrar(String query) {
        String[] proyeccion = new String[]{
                ContractParaEmpleados.Columnas.ID,
                ContractParaEmpleados.Columnas.NAME,
                ContractParaEmpleados.Columnas.PICTURE,
                ContractParaEmpleados.Columnas.EMAIL,
                ContractParaEmpleados.Columnas.CODDEPTO,
                ContractParaEmpleados.Columnas.ESTADO,
                ContractParaEmpleados.Columnas.PENDIENTE_ELIMINACION,
                ContractParaEmpleados.Columnas.PENDIENTE_INSERCION,
                ContractParaEmpleados.Columnas.ROL,
                ContractParaEmpleados.Columnas.ID_REMOTA
        };
        String[] args = new String[]{"%"+query+"%","%"+query+"%"};
        if (args != null) {
            cursor = context.getContentResolver().query(ContractParaEmpleados.CONTENT_URI, null, "eliminado = 0 and name like ? or eliminado =0 and _id like ?"
                    , args, null);
        }
        else {

        }
    }
    public void filtrarPorDeptos(String query, String informacionId) {
        String[] proyeccion = new String[]{
                ContractParaEmpleados.Columnas.ID,
                ContractParaEmpleados.Columnas.NAME,
                ContractParaEmpleados.Columnas.PICTURE,
                ContractParaEmpleados.Columnas.EMAIL,
                ContractParaEmpleados.Columnas.CODDEPTO,
                ContractParaEmpleados.Columnas.ESTADO,
                ContractParaEmpleados.Columnas.PENDIENTE_ELIMINACION,
                ContractParaEmpleados.Columnas.PENDIENTE_INSERCION,
                ContractParaEmpleados.Columnas.ROL,
                ContractParaEmpleados.Columnas.ID_REMOTA
        };
        String[] args = new String[]{"%"+query+"%",informacionId,"%"+query+"%",informacionId};
        if (args != null) {
            cursor = context.getContentResolver().query(ContractParaEmpleados.CONTENT_URI, null, "eliminado = 0 and name like ? and codDepartamento =? or eliminado =0 and _id like ? and codDepartamento =? "
                    , args, null);
        }
        else {

        }
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
    private String obtenerNombreDepto(int posicion) {
        if (cursor != null) {
            if (cursor.moveToPosition(posicion)) {
                return cursor.getString(ConsultaAlquileres.NAME);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    private int obtenerIdRemota(int posicion) {
        if (cursor != null) {
            if (cursor.moveToPosition(posicion)) {
                return cursor.getInt(ConsultaAlquileres.IDREMOTA);
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }


    public  class ExpenseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,OnLongClickListener, View.OnLongClickListener {
        // Campos respectivos de un item
        public TextView texNombre;
        public TextView texCodigo;
        public ImageView imagen;
        public ImageView imagenSubida;
        Bitmap bitmap;

        public ExpenseViewHolder(View v) {
            super(v);
            texNombre = (TextView) v.findViewById(R.id.textNombreItem);
            texCodigo = (TextView) v.findViewById(R.id.textCodigoItemsolo);
            imagen = (ImageView) v.findViewById(R.id.imageitem);
            imagenSubida = (ImageView) v.findViewById(R.id.imagenSubida);


            imagen.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    cursor.moveToPosition(getAdapterPosition());
                    AlertDialog.Builder imagenD = new AlertDialog.Builder(context);
                    LayoutInflater factory = LayoutInflater.from(context);
                    final View view1 = factory.inflate(R.layout.item_alert_dialog, null);
                    imagenD.setView(view1);
                    //Estableciendole el titulo y la imagen al alertDialog
                    ImageView imagen = view1.findViewById(R.id.imagenAlertDialog);


                    if (cursor.getString(cursor.getColumnIndex("picture")) != null) {
                        byte[] bytes = Base64.decode(cursor.getString(cursor.getColumnIndex("picture")), Base64.DEFAULT);
                        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                         imagen.setImageBitmap(bitmap);

                    } else {
                        imagen.setImageResource(R.drawable.empleado_icon);
                    }
                    imagenD.setInverseBackgroundForced(true);


                    imagenD.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    imagenD.create().show();
                }
            });

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


    public AdaptadorDeEmpleados(Context context, OnItemClickListener escucha, OnLongClickListener escucha2  ) {
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
                .inflate(R.layout.modelolistaempleado, viewGroup, false);


        return new ExpenseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ExpenseViewHolder viewHolder, int i) {
        String imagenDatos =null;
        cursor.moveToPosition(i);
        if (cursor.getCount()>= 1){
            String nombre;
            int id;

            Bitmap bitmap;

            try {
                imagenDatos = cursor.getString(cursor.getColumnIndex("picture"));
            }catch (Exception e){

            }



            if(imagenDatos !=null) {

                try {

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 5;

                    byte[] bytes = Base64.decode(imagenDatos, Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                    viewHolder.imagen.setImageBitmap(bitmap);

                    bitmap=null;

                } catch (OutOfMemoryError exc) {

                    bitmap = null;
                }
            }else {
                viewHolder.imagen.setImageResource(R.drawable.empleado_icon);

            }

            id= cursor.getInt(0);

            viewHolder.texCodigo.setText(""+id);

                nombre = cursor.getString(ConsultaAlquileres.NAME);

                viewHolder.texNombre.setText(nombre);

                int idRemota = cursor.getInt(cursor.getColumnIndex("pendiente_insercion"));
                if (idRemota ==0){
                    viewHolder.imagenSubida.setImageResource(R.drawable.ok_db);

                }else {
                    viewHolder.imagenSubida.setImageResource(R.drawable.ic_stat_ic_subido);
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
package proyectod.permisosunahtec.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import proyectod.permisosunahtec.R;
import proyectod.permisosunahtec.providers.ContractParaDepartamentos;


/**
 * Adaptador del recycler view
 */
public class AdaptadorDeDepartamentos extends RecyclerView.Adapter<AdaptadorDeDepartamentos.ExpenseViewHolder> implements ListAdapter {
    private Cursor cursor;
    private Context context;


    private OnItemClickListener escucha;

    private OnLongClickListener escucha2;

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        if (cursor != null)
            return cursor.getCount();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflar = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View fila;
        fila = inflar.inflate(R.layout.modelolistadepartamentos, parent, false);


        TextView nombreDepto = (TextView) fila.findViewById(R.id.txtDepartamento);


        cursor.moveToPosition(position);
        if (cursor.getCount() >= 1) {
            String monto;
            int id;
            int pendiente;

            id = cursor.getInt(0);


            monto = cursor.getString(ConsultaAlquileres.NOMBREDEPTO);
            nombreDepto.setText(monto);


        }

        return fila;
    }

    @Override
    public int getViewTypeCount() {
        if (cursor != null)
            return cursor.getCount();
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }


    public interface OnItemClickListener {
        public void onClick(RecyclerView.ViewHolder holder, int idDepartamento);
    }

    public interface OnLongClickListener {
        public void onLongClick(RecyclerView.ViewHolder holder, int idDepartamento, String nombreDepto, int IdRemota, int adapterPosicion);
    }


    public void filtrar(String query) {
        String[] proyeccion = new String[]{
                ContractParaDepartamentos.Columnas.ID,
                ContractParaDepartamentos.Columnas.NOMBREDEPTO,
                ContractParaDepartamentos.Columnas.ESTADO,
                ContractParaDepartamentos.Columnas.ID_REMOTA,
                ContractParaDepartamentos.Columnas.PENDIENTE_ELIMINACION,
                ContractParaDepartamentos.Columnas.PENDIENTE_INSERCION
        };
        String[] args = new String[]{"%"+query+"%"};
        if (args != null) {
            cursor = context.getContentResolver().query(ContractParaDepartamentos.CONTENT_URI, proyeccion, "eliminado =0 and nombreDepto like ?", args, null);
        }
        else {
            cursor = context.getContentResolver().query(ContractParaDepartamentos.CONTENT_URI,
                    proyeccion,"eliminado =0",null,null);

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
                return cursor.getString(ConsultaAlquileres.NOMBREDEPTO);
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


    public class ExpenseViewHolder extends RecyclerView.ViewHolder implements  OnLongClickListener, View.OnLongClickListener {
        // Campos respectivos de un item
        public TextView monto;
        private View itemView;
        ImageView imagenDepto;

        public ExpenseViewHolder(View v) {
            super(v);
            this.itemView=v;
            monto = (TextView) itemView.findViewById(R.id.txtDepartamento);
            imagenDepto = (ImageView) itemView.findViewById(R.id.imagenDeptos);
            v.setOnLongClickListener(this);

        }




        @Override
        public boolean onLongClick(View v) {
            if (escucha2 != null) {
                escucha2.onLongClick(this, obtenerIdDepto(getAdapterPosition()), obtenerNombreDepto(getAdapterPosition()), obtenerIdRemota(getAdapterPosition()), getAdapterPosition());
            } else {

            }
            return false;
        }

        @Override
        public void onLongClick(RecyclerView.ViewHolder holder, int idDepartamento, String nombreDepto, int IdRemota, int adapterPosicion) {

        }
    }


    public AdaptadorDeDepartamentos(Context context, OnItemClickListener escucha, OnLongClickListener escucha2) {
        this.context = context;
        this.escucha = escucha;
        this.escucha2 = escucha2;

    }

    @Override
    public int getItemCount() {
        if (cursor != null)
            return cursor.getCount();
        return 0;
    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.modelolistadepartamentos, viewGroup, false);


        return new ExpenseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ExpenseViewHolder viewHolder,  int i) {

        cursor.moveToPosition(i);
        if (cursor.getCount() >= 1) {
            String monto;
            int id;
            int pendiente;

            id = cursor.getInt(0);


            monto = cursor.getString(ConsultaAlquileres.NOMBREDEPTO);
            viewHolder.monto.setText(monto);
            int idRemota = cursor.getInt(cursor.getColumnIndex("pendiente_insercion"));
            if (idRemota ==0){
                viewHolder.imagenDepto.setImageResource(R.drawable.ok_db);

            }else {
                viewHolder.imagenDepto.setImageResource(R.drawable.ic_stat_ic_subido);
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (escucha != null) {
                        escucha.onClick(viewHolder, obtenerIdDepto(viewHolder.getAdapterPosition()));
                    } else {

                    }
                }
            });
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
        int NOMBREDEPTO = 1;
        int IDREMOTA = 2;
        int UBICACION = 3;
        int DESCRIPCION = 4;
        int PRECIO = 5;
        int URL = 6;
    }
}
package proyectod.permisosunahtec.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
 import proyectod.permisosunahtec.R;
import proyectod.permisosunahtec.web.DepartamentosGetYSet;
import proyectod.permisosunahtec.web.PojoEmpleados;

public class AdapterListEmpleado extends ArrayAdapter<PojoEmpleados> {

    Context cont;
    Bitmap bitmap;
    ArrayList<PojoEmpleados> usuarios;

    public AdapterListEmpleado(Context cont, ArrayList<PojoEmpleados> arrayList) {
        super(cont, R.layout.modelolistaempleado, arrayList);
        this.cont = cont;
        this.usuarios = arrayList;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        try {
            convertView = LayoutInflater.from(cont).inflate(R.layout.modelolistaempleado, null);

            final PojoEmpleados empleado = getItem(position);
            TextView nombreempleado = (TextView) convertView.findViewById(R.id.textNombreItem);
            TextView codigoEmpleado = (TextView) convertView.findViewById(R.id.textCodigoItem);
            nombreempleado.setText("" + empleado.getName());
            codigoEmpleado.setText("" + empleado.getId());
            CircleImageView imagenEmpleado = convertView.findViewById(R.id.imageitem);

            if (empleado.getImagen() != null) {
                imagenEmpleado.setImageBitmap(empleado.getImagen());

            } else {
                imagenEmpleado.setColorFilter(R.drawable.empleado_icon);
            }

            imagenEmpleado.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    AlertDialog.Builder imagenD = new AlertDialog.Builder(cont);
                    LayoutInflater factory = LayoutInflater.from(cont);
                    final View view1 = factory.inflate(R.layout.item_alert_dialog, null);
                    imagenD.setView(view1);
                    //Estableciendole el titulo y la imagen al alertDialog
                    ImageView imagen = view1.findViewById(R.id.imagenAlertDialog);
                    if (empleado.getImagen() != null) {
                        byte[] bytes = Base64.decode(empleado.getPicture(), Base64.DEFAULT);
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


        } catch (Exception e) {
            Toast.makeText(getContext(), "errores", Toast.LENGTH_SHORT).show();
        }

        return convertView;
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
    public PojoEmpleados getItem(int position) {
        return usuarios.get(position);
    }

    public void filtrar(ArrayList<PojoEmpleados> listaDeptosFiltrada) {
        this.usuarios = new ArrayList<>();
        this.usuarios.addAll(listaDeptosFiltrada);
        notifyDataSetChanged();


    }
}

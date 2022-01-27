package proyectod.permisosunahtec;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import proyectod.permisosunahtec.providers.ContractParaDepartamentos;
import proyectod.permisosunahtec.syncs.SyncAdapterDeptos;
import proyectod.permisosunahtec.utils.Utilidades;


public class agregarnuevodepto extends AppCompatActivity {

    EditText editDepartamento;

    private String idDepto;
    private String nombreDepto;
    private int i;
    private String idD;
    private String idRemota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_nuevo_depto);
         editDepartamento = (EditText) findViewById(R.id.EditCrearDepartamento);


        Bundle bundle =this.getIntent().getExtras();
        if(bundle != null){

            idDepto =bundle.getString("informacionId");
            idRemota =bundle.getString("idRemota");
            nombreDepto =bundle.getString("nombreDepto");
            i =bundle.getInt("c");
            editDepartamento.setText(nombreDepto);
        }

    }



    public void alClickearBoton(View v) {

        if(i==2){
            if (editDepartamento.getText().toString().equals("")) {
                Toast.makeText(this, "Ingrese los datos", Toast.LENGTH_SHORT).show();
            }else {
                Log.i("JUSTIN",""+idRemota);
                String nombreText = editDepartamento.getText().toString().toLowerCase();
                ContentValues values = new ContentValues();

                values.put(ContractParaDepartamentos.Columnas.NOMBREDEPTO, nombreText);

                values.put(ContractParaDepartamentos.Columnas.PENDIENTE_INSERCION, 1);
                values.put(ContractParaDepartamentos.Columnas.ESTADO, 0);
                String[] selectionArgs = new String[]{idDepto};



                try {
                    getApplicationContext().getContentResolver().update(ContractParaDepartamentos.CONTENT_URI, values,"_id =?",selectionArgs);

                    SyncAdapterDeptos.sincronizarAhora(this, true);
                    if (Utilidades.materialDesign())
                        finishAfterTransition();

                    else finish();
                }catch (Exception e){
                    Toast.makeText(this, "Este departamento ya existe.", Toast.LENGTH_SHORT).show();
                }


            }

        }else {

            if (editDepartamento.getText().toString().equals("")) {
                Toast.makeText(this, "Ingrese los datos", Toast.LENGTH_SHORT).show();
            } else {
                String nombreText = editDepartamento.getText().toString().toLowerCase();
                ContentValues values = new ContentValues();
                values.put(ContractParaDepartamentos.Columnas.NOMBREDEPTO, nombreText);

                values.put(ContractParaDepartamentos.Columnas.PENDIENTE_INSERCION, 1);
   try {
    getApplicationContext().getContentResolver().insert(ContractParaDepartamentos.CONTENT_URI, values);
    SyncAdapterDeptos.sincronizarAhora(this, true);


       if (Utilidades.materialDesign())
           finishAfterTransition();

       else finish();
     }catch (Exception e){
    Toast.makeText(this, "Este departamento ya existe", Toast.LENGTH_SHORT).show();
}


            }
        }


    }

}
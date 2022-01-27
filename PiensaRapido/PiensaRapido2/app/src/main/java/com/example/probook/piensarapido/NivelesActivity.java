package com.example.probook.piensarapido;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NivelesActivity extends AppCompatActivity {


    private EditText et_numero, et_descripcion, et_respuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_niveles);

        et_numero = (EditText)findViewById(R.id.txt_numero);
        et_descripcion = (EditText)findViewById(R.id.txt_descripcion);
        et_respuesta = (EditText)findViewById(R.id.txt_respuesta);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_homero2);
    }

    //Méotdo para registrar la idea
    public void Registrar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        String codigo = et_numero.getText().toString();
        String descripcion = et_descripcion.getText().toString();
        String precio = et_respuesta.getText().toString();

        if(!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()){
            ContentValues registro = new ContentValues();

            registro.put("codigo", codigo);
            registro.put("descripcion", descripcion);
            registro.put("precio", precio);

            BaseDeDatos.insert("articulos", null, registro);

            BaseDeDatos.close();
            et_numero.setText("");
            et_descripcion.setText("");
            et_respuesta.setText("");

            Toast.makeText(this,"Registro exitoso", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    //Método para consultar la idea
    public void Buscar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDeDatabase = admin.getWritableDatabase();

        String cod = et_numero.getText().toString();

        if(!cod.isEmpty()){
            Cursor fila = BaseDeDatabase.rawQuery
                    ("select descripcion, precio from articulos where codigo =" + cod, null);

            if(fila.moveToFirst()){
                et_descripcion.setText(fila.getString(0));
                et_respuesta.setText(fila.getString(1));
                BaseDeDatabase.close();
            } else {
                Toast.makeText(this,"No existe esta idea", Toast.LENGTH_SHORT).show();
                BaseDeDatabase.close();
            }

        } else {
            Toast.makeText(this, "Debes introducir el numero de busqueda", Toast.LENGTH_SHORT).show();
        }
    }

    //Método para eliminar la idea
    public void Eliminar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper
                (this, "administracion", null, 1);
        SQLiteDatabase BaseDatabase = admin.getWritableDatabase();

        String codigo = et_numero.getText().toString();

        if(!codigo.isEmpty()){

            int cantidad = BaseDatabase.delete("articulos", "codigo=" + codigo, null);
            BaseDatabase.close();

            et_numero.setText("");
            et_descripcion.setText("");
            et_respuesta.setText("");

            if(cantidad == 1){
                Toast.makeText(this, "Idea eliminada exitosamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Esta idea no existe", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Debes de introducir el numero de busqueda", Toast.LENGTH_SHORT).show();
        }
    }

    //Método para modificar una idea
    public void Modificar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase BaseDatabase = admin.getWritableDatabase();

        String codigo = et_numero.getText().toString();
        String descripcion = et_descripcion.getText().toString();
        String precio = et_respuesta.getText().toString();

        if(!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()){

            ContentValues registro = new ContentValues();
            registro.put("codigo", codigo);
            registro.put("descripcion", descripcion);
            registro.put("precio", precio);

            int cantidad = BaseDatabase.update("articulos", registro, "codigo=" + codigo, null);
            BaseDatabase.close();

            if(cantidad == 1){
                Toast.makeText(this, "Idea modificada correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Esta idea no existe", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
}

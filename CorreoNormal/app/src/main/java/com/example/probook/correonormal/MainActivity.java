package com.example.probook.correonormal;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText correo;
    private EditText asunto;
    private EditText mensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        correo = findViewById(R.id.correo);
        asunto = findViewById(R.id.asunto);
        mensaje = findViewById(R.id.mensaje);

    }
    public void enviar(View view){
        String c = correo.getText().toString().trim();
        String a = asunto.getText().toString().trim();
        String m = mensaje.getText().toString().trim();
        try {
            enviarCorreo(c, a, m);
        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
    private final static int ENVIAR_CORREO = 1;
    public void enviarCorreo(String correo, String asunto, String mensaje){
        //Estas son las intenciones implicitas
        Intent intent = new Intent(Intent.ACTION_SEND);//Aqui podemos compartir multimedia y muchas mas pero tenemosque indicar el tipo de contediod
        //intent.setType("text/plain");//Tipo de contenido
        intent.setType("message/rfc822"); //MIME TYPE
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{correo});
        intent.putExtra(Intent.EXTRA_SUBJECT, asunto);
        intent.putExtra(Intent.EXTRA_TEXT, mensaje);

        //startActivity(intent.createChooser(intent, "Enviar Correo"));
        //startActivity(intent);// La accion de compartir sse muestra un poco diferente es valida pero tiene el inconveniente que
        //tengo que tener un aplicacion para que se abra El Chooser le pregunta al S.O si tiene la app
        startActivityForResult(intent, ENVIAR_CORREO);//Deberiamos de utilizar una constante definida por nosotros
    }
    //Sobreescribir el metodo

    @Override//3 coas importantes Codigo de peticion, codigo de resultado,
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        /*if (resultCode == ENVIAR_CORREO && resultCode == RESULT_OK ){
            Toast.makeText(this, "El mensaje se envio", Toast.LENGTH_LONG).show();
        }*/
        if (resultCode == ENVIAR_CORREO){
            Toast.makeText(this, "El mensaje se envio"+resultCode, Toast.LENGTH_LONG).show();
        }
    }
}

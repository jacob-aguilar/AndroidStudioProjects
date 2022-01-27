package com.example.user.respuestas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private EditText codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultadode);
        codigo = findViewById(R.id.codigo);

    }
    private final static int VALIDAR = 1;
    public void validar(View view){
        int numero = Integer.valueOf(codigo.getText().toString());
        Intent intent = new Intent(this, ResultadodeActivity.class);
        intent.putExtra("codigo", numero);
        startActivityForResult(intent, VALIDAR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == VALIDAR){
            if(resultCode == RESULT_OK){
                String msg = data.getStringExtra("msg");
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            }
            if(requestCode == RESULT_CANCELED){
                Toast.makeText(this, "Lastima", Toast.LENGTH_LONG).show();
            }
        }
    }
}

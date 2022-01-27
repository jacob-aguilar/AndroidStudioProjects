package com.example.probook.answer;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText codigo;
    private static final int VALIDAR = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        codigo = findViewById(R.id.codigo);
    }

    public void validar(View view){
        int numero = Integer.valueOf(codigo.getText().toString());
        Intent intent = new Intent(this, ResultadoActivity.class);
        intent.putExtra("Codigo", numero);
        startActivityForResult(intent, VALIDAR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VALIDAR){
            if (resultCode == RESULT_OK){
                String msg = data.getStringExtra("msg");
                Toast.makeText(this, "msg", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Lastima", Toast.LENGTH_LONG).show();
            }
        }
    }
}


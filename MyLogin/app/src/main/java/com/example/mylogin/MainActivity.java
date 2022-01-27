package com.example.mylogin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    EditText etEmail, etPassword;
    TextView regist;
    Button btnLogin;
    ProgressDialog pdDialogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        btnLogin= (Button) findViewById(R.id.btnLogin);
        regist= (TextView) findViewById(R.id.idRegistroLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdDialogo = ProgressDialog.show(MainActivity.this, "Iniciando Sesion", "Comprobando credenciales", true, false);
            }
        });
    }
}

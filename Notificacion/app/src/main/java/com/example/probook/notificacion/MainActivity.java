package com.example.probook.notificacion;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private final static String CANAL_ID = "CANAL";
    private final static int NOTIFICACION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void notificar(View view){
        lanzarNotificacion();
    }

    //Para poderlo utilizar en una mehor forma
    public void lanzarNotificacion(){
        NotificationCompat.Builder buider = new NotificationCompat.Builder(this, CANAL_ID);

        //Intent intent = new Intent(this, MainActivity.class);
        Intent intent = new Intent(this, NotificacionActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        buider.setContentIntent(pendingIntent);

        buider.setSmallIcon(R.drawable.botonacercade2);//El icono no es opcional
        buider.setContentTitle("Bienvenido");
        buider.setContentText("Esta es la primera notificacion");
        buider.setAutoCancel(true);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        createNotificationChannel(notificationManager);
        notificationManager.notify(NOTIFICACION_ID, buider.build());
    }

    private void createNotificationChannel(NotificationManager notificationManager){
        /*crear NotificacionChannel, pero solo en API 26 o superior porque
         la clase NotificationChannel es nueva y no esta en la suport library (En mi caso no es necesario este metodo)*/

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence nombreCanal = "Canal Notificacion";
            String descripcionCanal = "Descripcion del canal";

            int importancia = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel canal = new NotificationChannel(CANAL_ID, nombreCanal, importancia);
            canal.setDescription(descripcionCanal);
            notificationManager.createNotificationChannel(canal);
        }
    }
}

package proyectod.permisosunahtec;


import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


public class ServicioNotificacion extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    private NotificationManager notificationManager;
    private final int NOTIFICATION_ID = 1010;

    private Cursor fila;
    private SQLiteDatabase bd;
    private String nombre;
    public static String  fecha,descripcion;
    private int id;
    public static String horaNoti;
    public static String fechaEstatica = "20";
    public static String horaEstatica  = "22";
    public static String descripcionEstatica = "21";
    public static int a =1;
    public  static String aaa;
    public static  String cadenaF, cadenaH,fecha_sistema,hora_sistema,fecha_sistema2;
    Calendar calendario;



    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MyTestService.class);
        context.startService(i);

        calendario = Calendar.getInstance();


        int hora, min, dia, mesMas, ano, mes;
        int diaMas, anio;

        mes = calendario.get(Calendar.MONTH) + 1;
        dia = calendario.get(Calendar.DAY_OF_MONTH);
        hora =calendario.get(Calendar.HOUR_OF_DAY);

         if(dia ==1 && hora >=8 && hora <=24 ) {
             triggerNotification(context, "Es tiempo de generar su reporte mensual");
         }


    }
    private void triggerNotification(Context contexto, String t) {
        Intent notificationIntent = new Intent(contexto, Reportes.class);


        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(contexto, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] pattern = new long[]{2000, 1000, 2000};

        NotificationCompat.Builder builder = new NotificationCompat.Builder(contexto);
        builder.setContentIntent(contentIntent)



                .setTicker("Pemisos UNAH-TEC requiere su atención")
                .setContentTitle("")
                .setContentTitle("Generar reportes")
                .setContentText(t)
                .setContentInfo("")
                .setLargeIcon(BitmapFactory.decodeResource(contexto.getResources(), R.drawable.ic_notifications))
                .setSmallIcon(R.drawable.ic_notifications)
                .setAutoCancel(true) //Cuando se pulsa la notificación ésta desaparece
                .setSound(defaultSound)
                .setVibrate(pattern);

        Notification notificacion = new NotificationCompat.BigTextStyle(builder)
                .bigText(t)
                .setBigContentTitle("Pemisos UNAH-TEC requiere su atención")
                .setSummaryText("Generar reportes")
                .build();

        notificationManager = (NotificationManager) contexto.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notificacion);



    }




}






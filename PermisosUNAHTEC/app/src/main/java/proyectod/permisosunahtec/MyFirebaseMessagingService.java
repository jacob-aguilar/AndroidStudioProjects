package proyectod.permisosunahtec;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import proyectod.permisosunahtec.syncs.SyncAdapterDeptos;
import proyectod.permisosunahtec.syncs.SyncAdapterEmpleados;
import proyectod.permisosunahtec.syncs.SyncAdapterPermisos;

import static android.content.ContentValues.TAG;
import static android.graphics.Color.rgb;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
int i=0;
    private String rolUsuario;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        cargarPreferencias();

        Log.i("NotificationPermisos", "aqui en else si me borra"+" "+remoteMessage.getData());

        if(remoteMessage.getData().toString().trim().equals("{sound=default, title=Se a eliminado su información}")){
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Intent intentInfo = new Intent(getApplicationContext(), Login.class);
            startActivity(intentInfo);


        }else {

        }


        try {
            if (remoteMessage.getData().isEmpty()) {
                Log.i("JUSTINO", "aqui en vacio");
                SyncAdapterDeptos.inicializarSyncAdapter(this);
                SyncAdapterDeptos.sincronizarAhora(this, true);



                SyncAdapterEmpleados.inicializarSyncAdapter(this);
                SyncAdapterEmpleados.sincronizarAhora(this, true);




                SyncAdapterPermisos.inicializarSyncAdapter(this);
                SyncAdapterPermisos.sincronizarAhora(this, true);
             }
            else {

                    SyncAdapterEmpleados.inicializarSyncAdapter(this);
                    SyncAdapterEmpleados.sincronizarAhora(this, true);
                    SyncAdapterDeptos.inicializarSyncAdapter(this);
                    SyncAdapterDeptos.sincronizarAhora(this, true);
                SyncAdapterPermisos.inicializarSyncAdapter(this);
                SyncAdapterPermisos.sincronizarAhora(this, true);



                showNotification(remoteMessage.getData());


            }

        }catch (Exception e) {

            if (remoteMessage.getData().isEmpty()) {
                Log.i("JUSTINO", "aqui en vacio");
                SyncAdapterEmpleados.inicializarSyncAdapter(this);
                SyncAdapterEmpleados.sincronizarAhora(this, true);
                SyncAdapterDeptos.inicializarSyncAdapter(this);
                SyncAdapterDeptos.sincronizarAhora(this, true);
                SyncAdapterPermisos.inicializarSyncAdapter(this);
                SyncAdapterPermisos.sincronizarAhora(this, true);


            } else {
                SyncAdapterEmpleados.inicializarSyncAdapter(this);
                SyncAdapterEmpleados.sincronizarAhora(this, true);
                SyncAdapterDeptos.inicializarSyncAdapter(this);
                SyncAdapterDeptos.sincronizarAhora(this, true);
                SyncAdapterPermisos.inicializarSyncAdapter(this);
                SyncAdapterPermisos.sincronizarAhora(this, true);


                showNotification(remoteMessage.getData());
                Log.i("JUSTINO", "aqui en is empit" + " " + remoteMessage.getData());

            }
        }

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
            } else {
                // Handle message within 10 seconds
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }




    public void showNotification(Map<String, String> data) {
        String title=null;
        String body=null;
        Intent intent;
        try {
             title = data.get("title").toString();
            body = data.get("body").toString();




        }catch (Exception e){

        }

        String NOTIFICATION_CHANNEL_ID = getString(R.string.default_notification_channel_id);

        if(rolUsuario.equals("admin")){
            intent = new Intent(this, ContenFragment.class);

        }else {
            //
            intent = new Intent(this, InformacionEmpleado.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setColor(rgb(255,160,0))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 1000, 500, 1000})
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

                .setContentInfo("info");

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("Descripcion");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableLights(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }
        i =(int) (Math.random()*1000 + 1);
        notificationManager.notify(i++ /* ID of notification */, notificationBuilder.build());
        Log.i("JUSTINTIN",""+i);
    }

    private void showNotification(String title, String body) {


        Intent intent;

        if(rolUsuario.equals("admin")){
            intent = new Intent(this, ContenFragment.class);

        }else {
            //
            intent = new Intent(this, InformacionEmpleado.class);
        }
         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //
        String NOTIFICATION_CHANNEL_ID = getString(R.string.default_notification_channel_id);


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setColor(rgb(255,160,0))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 1000, 500, 1000})
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

                .setContentInfo("info");

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("Descripcion");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
 i =(int) (Math.random()*1000 + 1);
        notificationManager.notify(i /* ID of notification */, notificationBuilder.build());
   Log.i("JUSTINTIN",""+i);

    }


    private void cargarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        rolUsuario = preferences.getString("rol", "");




    }

}

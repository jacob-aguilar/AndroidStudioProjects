package proyectod.permisosunahtec;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import proyectod.permisosunahtec.providers.ContractParaDepartamentos;
import proyectod.permisosunahtec.syncs.SyncAdapterDeptos;
import proyectod.permisosunahtec.syncs.SyncAdapterEmpleados;
import proyectod.permisosunahtec.syncs.SyncAdapterPermisos;

public class Internet extends BroadcastReceiver
{

    private boolean internet;

    @Override
    public void onReceive(final Context context, Intent intent)
    {


        internet =  isOnline(context);
        if(internet ==true){

            SyncAdapterDeptos.inicializarSyncAdapter(context);
            SyncAdapterDeptos.sincronizarAhora(context, true);

            String[] selectionArgs = new String[]{"1"};

            Cursor cursorDeparatamentos = context.getContentResolver().query(ContractParaDepartamentos.CONTENT_URI, null, "pendiente_insercion =?", selectionArgs, null);
            if(cursorDeparatamentos.getCount()>0){

            }else {
                SyncAdapterEmpleados.inicializarSyncAdapter(context);
                SyncAdapterEmpleados.sincronizarAhora(context, true);




                SyncAdapterPermisos.inicializarSyncAdapter(context);
                SyncAdapterPermisos.sincronizarAhora(context, true);

            }


         }else{
         }

    }


    public static boolean isOnline(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        RunnableFuture<Boolean> futureRun = new FutureTask<Boolean>(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if ((networkInfo .isAvailable()) && (networkInfo .isConnected())) {
                    try {
                        HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                        urlc.setRequestProperty("User-Agent", "Test");
                        urlc.setRequestProperty("Connection", "close");
                        urlc.setConnectTimeout(1500);
                        urlc.connect();

                        return (urlc.getResponseCode() == 200);
                    } catch (Exception e) {
                     }
                } else {
                 }
                return false;
            }
        });

        new Thread(futureRun).start();


        try {
            return futureRun.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }

    }

}
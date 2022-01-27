package proyectod.permisosunahtec.syncs;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Bound Service que interactua con el sync adapter para correr las sincronizaciones
 */
public class SyncServiceEmpleados extends Service {



    // Instancia del sync adapter

    private static SyncAdapterEmpleados syncAdapterEmpleados = null;
    // Objeto para prevenir errores entre hilos
    private static final Object lock = new Object();

    @Override
    public void onCreate() {


        synchronized (lock) {
            if(syncAdapterEmpleados==null){
                syncAdapterEmpleados = new SyncAdapterEmpleados(getApplicationContext(), true);
            }
        }
    }
    /**
     * Retorna interfaz de comunicación para que el sistema llame al sync adapter
     */
    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapterEmpleados.getSyncAdapterBinder() ;
    }
}
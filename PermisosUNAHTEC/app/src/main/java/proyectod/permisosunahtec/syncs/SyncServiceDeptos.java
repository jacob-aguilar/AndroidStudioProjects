package proyectod.permisosunahtec.syncs;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Bound Service que interactua con el sync adapter para correr las sincronizaciones
 */
public class SyncServiceDeptos extends Service {



    // Instancia del sync adapter
    private static SyncAdapterDeptos syncAdapter = null;

    // Objeto para prevenir errores entre hilos
    private static final Object lock = new Object();

    @Override
    public void onCreate() {


        synchronized (lock) {
            if (syncAdapter == null) {
                syncAdapter = new SyncAdapterDeptos(getApplicationContext(), true);

                
            }
        }
    }
    /**
     * Retorna interfaz de comunicación para que el sistema llame al sync adapter
     */
    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder()  ;
    }

}
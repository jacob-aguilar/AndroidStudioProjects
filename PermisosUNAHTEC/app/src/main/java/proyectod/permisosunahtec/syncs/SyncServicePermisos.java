package proyectod.permisosunahtec.syncs;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SyncServicePermisos extends Service {

    // Instancia del sync adapter
    private static SyncAdapterPermisos syncAdapter = null;

    // Objeto para prevenir errores entre hilos
    private static final Object lock = new Object();

    @Override
    public void onCreate() {


        synchronized (lock) {
            if (syncAdapter == null) {
                syncAdapter = new SyncAdapterPermisos(getApplicationContext(), true);


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

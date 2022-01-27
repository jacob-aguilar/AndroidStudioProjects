package proyectod.permisosunahtec;

import android.app.Service;
import android.database.Cursor;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import proyectod.permisosunahtec.providers.ContractParaDepartamentos;
import proyectod.permisosunahtec.providers.ContractParaEmpleados;
import proyectod.permisosunahtec.providers.ContractParaPermisos;
import proyectod.permisosunahtec.syncs.SyncAdapterDeptos;
import proyectod.permisosunahtec.syncs.SyncAdapterEmpleados;
import proyectod.permisosunahtec.syncs.SyncAdapterPermisos;

public class GCMService extends GcmTaskService {
    private Cursor cursorDeparatamentos;
    private Cursor cursorEmpleados;
    private Cursor cursorPermisos;

    @Override
    public int onRunTask(TaskParams taskParams) {

                 //bundle with other tasks and then execute

        String[] selectionArgs = new String[]{"1"};
        cursorDeparatamentos=  getApplicationContext().getContentResolver().query(ContractParaDepartamentos.CONTENT_URI, null,"pendiente_insercion =?",selectionArgs,null);
        cursorEmpleados=  getApplicationContext().getContentResolver().query(ContractParaEmpleados.CONTENT_URI, null,"pendiente_insercion =?",selectionArgs,null);
        cursorPermisos=  getApplicationContext().getContentResolver().query(ContractParaPermisos.CONTENT_URI, null,"pendiente_insercion =?",selectionArgs,null);

        if(cursorDeparatamentos.getCount()>0){
            SyncAdapterDeptos.inicializarSyncAdapter(getApplicationContext());


            SyncAdapterDeptos.sincronizarAhora(getApplicationContext(), true);

        }else {
            if (cursorEmpleados.getCount() > 0) {

                Cursor cursorDeparatamentos = getApplicationContext().getContentResolver().query(ContractParaDepartamentos.CONTENT_URI, null, "pendiente_insercion =?", selectionArgs, null);
                if (cursorDeparatamentos.getCount() > 0) {

                } else {
                    SyncAdapterEmpleados.inicializarSyncAdapter(getApplicationContext());


                    SyncAdapterEmpleados.sincronizarAhora(getApplicationContext(), true);
                }
            }
            if (cursorPermisos.getCount() > 0) {
                Cursor cursorDeparatamentos = getApplicationContext().getContentResolver().query(ContractParaDepartamentos.CONTENT_URI, null, "pendiente_insercion =?", selectionArgs, null);
                if (cursorDeparatamentos.getCount() > 0) {

                }else {

                    SyncAdapterPermisos.inicializarSyncAdapter(getApplicationContext());


                    SyncAdapterPermisos.sincronizarAhora(getApplicationContext(), true);
                }

            }
        }

        return GcmNetworkManager.RESULT_SUCCESS;

        }
    }


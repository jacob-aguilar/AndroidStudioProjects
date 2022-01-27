package proyectod.permisosunahtec.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Clase envoltura para el gestor de Bases de datos
 */
class DatabaseHelper extends SQLiteOpenHelper {


    private Context contexto;

    public DatabaseHelper(Context context,
                          String name,
                          SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context contexto) {
        super(contexto, "librito", null, 8);
        this.contexto = contexto;
    }

    public void onCreate(SQLiteDatabase database) {
        createTable(database);
        createTableEmpleados(database);
        createTablePermisos(database);
        // Crear la tabla "gasto"
    }
    

    /**
     * Crear tabla en la base de datos
     *
     * @param database Instancia de la base de datos
     */
    private void createTable(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractParaDepartamentos.DEPARTAMENTO + " (" +
                ContractParaDepartamentos.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractParaDepartamentos.Columnas.NOMBREDEPTO + " TEXT UNIQUE, " +
              //  ContractParaDepartamentos.Columnas.ETIQUETA + " TEXT, " +
             //   ContractParaDepartamentos.Columnas.FECHA + " TEXT, " +
               // ContractParaDepartamentos.Columnas.DESCRIPCION + " TEXT," +
                ContractParaDepartamentos.Columnas.ID_REMOTA + " TEXT UNIQUE," +
                ContractParaDepartamentos.Columnas.ESTADO + " INTEGER NOT NULL DEFAULT "+ ContractParaDepartamentos.ESTADO_OK+"," +
                ContractParaDepartamentos.Columnas.PENDIENTE_ELIMINACION + " INTEGER NOT NULL DEFAULT 0,"+
                ContractParaDepartamentos.Columnas.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }
    private void createTableEmpleados(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractParaEmpleados.USERS + " (" +
                ContractParaEmpleados.Columnas._ID + " INTEGER PRIMARY KEY, " +
                ContractParaEmpleados.Columnas.NAME + " TEXT UNIQUE , " +
                 ContractParaEmpleados.Columnas.EMAIL + " TEXT UNIQUE, " +
                   ContractParaEmpleados.Columnas.PASSWORD + " TEXT, " +
                ContractParaEmpleados.Columnas.PASSWORD_CONFIRMATION + " TEXT, " +
                 ContractParaEmpleados.Columnas.PICTURE+ " BLOG," +
                ContractParaEmpleados.Columnas.TOKENFIREBASE+ " TEXT," +
                ContractParaEmpleados.Columnas.CODDEPTO+ " TEXT," +
                ContractParaEmpleados.Columnas.ROL+ " TEXT," +
                ContractParaDepartamentos.Columnas.ID_REMOTA + " TEXT UNIQUE," +
                ContractParaDepartamentos.Columnas.ESTADO + " INTEGER NOT NULL DEFAULT "+ ContractParaDepartamentos.ESTADO_OK+"," +
                ContractParaDepartamentos.Columnas.PENDIENTE_ELIMINACION + " INTEGER NOT NULL DEFAULT 0,"+
                ContractParaDepartamentos.Columnas.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }
    private void createTablePermisos(SQLiteDatabase database) {
        String cmd = "CREATE TABLE " + ContractParaPermisos.PERMISOS + " (" +
                ContractParaPermisos.Columnas.ID + " INTEGER PRIMARY KEY, " +
                ContractParaPermisos.Columnas.TIPO_PERMISO + " TEXT NOT NULL, " +
                ContractParaPermisos.Columnas.ID_DEPTO + " TEXT NOT NULL, " +
                ContractParaPermisos.Columnas.ID_EMPLEADO + " TEXT NOT NULL, " +
                ContractParaPermisos.Columnas.FECHA_INICIO + " DATE NOT NULL, " +
                ContractParaPermisos.Columnas.FECHA_FIN + " DATE NOT NULL, " +
                ContractParaPermisos.Columnas.OBSERVACIONES + " TEXT , " +
                ContractParaPermisos.Columnas.ID_REMOTA + " TEXT UNIQUE," +
                ContractParaPermisos.Columnas.ESTADO + " INTEGER NOT NULL DEFAULT "+ ContractParaPermisos.ESTADO_OK+"," +
                ContractParaPermisos.Columnas.PENDIENTE_ELIMINACION + " INTEGER NOT NULL DEFAULT 0,"+
                ContractParaPermisos.Columnas.PENDIENTE_ARCHIVADO + " INTEGER NOT NULL DEFAULT 0,"+
                ContractParaPermisos.Columnas.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(cmd);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try { db.execSQL("drop table " + ContractParaDepartamentos.DEPARTAMENTO);
            db.execSQL("drop table " + ContractParaEmpleados.USERS);
            db.execSQL("drop table " + ContractParaPermisos.PERMISOS);
       }
        catch (SQLiteException e) { }
        onCreate(db);
    }

}
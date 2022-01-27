package proyectod.permisosunahtec.providers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

public final class OperacionesBaseDatos {

    private static DatabaseHelper baseDatos;

    private static OperacionesBaseDatos instancia = new OperacionesBaseDatos();
    private Cursor cursor;

    private OperacionesBaseDatos() {
    }

    public static OperacionesBaseDatos obtenerInstancia(Context contexto) {
        if (baseDatos == null) {
            baseDatos = new DatabaseHelper(contexto);
        }
        return instancia;
    }

    public Cursor obtenerJoinsDePermisos() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(JOINS_DEPTOS_EMPLEADOS_PERMISOS);
        String[] selectionArgs = {"0","0"};

        return builder.query(db, proyCabeceraPedido, "permisos.eliminado =? and permisos.archivado =?", selectionArgs, null, null, null);
     }
    public Cursor obtenerJoinsDePermisosFiltrado(String query) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(JOINS_DEPTOS_EMPLEADOS_PERMISOS);
        String[] selectionArgs = {"0","0",query,"0","0",query };

        return builder.query(db, proyCabeceraPedido, "permisos.eliminado =? and permisos.archivado =? and tipoPermiso like ? or permisos.eliminado =? and permisos.archivado =? and users.name like ?", selectionArgs, null, null, null);
    }
    public Cursor obtenerJoinsDePermisosFecha(String fechaInicio,String fechaFin) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(JOINS_DEPTOS_EMPLEADOS_PERMISOS);
        String[] selectionArgs = {"0","0",fechaInicio,fechaFin};


        return builder.query(db, proyCabeceraPedido, "permisos.eliminado =? and permisos.archivado =? and permisos.fechaInicio >= ? and permisos.fechaFin <= ?", selectionArgs, null, null, null);
    }

    public Cursor obtenerJoinsDePermisosDeptos(String idDepto) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(JOINS_DEPTOS_EMPLEADOS_PERMISOS);
        String[] selectionArgs = {"0","0",idDepto};


        return builder.query(db, proyCabeceraPedido, "permisos.eliminado =? and permisos.archivado =? and permisos.idDepartamento = ?", selectionArgs, null, null, null);
    }
    public Cursor obtenerJoinsDePermisosEmpleado(String idEmpleado) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(JOINS_DEPTOS_EMPLEADOS_PERMISOS);
        String[] selectionArgs = {"0","0",idEmpleado};


        return builder.query(db, proyCabeceraPedido, "permisos.eliminado =? and permisos.archivado =? and users._id = ?", selectionArgs, null, null, null);
    }
    public Cursor obtenerJoinsDePermisosTipoPermiso(String tipoPermiso) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(JOINS_DEPTOS_EMPLEADOS_PERMISOS);
        String[] selectionArgs = {"0","0",tipoPermiso};


        return builder.query(db, proyCabeceraPedido, "permisos.eliminado =? and permisos.archivado =? and permisos.tipoPermiso = ?", selectionArgs, null, null, null);
    }
    public Cursor obtenerJoinsDePermisosDeptoUsers(String idUser,String idDepto) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(JOINS_DEPTOS_EMPLEADOS_PERMISOS);
        String[] selectionArgs = {"0","0",idUser,idDepto};


        return builder.query(db, proyCabeceraPedido, "permisos.eliminado =? and permisos.archivado =? and users._id = ? and permisos.idDepartamento =?", selectionArgs, null, null, null);
    }
    public Cursor obtenerJoinsDePermisosDeptoPermiso(String tipoPermiso,String idDepto) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(JOINS_DEPTOS_EMPLEADOS_PERMISOS);
        String[] selectionArgs = {"0","0",tipoPermiso,idDepto};


        return builder.query(db, proyCabeceraPedido, "permisos.eliminado =? and permisos.archivado =? and permisos.tipoPermiso = ? and permisos.idDepartamento =?", selectionArgs, null, null, null);
    }

    public Cursor obtenerJoinsDePermisosEmpleadoPermiso(String tipoPermiso,String idUser) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(JOINS_DEPTOS_EMPLEADOS_PERMISOS);
        String[] selectionArgs = {"0","0",tipoPermiso,idUser};


        return builder.query(db, proyCabeceraPedido, "permisos.eliminado =? and permisos.archivado =? and permisos.tipoPermiso = ? and users._id =?", selectionArgs, null, null, null);
    }

    public Cursor obtenerJoinsDePermisosFechaEmpleado(String fechaInicio,String fechaFin ,String idUser) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(JOINS_DEPTOS_EMPLEADOS_PERMISOS);
        String[] selectionArgs = {"0","0",fechaInicio,fechaFin,idUser};


        return builder.query(db, proyCabeceraPedido, "permisos.eliminado =? and permisos.archivado =? and permisos.FechaInicio >= ? and permisos.fechaFin <=? and users._id =?", selectionArgs, null, null, null);
    }
    public Cursor obtenerJoinsDePermisosFechaDepto(String fechaInicio,String fechaFin ,String idDepartamento) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(JOINS_DEPTOS_EMPLEADOS_PERMISOS);
        String[] selectionArgs = {"0","0",fechaInicio,fechaFin,idDepartamento};


        return builder.query(db, proyCabeceraPedido, "permisos.eliminado =? and permisos.archivado =? and permisos.FechaInicio >= ? and permisos.fechaFin <=? and permisos.idDepartamento =?", selectionArgs, null, null, null);
    }
    public Cursor obtenerJoinsDePermisosFechaPermiso(String fechaInicio,String fechaFin ,String tipoPermiso) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(JOINS_DEPTOS_EMPLEADOS_PERMISOS);
        String[] selectionArgs = {"0","0",fechaInicio,fechaFin,tipoPermiso};


        return builder.query(db, proyCabeceraPedido, "permisos.eliminado =? and permisos.archivado =? and permisos.FechaInicio >= ? and permisos.fechaFin <=? and permisos.tipoPermiso =?", selectionArgs, null, null, null);
    }
    public Cursor obtenerJoinsDePermisosDeptoEmpleadoPermiso(String idDepto,String idUser ,String tipoPermiso) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(JOINS_DEPTOS_EMPLEADOS_PERMISOS);
        String[] selectionArgs = {"0","0",idDepto,idUser,tipoPermiso};


        return builder.query(db, proyCabeceraPedido, "permisos.eliminado =? and permisos.archivado =? and permisos.idDepartamento = ? and users._id =? and permisos.tipoPermiso =?", selectionArgs, null, null, null);
    }

    public Cursor obtenerJoinsDePermisosFechaPermisoEmpleado(String fechaInicio,String fechaFin ,String tipoPermiso, String idUser) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(JOINS_DEPTOS_EMPLEADOS_PERMISOS);
        String[] selectionArgs = {"0","0",fechaInicio,fechaFin,tipoPermiso,idUser};


        return builder.query(db, proyCabeceraPedido, "permisos.eliminado =? and permisos.archivado =? and permisos.fechaInicio >=? and permisos.fechaFin <=? and permisos.tipoPermiso =? and users._id =?", selectionArgs, null, null, null);
    }
    public Cursor obtenerJoinsDePermisosFechaDeptoEmpleado(String fechaInicio,String fechaFin ,String idDepto, String idUser) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(JOINS_DEPTOS_EMPLEADOS_PERMISOS);
        String[] selectionArgs = {"0","0",fechaInicio,fechaFin,idDepto,idUser};


        return builder.query(db, proyCabeceraPedido, "permisos.eliminado =? and permisos.archivado =? and permisos.fechaInicio >=? and permisos.fechaFin <=? and permisos.idDepartamento =? and users._id =?", selectionArgs, null, null, null);
    }
    public Cursor obtenerJoinsDePermisosFechaPermisoDepto(String fechaInicio,String fechaFin ,String tipoPermiso, String idDepto) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(JOINS_DEPTOS_EMPLEADOS_PERMISOS);
        String[] selectionArgs = {"0","0",fechaInicio,fechaFin,tipoPermiso,idDepto};


        return builder.query(db, proyCabeceraPedido, "permisos.eliminado =? and permisos.archivado =? and permisos.fechaInicio >=? and permisos.fechaFin <=? and permisos.tipoPermiso =? and permisos.idDepartamento =?", selectionArgs, null, null, null);
    }
    public Cursor obtenerJoinsDePermisosFechaPermisoDeptoUser(String fechaInicio,String fechaFin ,String tipoPermiso, String idDepto,String idUser) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(JOINS_DEPTOS_EMPLEADOS_PERMISOS);
        String[] selectionArgs = {"0","0",fechaInicio,fechaFin,tipoPermiso,idDepto,idUser};


        return builder.query(db, proyCabeceraPedido, "permisos.eliminado =? and permisos.archivado =? and permisos.fechaInicio >=? and permisos.fechaFin <=? and permisos.tipoPermiso =? and permisos.idDepartamento =? and users._id =?", selectionArgs, null, null, null);
    }

    public Cursor obtenerJoinsDePermisosIdUSer(String idLocal) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(JOINS_DEPTOS_EMPLEADOS_PERMISOS);
        String[] selectionArgs = {idLocal};


        return builder.query(db, proyCabeceraPedido, "permisos.id =?", selectionArgs, null, null, null);
    }

    private static final String JOINS_DEPTOS_EMPLEADOS_PERMISOS = "permisos " +
            "INNER JOIN users " +
            "ON users._id = permisos.idEmpleado INNER JOIN departamentos ON permisos.idDepartamento = departamentos.idRemota or permisos.idDepartamento = departamentos._id ";


    private final String[] proyCabeceraPedido = new String[]{
            ContractParaPermisos.PERMISOS + "." + ContractParaPermisos.Columnas.ID,
            "permisos."+ContractParaPermisos.Columnas.ID_REMOTA,
            ContractParaPermisos.Columnas.TIPO_PERMISO,
            ContractParaPermisos.Columnas.ID_EMPLEADO,
            ContractParaPermisos.Columnas.ID_DEPTO,
            "permisos."+ContractParaPermisos.Columnas.PENDIENTE_INSERCION,
            ContractParaPermisos.Columnas.OBSERVACIONES,
            ContractParaDepartamentos.Columnas.NOMBREDEPTO,
            ContractParaEmpleados.Columnas.NAME,
            ContractParaPermisos.Columnas.FECHA_INICIO,
            ContractParaPermisos.Columnas.FECHA_FIN};



    public Cursor obtenerCabeceraPorId(String id) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String selection = String.format("%s=?", ContractParaPermisos.Columnas.PENDIENTE_ELIMINACION);
        String[] selectionArgs = {id};

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(JOINS_DEPTOS_EMPLEADOS_PERMISOS);



        String[] proyeccion = {
                ContractParaPermisos.PERMISOS + "." + ContractParaPermisos.Columnas.ID,
                ContractParaPermisos.Columnas.TIPO_PERMISO,
                ContractParaPermisos.Columnas.ID_EMPLEADO,
                ContractParaDepartamentos.Columnas.NOMBREDEPTO,
                ContractParaEmpleados.Columnas.NAME,
                ContractParaPermisos.Columnas.FECHA_INICIO,
                ContractParaPermisos.Columnas.FECHA_FIN};

        return builder.query(db, proyeccion, "", selectionArgs, null, null, null);
    }
}

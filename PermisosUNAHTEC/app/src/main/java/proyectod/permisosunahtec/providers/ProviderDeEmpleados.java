package proyectod.permisosunahtec.providers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;


/**
 * Content Provider personalizado para los gastos
 */
public class ProviderDeEmpleados extends ContentProvider {
    /**
     * Nombre de la base de datos
     */
    private static final String DATABASE_NAME = "librito";
    /**
     * Versión actual de la base de datos
     */
    private static final int DATABASE_VERSION = 8;
    /**
     * Instancia global del Content Resolver
     */
    private ContentResolver resolver;
    /**
     * Instancia del administrador de BD
     */
    private DatabaseHelper databaseHelper;


    @Override
    public boolean onCreate() {
        // Inicializando gestor BD
        databaseHelper = new DatabaseHelper(
                getContext(),
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );

        resolver = getContext().getContentResolver();

        return true;
    }

    @Override
    public Cursor query(Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {

        // Obtener base de datos
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        // Comparar Uri
        int match = ContractParaEmpleados.uriMatcher.match(uri);

        Cursor c =null;

        switch (match) {
            case ContractParaEmpleados.ALLROWS:
                // Consultando todos los registros

                try {
                    c = db.query(ContractParaEmpleados.USERS, projection,
                            selection, selectionArgs,
                            null, null, sortOrder);
                    c.setNotificationUri(
                            resolver,
                            ContractParaEmpleados.CONTENT_URI);
                }catch (Exception e){

                }

                break;
            case ContractParaEmpleados.SINGLE_ROW:
                // Consultando un solo registro basado en el Id del Uri
                long idGasto = ContentUris.parseId(uri);
                c = db.query(ContractParaEmpleados.USERS, projection,
                        ContractParaEmpleados.Columnas._ID + " = " + idGasto,
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        ContractParaEmpleados.CONTENT_URI);
                break;
            default:
                throw new IllegalArgumentException("URI no soportada: " + uri);
        }
        return c;

    }

    @Override
    public String getType(Uri uri) {
        switch (ContractParaEmpleados.uriMatcher.match(uri)) {
            case ContractParaEmpleados.ALLROWS:
                return ContractParaEmpleados.MULTIPLE_MIME;
            case ContractParaEmpleados.SINGLE_ROW:
                return ContractParaEmpleados.SINGLE_MIME;
            default:
                throw new IllegalArgumentException("Tipo de gasto desconocido: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Validar la uri
        if (ContractParaEmpleados.uriMatcher.match(uri) != ContractParaEmpleados.ALLROWS) {
            throw new IllegalArgumentException("URI desconocida : " + uri);
        }
        ContentValues contentValues;
        if (values != null) {
            contentValues = new ContentValues(values);
        } else {
            contentValues = new ContentValues();
        }

        // Inserción de nueva fila
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        long rowId = db.insert(ContractParaEmpleados.USERS, null, contentValues);
        if (rowId > 0) {
            Uri uri_gasto = ContentUris.withAppendedId(
                    ContractParaEmpleados.CONTENT_URI, rowId);
            resolver.notifyChange(uri_gasto, null, false);
            return uri_gasto;
        }
        throw new SQLException("Falla al insertar fila en : " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int match = ContractParaEmpleados.uriMatcher.match(uri);
        int affected;

        switch (match) {
            case ContractParaEmpleados.ALLROWS:
                affected = db.delete(ContractParaEmpleados.USERS,
                        selection,
                        selectionArgs);
                break;
            case ContractParaEmpleados.SINGLE_ROW:
                long idGasto = ContentUris.parseId(uri);
                affected = db.delete(ContractParaEmpleados.USERS,
                        ContractParaEmpleados.Columnas.ID_REMOTA + "=" + idGasto
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                // Notificar cambio asociado a la uri

                break;
            default:
                throw new IllegalArgumentException("Elemento gasto desconocido: " +
                        uri);
        }
        resolver.notifyChange(uri, null, false);
        return affected;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int affected;
        switch (ContractParaEmpleados.uriMatcher.match(uri)) {
            case ContractParaEmpleados.ALLROWS:
                affected = db.update(ContractParaEmpleados.USERS, values,
                        selection, selectionArgs);
                break;
            case ContractParaEmpleados.SINGLE_ROW:
                String idGasto = uri.getPathSegments().get(1);
                affected = db.update(ContractParaEmpleados.USERS, values,
                        ContractParaEmpleados.Columnas.ID_REMOTA + "=" + idGasto
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("URI desconocida: " + uri);
        }
        resolver.notifyChange(uri, null,false);

        return affected;
    }

}
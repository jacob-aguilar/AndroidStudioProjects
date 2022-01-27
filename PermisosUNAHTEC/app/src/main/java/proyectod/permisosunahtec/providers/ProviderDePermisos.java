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

import proyectod.permisosunahtec.PermisoFragment;

public class ProviderDePermisos extends ContentProvider {
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
        int match = ContractParaPermisos.uriMatcher.match(uri);

        Cursor c =null;

        switch (match) {
            case ContractParaPermisos.ALLROWS:
                // Consultando todos los registros

                try {
                    c = db.query(ContractParaPermisos.PERMISOS, projection,
                            selection, selectionArgs,
                            null, null, sortOrder);
                    c.setNotificationUri(
                            resolver,
                            ContractParaPermisos.CONTENT_URI);
                }catch (Exception e){

                }

                break;
            case ContractParaPermisos.SINGLE_ROW:
                // Consultando un solo registro basado en el Id del Uri
                long idGasto = ContentUris.parseId(uri);
                c = db.query(ContractParaPermisos.PERMISOS, projection,
                        ContractParaPermisos.Columnas._ID + " = " + idGasto,
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        ContractParaPermisos.CONTENT_URI);
                break;
            default:
                throw new IllegalArgumentException("URI no soportada: " + uri);
        }
        return c;

    }

    @Override
    public String getType(Uri uri) {
        switch (ContractParaPermisos.uriMatcher.match(uri)) {
            case ContractParaPermisos.ALLROWS:
                return ContractParaPermisos.MULTIPLE_MIME;
            case ContractParaPermisos.SINGLE_ROW:
                return ContractParaPermisos.SINGLE_MIME;
            default:
                throw new IllegalArgumentException("Tipo de permiso desconocido: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Validar la uri
        if (ContractParaPermisos.uriMatcher.match(uri) != ContractParaPermisos.ALLROWS) {
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

        long rowId = db.insert(ContractParaPermisos.PERMISOS, null, contentValues);
        if (rowId > 0) {
            Uri uri_gasto = ContentUris.withAppendedId(
                    ContractParaPermisos.CONTENT_URI, rowId);
            resolver.notifyChange(uri_gasto, null, false);


            return uri_gasto;
        }
        throw new SQLException("Falla al insertar fila en : " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int match = ContractParaPermisos.uriMatcher.match(uri);
        int affected;

        switch (match) {
            case ContractParaPermisos.ALLROWS:
                affected = db.delete(ContractParaPermisos.PERMISOS,
                        selection,
                        selectionArgs);
                break;
            case ContractParaPermisos.SINGLE_ROW:
                long idGasto = ContentUris.parseId(uri);
                affected = db.delete(ContractParaPermisos.PERMISOS,
                        ContractParaPermisos.Columnas.ID_REMOTA + "=" + idGasto
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                // Notificar cambio asociado a la uri

                break;
            default:
                throw new IllegalArgumentException("Elemento permiso desconocido: " +
                        uri);
        }
        resolver.notifyChange(uri, null, false);
        return affected;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int affected;
        switch (ContractParaPermisos.uriMatcher.match(uri)) {
            case ContractParaPermisos.ALLROWS:
                affected = db.update(ContractParaPermisos.PERMISOS, values,
                        selection, selectionArgs);
                break;
            case ContractParaPermisos.SINGLE_ROW:
                String idGasto = uri.getPathSegments().get(1);
                affected = db.update(ContractParaPermisos.PERMISOS, values,
                        ContractParaPermisos.Columnas.ID_REMOTA + "=" + idGasto
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

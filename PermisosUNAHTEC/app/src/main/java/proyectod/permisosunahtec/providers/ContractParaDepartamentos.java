package proyectod.permisosunahtec.providers;


import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract Class entre el provider y las aplicaciones
 */
public class ContractParaDepartamentos {
    /**
     * Autoridad del Content Provider
     */
    public final static String AUTHORITY = "com.herprogramacion.permisos_unah";
    /**
     * Representación de la tabla a consultar
     */
    public static final String DEPARTAMENTO = "departamentos";
    public static final String ELIMINADO = "eliminados";
    /**
     * Tipo MIME que retorna la consulta de una sola fila
     */
    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + AUTHORITY + DEPARTAMENTO;

    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + AUTHORITY + DEPARTAMENTO;
    /**
     * URI de contenido principal
     */
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + DEPARTAMENTO);

    public final static Uri CONTENT_URIDELETE =
            Uri.parse("content://" + AUTHORITY + "/" + ELIMINADO);
    /**
     * Comparador de URIs de contenido
     */
    public static final UriMatcher uriMatcher;
    /**
     * Código para URIs de multiples registros
     */
    public static final int ALLROWS = 1;
    /**
     * Código para URIS de un solo registro
     */
    public static final int SINGLE_ROW = 2;


    // Asignación de URIs
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, DEPARTAMENTO, ALLROWS);
        uriMatcher.addURI(AUTHORITY, DEPARTAMENTO + "/#", SINGLE_ROW);
    }

    // Valores para la columna ESTADO
    public static final int ESTADO_OK = 0;
    public static final int ESTADO_SYNC = 1;


    /**
     * Estructura de la tabla
     */
    public static class Columnas implements BaseColumns {

        private Columnas() {
            // Sin instancias
        }

        public final static String ID = "_id";
        public final static String NOMBREDEPTO = "nombreDepto";


        public static final String ESTADO = "estado";
        public static final String ID_REMOTA = "idRemota";
        public final static String PENDIENTE_INSERCION = "pendiente_insercion";
        public final static String PENDIENTE_ELIMINACION = "eliminado";



    }
}
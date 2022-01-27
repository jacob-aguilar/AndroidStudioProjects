package proyectod.permisosunahtec.web;

public class DepartamentosGetYSet {
    public String id;
    public String nombreDepto;
    public Integer idRemota;
    public String etiqueta;
    public String fecha;
    public String descripcion;


    public DepartamentosGetYSet(String idGasto, String nombreDepto,Integer idRemota, String etiqueta, String fecha, String descripcion) {
        this.id = idGasto;
        this.nombreDepto = nombreDepto;
        this.etiqueta = etiqueta;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.idRemota = idRemota;
    }

    public DepartamentosGetYSet() {

    }

    public Integer getIdRemota() {
        return idRemota;
    }

    public void setIdRemota(Integer idRemota) {
        this.idRemota = idRemota;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreDepto() {
        return nombreDepto;
    }

    public void setNombreDepto(String nombreDepto) {
        this.nombreDepto = nombreDepto;
    }
}
package proyectod.permisosunahtec.pojo;

import java.io.Serializable;

public class PermisosGetYSet  implements Serializable {

    public String id;
    public   String tipoPermiso;
    public   String idEmpleado;
    public   String idDepartamento;
    public   String nameEmpleado;

    public   String fechaInicio;
    public   String fechaFin;
    public String observaciones;
    public String archivado;

    public PermisosGetYSet(String nameEmpleado,String id, String tipoPermiso, String idEmpleado, String idDepartamento, String fechaInicio, String fechaFin, String observaciones,String archivado) {
        this.id = id;
        this.tipoPermiso = tipoPermiso;
        this.idEmpleado = idEmpleado;
        this.idDepartamento = idDepartamento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.observaciones = observaciones;
        this.observaciones = archivado;
        this.nameEmpleado = nameEmpleado;
    }




    public PermisosGetYSet() {

    }

    public String getNameEmpleado() {
        return nameEmpleado;
    }

    public void setNameEmpleado(String nameEmpleado) {
        this.nameEmpleado = nameEmpleado;
    }

    public String getArchivado() {
        return archivado;
    }

    public void setArchivado(String archivado) {
        this.archivado = archivado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getTipoPermiso() {
        return tipoPermiso;
    }

    public void setTipoPermiso(String tipoPermiso) {
        this.tipoPermiso = tipoPermiso;
    }

    public String getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(String idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) { this.observaciones = observaciones;
    }


}

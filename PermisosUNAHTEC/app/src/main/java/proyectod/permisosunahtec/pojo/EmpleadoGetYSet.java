package proyectod.permisosunahtec.pojo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.Serializable;

public class EmpleadoGetYSet implements Serializable {

    private int id;
    private String nombre;
    private String email;
    private String password;
    private String codEmpleado;
    private int codDepto;
    private String rol;
    private  String datosImagen;
    private transient Bitmap imagen;


    public EmpleadoGetYSet(String rol,int id, String nombre, String email, String password, String codEmpleado, int codDepto, String datosImagen, Bitmap imagen) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.password = password;
        this.codEmpleado = codEmpleado;
        this.codDepto = codDepto;
        this.datosImagen = datosImagen;
        this.imagen = imagen;
    }

    public EmpleadoGetYSet() {

    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodEmpleado() {
        return codEmpleado;
    }

    public void setCodEmpleado(String codEmpleado) {
        this.codEmpleado = codEmpleado;
    }

    public int getCodDepto() {
        return codDepto;
    }

    public void setCodDepto(int codDepto) {
        this.codDepto = codDepto;
    }

    public String getDatosImagen() {
        return datosImagen;
    }

    public void setDatosImagen(String datosImagen) {
        this.datosImagen = datosImagen;

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

                byte[] bytes = Base64.decode(datosImagen, Base64.DEFAULT);
                this.imagen = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,options);


        }catch (OutOfMemoryError exc){

            this.imagen = null;
        }
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}

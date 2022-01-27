package proyectod.permisosunahtec.web;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class PojoEmpleados {

    public String id;
    public String name;
    public String email;
    public String password;
    public String picture;
    public String tokenFirebase;
    public String codDepartamento;
    public String rol;
    private transient Bitmap imagen;

    public PojoEmpleados(String id, String name, String email, String password, String picture, String tokenFirebase, String codDepartamento, String rol) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.picture = picture;
        this.tokenFirebase = tokenFirebase;
        this.codDepartamento = codDepartamento;
        this.rol = rol;
    }

    public PojoEmpleados() {

    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;


        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            if(picture !=null) {

                byte[] bytes = Base64.decode(picture, Base64.DEFAULT);
                this.imagen = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            }


        }catch (OutOfMemoryError exc){

            this.imagen = null;
        }
    }

    public String getTokenFirebase() {
        return tokenFirebase;
    }

    public void setTokenFirebase(String tokenFirebase) {
        this.tokenFirebase = tokenFirebase;
    }

    public String getCodDepartamento() {
        return codDepartamento;
    }

    public void setCodDepartamento(String codDepartamento) {
        this.codDepartamento = codDepartamento;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}

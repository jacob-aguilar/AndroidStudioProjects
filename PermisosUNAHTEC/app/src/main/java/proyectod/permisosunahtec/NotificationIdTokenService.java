package proyectod.permisosunahtec;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

import proyectod.permisosunahtec.utils.Utilidades;

public class NotificationIdTokenService extends FirebaseInstanceIdService {
private static  final String TAG ="FIREBASE_TOKENJUS";
    private String idAdmin;
    String refrescheToken;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
         refrescheToken = FirebaseInstanceId.getInstance().getToken();
       cargarPreferencias();
        guardarToken(refrescheToken);
        enviarTokenRegistro(refrescheToken);

if(idAdmin.equals("")){


}else {


    updateToken();
}

    }

    public void guardarToken(String token){
        SharedPreferences preferences = getSharedPreferences("credenciales",Context.MODE_PRIVATE);



        SharedPreferences.Editor editor =preferences.edit();

        editor.putString("tokenFirebase",token);
        editor.apply();
    }

    private void enviarTokenRegistro(String refrescheToken) {
        Log.d(TAG,"RefrechedToken:"+refrescheToken);
    }






    private void cargarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);


        idAdmin = preferences.getString("id","");


    }

    private void updateToken(  ) {

        String REGISTER_URL = Utilidades.URL+"api/auth/updateToken/"+idAdmin;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                         Log.d(TAG,""+idAdmin);

                    }},new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(error.toString().equals("com.android.volley.ServerError"))
                {
                    Toast.makeText(getApplicationContext(), "Presentamos problemas intentelo mas tarde." , Toast.LENGTH_LONG).show();

                }
                else
                if(error.toString().equals("com.android.volley.TimeoutError")){
                    Toast.makeText(getApplicationContext(), "Revise su conexión a internet" , Toast.LENGTH_LONG).show();
                }else
                {
                    Toast.makeText(getApplicationContext(), "Fracaso del token" , Toast.LENGTH_LONG).show();



                } Log.d(TAG,""+idAdmin); }}){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put("tokenFirebase",refrescheToken);



                return params;}};
        RequestQueue requestQueue = Volley.newRequestQueue(this);requestQueue.add(stringRequest);
    }



}

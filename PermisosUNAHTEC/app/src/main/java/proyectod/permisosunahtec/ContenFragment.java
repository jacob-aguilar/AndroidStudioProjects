package proyectod.permisosunahtec;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import proyectod.permisosunahtec.utils.Utilidades;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public class ContenFragment extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Object> {

     private String[] icons = {

            "Deptos",
            "Empleados",
            "Permisos"


    };
    public static ViewPager fragmentsPager;
    public static TabLayout tabItems;
    private String idAdmin;
    private String refrescheToken;
    private  boolean internet;
    private static final String WIFI_STATE_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";



    Internet broadcastReceiver =null;

    private GcmNetworkManager gcmNetworkManager;
    private GcmNetworkManager mGcmNetworkManager;
    private String tokenFirebase;

    @Override
    protected void onDestroy() {


        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();

        cargarPreferencias();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenfragment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Permisos UNAH-TEC");
        setSupportActionBar(toolbar);


        gcmNetworkManager = GcmNetworkManager.getInstance(this);
         servicio();
         registerForNetworkChangeEvents(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);

        registerReceiver(broadcastReceiver, intentFilter);

          hiloTArea();

        getSupportActionBar();






        fragmentsPager = (ViewPager) findViewById(R.id.fragmentsPager);


        tabItems = (TabLayout) findViewById(R.id.tablaItems);
        fragmentsPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabItems.setupWithViewPager(fragmentsPager);
         cargarPreferencias();

        for (int i = 0; i < tabItems.getTabCount(); i++) {
            TabLayout.Tab tab = tabItems.getTabAt(i);
            if (tab != null) {
                tab.setText("" + icons[i]);
            }
        }
        refrescheToken = FirebaseInstanceId.getInstance().getToken();
        cargarPreferencias();


        updateToken();
        
    }

    private void cargarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("credenciales",Context.MODE_PRIVATE);

        String user = preferences.getString("user","");
        String pass = preferences.getString("pass","");
        tokenFirebase = preferences.getString("token","");
        Utilidades.TOKEN = tokenFirebase;
        idAdmin= preferences.getString("id","");



    } private void updateToken(  ) {

        String REGISTER_URL = Utilidades.URL+"api/auth/updateToken/"+idAdmin;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }},new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

               }}){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put("tokenFirebase",refrescheToken);



                return params;}
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer" + " " + tokenFirebase);


                return params;
            }};
        RequestQueue requestQueue = Volley.newRequestQueue(this);requestQueue.add(stringRequest);
    }


    public void servicio() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());


        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 0);



        if (Calendar.getInstance().after(cal)) {
            cal.add(Calendar.DAY_OF_MONTH, 1);


        }

        Intent intent = new Intent(getApplicationContext(), ServicioNotificacion.class);

        final PendingIntent pIntent = PendingIntent.getBroadcast(this, ServicioNotificacion.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long firstMillis = System.currentTimeMillis(); //first run of alarm is immediate // aranca la palicacion

        int intervalo = 60000 ;
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pIntent);


    }



    public static void registerForNetworkChangeEvents(final Context context) {
      Internet networkStateChangeReceiver= new Internet();
        context.registerReceiver(networkStateChangeReceiver, new IntentFilter(CONNECTIVITY_ACTION));
        context.registerReceiver(networkStateChangeReceiver, new IntentFilter(WIFI_STATE_CHANGE_ACTION));
    }



    @NonNull
    @Override
    public Loader<Object> onCreateLoader(int i, @Nullable Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Object> loader, Object o) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Object> loader) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    public  void hiloTArea(){
        Task task = new PeriodicTask.Builder()
                .setService(GCMService.class)
                .setPeriod(60)
                .setFlex(10)
                .setTag("hilo")
                .setPersisted(true)
                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                .build();

        gcmNetworkManager.schedule(task);
    }

}

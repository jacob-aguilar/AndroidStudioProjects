package com.example.personal.login_webservice;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


@RequiresApi(api = Build.VERSION_CODES.N)
public class horarios_traumotologia extends AppCompatActivity {
    CardView cit,cit1,cit2,cit3,cit4,cit5,cit6,cit7,cit8,cit9,cit10,cit11,cit12,cit13,cit14,cit15,cit16,cit17,cit18,cit19,cit20,cit21,cit22,cit23,cit24,cit25,cit26,cit27,cit28,cit29,cit30,cit31;
    Button bfecha;
    int unos;

    TextView doc, efecha,esp,horario,horario1,horario2,horario3,horario4,horario5,horario6,horario7,horario8,horario9,horario10,horario11,horario12,horario13,horario14,horario15,
            horario32,horario16,horario17,horario18,horario19,horario20,horario21,horario22,horario23,horario24,horario25,horario26,horario27,horario28,horario29,horario30,horario31;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final String TAG = "horarios_traumotologia";
    String dat,dats;
    String uno="CARDIOLOGIA";
    String dos="uno";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            setContentView(R.layout.activity_horarios_traumotologia);
            //horarios cardview-----------------------------------------
            cit = (CardView) findViewById(R.id.ochoquince);
            cit1 = (CardView) findViewById(R.id.ochoymedia);
            cit2 = (CardView) findViewById(R.id.ochoy45);
            cit3 = (CardView) findViewById(R.id.nueve);
            cit4 = (CardView) findViewById(R.id.nuevequince);
            cit5 = (CardView) findViewById(R.id.nuevey30);
            cit6 = (CardView) findViewById(R.id.nueve45);
            cit7 = (CardView) findViewById(R.id.diez);
            cit8 = (CardView) findViewById(R.id.diez15);
            cit9 = (CardView) findViewById(R.id.diez30);
            cit10 = (CardView) findViewById(R.id.diez45);
            cit11= (CardView) findViewById(R.id.once);
            cit12= (CardView) findViewById(R.id.once15);
            cit13 = (CardView) findViewById(R.id.once30);
            cit14 = (CardView) findViewById(R.id.once45);
            cit15 = (CardView) findViewById(R.id.doce);
            cit16 = (CardView) findViewById(R.id.doce15);
            cit17 = (CardView) findViewById(R.id.doce30);
            cit18 = (CardView) findViewById(R.id.una45);
            cit19 = (CardView) findViewById(R.id.dos);
            cit20 = (CardView) findViewById(R.id.dos15);
            cit21 = (CardView) findViewById(R.id.dos30);
            cit22 = (CardView) findViewById(R.id.dos45);
            cit23 = (CardView) findViewById(R.id.tres);
            cit24 = (CardView) findViewById(R.id.tres15);
            cit25 = (CardView) findViewById(R.id.tres30);
            cit26 = (CardView) findViewById(R.id.tres45);
            cit27 = (CardView) findViewById(R.id.cuatro);
            cit28 = (CardView) findViewById(R.id.cuatro15);
            cit29 = (CardView) findViewById(R.id.cuatro30);
            cit30 = (CardView) findViewById(R.id.cuatro45);
            cit31 = (CardView) findViewById(R.id.cinco);



            //-------------------------fecha-----------------------------
            bfecha = (Button) findViewById(R.id.btn_fecha);

            //horarios textview-----------------------------------------
            horario =  (TextView) findViewById(R.id.horariosochoquinceam);
            horario1 = (TextView) findViewById(R.id.turno_2);
            horario2 = (TextView) findViewById(R.id.turno_3);
            horario3 = (TextView) findViewById(R.id.turno_4);
            horario4 = (TextView) findViewById(R.id.turno_5);
            horario5 = (TextView) findViewById(R.id.turno_6);
            horario7 = (TextView) findViewById(R.id.turno_7);
            horario8 = (TextView) findViewById(R.id.turno_8);
            horario9 = (TextView) findViewById(R.id.turno_9);
            horario10 = (TextView) findViewById(R.id.turno_10);
            horario11 = (TextView) findViewById(R.id.turno_11);
            horario12= (TextView) findViewById(R.id.turno_12);
            horario13 = (TextView) findViewById(R.id.turno_13);
            horario14 = (TextView) findViewById(R.id.turno_14);
            horario15= (TextView) findViewById(R.id.turno_15);
            horario16 = (TextView) findViewById(R.id.turno_16);
            horario17 = (TextView) findViewById(R.id.turno_17);
            horario18 = (TextView) findViewById(R.id.turno_18);
            horario19 = (TextView) findViewById(R.id.turno_19);
            horario20 = (TextView) findViewById(R.id.turno_20);
            horario21 = (TextView) findViewById(R.id.turno_21);
            horario22 = (TextView) findViewById(R.id.turno_22);
            horario23 = (TextView) findViewById(R.id.turno_23);
            horario24 = (TextView) findViewById(R.id.turno_24);
            horario25 = (TextView) findViewById(R.id.turno_25);
            horario26 = (TextView) findViewById(R.id.turno_26);
            horario27 = (TextView) findViewById(R.id.turno_27);
            horario28 = (TextView) findViewById(R.id.turno_28);
            horario29 = (TextView) findViewById(R.id.turno_29);
            horario30 = (TextView) findViewById(R.id.turno_30);
            horario31 = (TextView) findViewById(R.id.turno_31);
            horario32 =  (TextView) findViewById(R.id.turno_32);
            ;




            esp = (TextView) findViewById(R.id.texview_traumatologia);
            doc = (TextView) findViewById(R.id.doctorarea);
            efecha = (TextView) findViewById(R.id.txt_fecha);
            bfecha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Calendar cal = null;

                    cal = Calendar.getInstance();

                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            horarios_traumotologia.this,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            mDateSetListener,
                            year,month,day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            });



            mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month + 1;
                    Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                    String date =  day + "/" + month + "/" + year;
                    efecha.setText(date);
                }
            };


            Bundle bundles = getIntent().getExtras();
            dat = bundles.getString("uno");
            String dats = bundles.getString("dos");
            esp.setText(dat);
            doc.setText(dats);



            //------------------------------------datos para el activity final-------------//
            cit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    try {
                        Intent i = new Intent(getApplication(), Main2Activity.class);
                        i.putExtra("ESPECIALIDAD", esp.getText().toString());
                        i.putExtra("FECHA", efecha.getText().toString());
                        i.putExtra("HORARIO", horario.getText().toString());
                        i.putExtra("DOCTOR", doc.getText().toString());


                        startActivity(i);

                    }catch (Exception e){
                        Toast.makeText(getApplication(),e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });


            cit1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(),  Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario1.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario2.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario3.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });

            cit4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario4.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario5.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario7.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(),Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario8.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario9.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario10.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario11.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario12.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit12.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario13.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit13.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario14.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit14.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario15.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit15.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario16.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit16.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario17.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit17.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario18.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit18.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario19.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit19.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(),Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario20.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit20.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario21.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit21.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario22.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit22.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario23.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit23.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario24.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit24.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario25.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit25.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario26.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit26.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario27.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit27.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario28.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit28.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario29.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit29.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario30.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit30.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(),Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario31.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });
            cit31.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplication(), Main2Activity.class);
                    i.putExtra("ESPECIALIDAD", esp.getText().toString());
                    i.putExtra("FECHA", efecha.getText().toString());
                    i.putExtra("HORARIO", horario32.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());
                    i.putExtra("DOCTOR",  doc.getText().toString());


                    startActivity(i);
                }
            });



        } catch (Exception e) {
            Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }







    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            Intent intent = new Intent(getApplication(), agendarcita.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}

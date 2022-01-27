package proyectod.permisosunahtec;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

import static proyectod.permisosunahtec.PermisoFragment.fechaInicioAlert;

public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);


        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm+1, dd);
    }
    public void populateSetDate(int year, int month, int day) {
        String monthPiker = null;
        String dayPiker = null;
        if (month <10){
            monthPiker = "0"+month;
        }else {
            monthPiker = String.valueOf(month);
        }
        if (day <10){
            dayPiker = "0"+day;
        }else{
            dayPiker = String.valueOf(day);
        }

        fechaInicioAlert.setText(year+"-"+monthPiker+"-"+dayPiker);
    }

}
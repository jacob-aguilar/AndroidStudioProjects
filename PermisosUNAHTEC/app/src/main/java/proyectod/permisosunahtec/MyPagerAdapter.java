package proyectod.permisosunahtec;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import static proyectod.permisosunahtec.PermisoFragment.c;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private   int NUM_OF_SLIDER =3;


    public MyPagerAdapter(FragmentManager fm){
        super(fm);
    }




    @Override    public Fragment getItem(int position ) {


        switch (position){
            case 0: return new DeptoFragment();
            case 1: return new EmpleadosFragment();
            case 2: return new PermisoFragment();



            default: return  null;




        }

    }
    @Override
    public int getCount() {

        return NUM_OF_SLIDER;

    }

}


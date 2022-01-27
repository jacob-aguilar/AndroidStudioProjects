package com.example.probook.planestudioinformatica;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AdaptadorClase extends ArrayAdapter<JSONObject>{

    private JSONArray data;
    public AdaptadorClase(Context context, JSONArray data) {
        super(context, R.layout.item_clase);
        this.data = data;
    }

    @Nullable
    @Override
    public JSONObject getItem(int position) {
        try {
            return data.getJSONObject(position);
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    public int getCount() {
        return data.length();
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_clase, null);

        TextView texto = convertView.findViewById(R.id.asignatura);

        JSONObject json = getItem(position);
        try {
            texto.setText(json.getString("codigo")+" "+
                    json.getString("asignatura"));
        }catch (Exception ex){ }

        return convertView;
    }
}

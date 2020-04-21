package com.example.revizija.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.revizija.Classes.JSONKlijent;
import com.example.revizija.R;

import java.util.ArrayList;

public class KlijentiAdapter extends ArrayAdapter<JSONKlijent>
{
    private Context mContext;
    private int mResource;
    public KlijentiAdapter(Context context, int resource, ArrayList<JSONKlijent> objects)
    {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        String naziv = getItem(position).getNaziv();
        int id_klijent = getItem(position).getId_klijent();
        String pib = getItem(position).getPIB();
        String pdv = getItem(position).getPdv();
        String direktor_ime = getItem(position).getDirektor_ime();

        JSONKlijent klijent = new JSONKlijent(id_klijent, naziv, pib, pdv, direktor_ime);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView tv_naziv = (TextView) convertView.findViewById(R.id.textViewNaziv);
        TextView tv_pib = (TextView) convertView.findViewById(R.id.textViewPIB);
        TextView tv_pdv = (TextView) convertView.findViewById(R.id.textViewpdv);

        tv_naziv.setText("Naziv: "+naziv);
        tv_pib.setText("PIB: "+pib);
        tv_pdv.setText("PDV: "+pdv);

        return convertView;
    }
}

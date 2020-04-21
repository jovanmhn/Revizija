package com.example.revizija.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.revizija.Classes.JSONKlijent;
import com.example.revizija.Classes.JSONKnjiga;
import com.example.revizija.R;

import java.util.ArrayList;

public class KnjigeAdapter extends ArrayAdapter<JSONKnjiga>
{
    private Context mContext;
    private int mResource;
    public KnjigeAdapter(Context context, int resource, ArrayList<JSONKnjiga> objects)
    {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        int id = getItem(position).getId();
        int godina = getItem(position).getGodina();
        String opis = getItem(position).getOpis();
        String klijent_naziv = getItem(position).getKlijent_naziv();

        JSONKnjiga knjiga = new JSONKnjiga(id, godina, opis, klijent_naziv);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView tv_godina = (TextView) convertView.findViewById(R.id.textViewGodina);
        TextView tv_klijent = (TextView) convertView.findViewById(R.id.textViewKlijent);
        TextView tv_opis = (TextView) convertView.findViewById(R.id.textViewOpis);

        tv_godina.setText("Godina: "+ String.valueOf(godina));
        tv_klijent.setText(klijent_naziv);
        tv_opis.setText("Opis: "+ opis);

        return convertView;
    }
}

package com.example.revizija.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.revizija.Adapters.KnjigeAdapter;
import com.example.revizija.Classes.API;
import com.example.revizija.Classes.JSONKnjiga;
import com.example.revizija.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class KnjigeActivity extends AppCompatActivity
{
    private TextView tv;
    private ListView lv;
    private ArrayList<JSONKnjiga> lista;
    KnjigeAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klijenti);
        tv = findViewById(R.id.textviewNaslov);
        tv.setText("Lista knjiga");

        AsyncLoad thread = new AsyncLoad();
        thread.start();
    }

    public class AsyncLoad extends Thread{
        @Override
        public void run(){
            Handler handler = new Handler(Looper.getMainLooper());
            Bundle extras = getIntent().getExtras();
            int id_klijent = 0;
            if(extras!=null)
            {
                id_klijent = extras.getInt("id_klijent");
            }
            final API.APIKnjigeResult result =  API.GetKnjige(id_klijent);

            if(result.requestState == API.LoginResult.Success){
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        lista = result.lista_knjiga;
                        lv = findViewById(R.id.listviewmain);
                        Collections.sort(lista, new KnjigaGodinaDesc());
                        adapter = new KnjigeAdapter(getApplicationContext(),R.layout.adapter_view_knjige, lista );

                        lv.setAdapter(adapter);
                    }
                });

            }
            else{
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(getApplicationContext(),"Doslo je do greske kod ucitavanja knjiga", Toast.LENGTH_LONG).show();
                    }
                });

            }

        }
    }
    public class KnjigaGodinaDesc implements Comparator<JSONKnjiga>{
        public int compare(JSONKnjiga left, JSONKnjiga right){
            return right.getGodina() - left.getGodina(); // ovo je descending
        }
    }
}

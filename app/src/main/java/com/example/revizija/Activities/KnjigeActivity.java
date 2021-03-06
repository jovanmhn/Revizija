package com.example.revizija.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.revizija.Adapters.KnjigeAdapter;
import com.example.revizija.Classes.API;
import com.example.revizija.Classes.JSONKlijent;
import com.example.revizija.Classes.JSONKnjiga;
import com.example.revizija.Classes.JSONOperater;
import com.example.revizija.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class KnjigeActivity extends AppCompatActivity
{
    private TextView tv;
    private ListView lv;
    private ArrayList<JSONKnjiga> lista;
    private JSONOperater logovani_operater;
    KnjigeAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knjige);
        tv = findViewById(R.id.textviewNaslov);
        tv.setText("Lista knjiga");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        logovani_operater = (JSONOperater) bundle.getSerializable("OPERATER");

        lv = findViewById(R.id.listviewmainKnjige);
        lv.setClickable(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                JSONKnjiga o = (JSONKnjiga) lv.getItemAtPosition(i);

                Bundle bundle = new Bundle();
                bundle.putSerializable("OPERATER", (Serializable) logovani_operater);
                bundle.putInt("id_knjiga", o.id);
                Intent intent = new Intent(getApplicationContext(),UploadActivity.class);
                //intent.putExtra("id_knjiga", o.id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

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
            final API.APIKnjigeResult result =  API.GetKnjige(getApplicationContext(), id_klijent);

            if(result.requestState == API.LoginResult.Success){
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        lista = result.lista_knjiga;
                        lv = findViewById(R.id.listviewmainKnjige);
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

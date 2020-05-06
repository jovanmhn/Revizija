package com.example.revizija.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.revizija.Adapters.KlijentiAdapter;
import com.example.revizija.Classes.API;
import com.example.revizija.Classes.JSONKlijent;
import com.example.revizija.Classes.JSONOperater;
import com.example.revizija.R;

import java.io.Serializable;
import java.util.ArrayList;

public class KlijentiActivity extends AppCompatActivity
{
    ArrayList<JSONKlijent> lista = new ArrayList<JSONKlijent>();
    KlijentiAdapter adapter;
    private ListView lv;
    JSONOperater logovani_operater;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klijenti);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        logovani_operater = (JSONOperater) bundle.getSerializable("OPERATER");

        lv = findViewById(R.id.listviewmain);
        lv.setClickable(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                JSONKlijent o = (JSONKlijent) lv.getItemAtPosition(i);

                Bundle bundle = new Bundle();
                bundle.putSerializable("OPERATER", (Serializable) logovani_operater);
                bundle.putInt("id_klijent", o.id_klijent);
                Intent intent = new Intent(getApplicationContext(),KnjigeActivity.class);
                //intent.putExtra("id_klijent", o.id_klijent);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        AsyncSRANJEJAVA thread = new AsyncSRANJEJAVA();
        thread.start();



    }
    public class AsyncSRANJEJAVA extends Thread{
        @Override
        public void run(){
            Handler handler = new Handler(Looper.getMainLooper());


            final API.APIKlijentiResult result =  API.GetKlijenti(getApplicationContext());

           if(result.requestState == API.LoginResult.Success){
               handler.post(new Runnable()
               {
                   @Override
                   public void run()
                   {
                       lista = result.lista_klijenata;
                       lv = findViewById(R.id.listviewmain);
                       adapter = new KlijentiAdapter(getApplicationContext(),R.layout.adapter_view_klijenti,lista);

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
                       Toast.makeText(getApplicationContext(),"Doslo je do greske kod ucitavanja klijenata", Toast.LENGTH_LONG).show();
                   }
               });

           }

        }
    }

}

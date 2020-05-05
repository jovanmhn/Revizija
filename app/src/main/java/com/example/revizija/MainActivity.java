package com.example.revizija;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.revizija.Activities.KlijentiActivity;
import com.example.revizija.Activities.UploadActivity;
import com.example.revizija.Classes.API;
import com.example.revizija.Classes.JSONOperater;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;


public class MainActivity extends AppCompatActivity
{

    Button btn_login;
    Button btnSettings;
    EditText editText_username;
    EditText editText_password;
    CheckBox checkBox;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        btn_login = findViewById(R.id.button_login);
        editText_username = findViewById(R.id.editTextUsername);
        editText_password = findViewById(R.id.editTextPassword);
        checkBox = findViewById(R.id.checkBoxSaveLogin);
        btnSettings = findViewById(R.id.btnSettings);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        boolean use = sharedPref.getBoolean(getString(R.string.save_login), false);
        if(use)
        {
            editText_username.setText(sharedPref.getString(getString(R.string.login_username),""));
            editText_password.setText(sharedPref.getString(getString(R.string.login_password),""));
            checkBox.setChecked(use);
        }
        btnSettings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SharedPreferences sharedPref_api = getApplicationContext().getSharedPreferences("API", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedPref_api.edit();

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
                builder.setTitle("API URL:");
                final EditText input = new EditText(MainActivity.this);
                input.setText(sharedPref_api.getString("API",""));
                input.setInputType(InputType.TYPE_CLASS_TEXT /*| InputType.TYPE_TEXT_VARIATION_PASSWORD*/);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putString("API",input.getText().toString());
                        editor.commit();
                    }
                });
                builder.setNeutralButton("Odustani", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {

                    }
                });
                builder.show();
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(checkBox.isChecked())
                {
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.login_username), editText_username.getText().toString());
                    editor.putString(getString(R.string.login_password), editText_password.getText().toString());
                    editor.putBoolean(getString(R.string.save_login), true);
                    editor.commit();
                }
                else
                {
                    getApplicationContext().getSharedPreferences("login",Context.MODE_PRIVATE).edit().clear().commit();
                }
                LoginAsync thread = new LoginAsync();
                thread.start();
            }
        });
    }

    public class LoginAsync extends Thread
    {
        @Override
        public void run()
        {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    ProgressBar pg = findViewById(R.id.progressBar);
                    pg.setVisibility(View.VISIBLE);
                    btn_login.setEnabled(false);
                }
            });
            final API.APILoginResult login_result = API.login(getApplicationContext(), editText_username.getText().toString(), editText_password.getText().toString() );

            if(login_result.loginState == API.LoginResult.Success)
            {
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ProgressBar pg = findViewById(R.id.progressBar);
                        pg.setVisibility(View.GONE);
                        btn_login.setEnabled(true);
                        Toast.makeText(getApplicationContext(),"Prijavljen kao: "+ login_result.logovani_operater.ime + " " + login_result.logovani_operater.prezime, Toast.LENGTH_LONG).show();



                    }
                });
                Bundle bundle = new Bundle();
                //bundle.putSerializable("OPERATER", (Serializable) login_result.logovani_operater);
                Intent intent = new Intent(getApplicationContext(), KlijentiActivity.class);
                //intent.putExtras(bundle);
                startActivity(intent);

            }
            else if (login_result.loginState == API.LoginResult.Failed)
            {
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ProgressBar pg = findViewById(R.id.progressBar);
                        pg.setVisibility(View.GONE);
                        btn_login.setEnabled(true);
                        Toast.makeText(getApplicationContext(),"Pogrešno korisničko ime / lozinka", Toast.LENGTH_LONG).show();

                    }
                });

            }
            else
            {
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ProgressBar pg = findViewById(R.id.progressBar);
                        pg.setVisibility(View.GONE);
                        btn_login.setEnabled(true);
                        Toast.makeText(getApplicationContext(),"500 - Internal server error", Toast.LENGTH_LONG).show();
                    }
                });
            }



        }
    }

}

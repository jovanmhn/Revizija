package com.example.revizija.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.revizija.Adapters.KlijentiAdapter;
import com.example.revizija.Classes.API;
import com.example.revizija.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class UploadActivity extends AppCompatActivity
{
    ImageView iv;
    Button btnLoad;
    Button btnUpload;
    String image;
    String naziv_fajla;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        iv = findViewById(R.id.imageViewUpload);
        btnLoad = findViewById(R.id.buttonLoad);
        btnUpload = findViewById(R.id.buttonUpload);

        btnLoad.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Intent intent = new Intent(Intent.ACTION_PICK,
                //        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(intent, 0);
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                BitmapDrawable bitmapDrawable = ((BitmapDrawable) iv.getDrawable());
                Bitmap bitmap = bitmapDrawable .getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] imageInByte = stream.toByteArray();
                image = Base64.encodeToString(imageInByte,Base64.DEFAULT);

                //Alert dialog nebuloza
                AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
                builder.setTitle("Naziv");
                final EditText input = new EditText(UploadActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT /*| InputType.TYPE_TEXT_VARIATION_PASSWORD*/);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        naziv_fajla = input.getText().toString();
                        UploadAsync thread = new UploadAsync();
                        thread.setNaziv(naziv_fajla);
                        thread.start();
                    }
                });
                builder.show();
                //


            }
        });
    }
    public class UploadAsync extends Thread
    {
        public String Naziv;

        public void setNaziv(String naziv)
        {
            Naziv = naziv;
        }

        @Override
        public void run()
        {
            Handler handler = new Handler(Looper.getMainLooper());
            API.LoginResult result = API.UploadImage(52,image, Naziv);
            if(result == API.LoginResult.Failed)
            {
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(getApplicationContext(),"Doslo je do greske prilikom uploada", Toast.LENGTH_LONG).show();
                    }
                });

            }
            else if(result == API.LoginResult.Success)
            {
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(getApplicationContext(),"upload uspjesan", Toast.LENGTH_LONG).show();
                    }
                });

            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try
            {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                iv.setImageBitmap(bitmap);
            } catch (FileNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

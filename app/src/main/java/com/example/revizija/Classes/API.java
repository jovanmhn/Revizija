package com.example.revizija.Classes;

import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class API
{
    public static APILoginResult login(String username, String password)
    {
        HttpURLConnection urlConnection = null;


        android.net.Uri.Builder builder = new android.net.Uri.Builder();
        builder.scheme("http")
                .encodedAuthority("192.168.1.131:1991")
                .appendPath("api")
                .appendPath("operater")
                .appendPath("login")
                .appendPath(username)
                .appendPath(password);
                //.appendQueryParameter("klijent", editText.getText().toString());


        URL url = null;
        try
        {
            url = new URL(builder.build().toString());
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
            return new APILoginResult(LoginResult.ServerError, null);
        }

        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e)
        {
            e.printStackTrace();
            return new APILoginResult(LoginResult.ServerError, null);
        }

        try
        {
            urlConnection.setRequestMethod("GET");
        } catch (ProtocolException e)
        {
            e.printStackTrace();
            return new APILoginResult(LoginResult.ServerError, null);
        }
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        //urlConnection.setDoOutput(true);
        try
        {
            urlConnection.connect();
        } catch (IOException e)
        {
            e.printStackTrace();
            return new APILoginResult(LoginResult.ServerError, null);
        }

        JSONOperater operater = new JSONOperater();
        try
        {
            int response = urlConnection.getResponseCode();
            if (urlConnection.getResponseCode() == 200)
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

                char[] buffer = new char[1024];

                String jsonString = new String();

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                br.close();

                jsonString = sb.toString();

                System.out.println("JSON: " + jsonString);
                JSONObject var = new JSONObject(jsonString);


                operater.id_operater = var.getInt("id_operater");
                operater.username = var.getString("username");
                operater.ime = var.getString("ime");
                operater.prezime = var.getString("prezime");

                return new APILoginResult(LoginResult.Success, operater);
            }
            else if( urlConnection.getResponseCode() == 400 )
            {
                return new APILoginResult(LoginResult.Failed, null);
            }
            else
            {
                return new APILoginResult(LoginResult.ServerError, null);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return new APILoginResult(LoginResult.ServerError, null);
        } catch (JSONException e)
        {
            e.printStackTrace();
            return new APILoginResult(LoginResult.ServerError, null);
        }


    }
    public static class APILoginResult
    {
        public LoginResult loginState;
        public JSONOperater logovani_operater;

        public APILoginResult(LoginResult loginState, JSONOperater logovani_operater)
        {
            this.loginState = loginState;
            this.logovani_operater = logovani_operater;
        }
    }
    public static class APIKlijentiResult
    {
        public LoginResult requestState;
        public ArrayList<JSONKlijent> lista_klijenata;

        public APIKlijentiResult(LoginResult loginState, ArrayList<JSONKlijent> lista)
        {
            this.requestState = loginState;
            this.lista_klijenata = lista;
        }
    }
    public enum LoginResult
    {
        Success,
        Failed,
        ServerError,
    }
    public static APIKlijentiResult GetKlijenti()
    {
        HttpURLConnection urlConnection = null;
        ArrayList<JSONKlijent> lista_klijenata = new ArrayList<JSONKlijent>();

        android.net.Uri.Builder builder = new android.net.Uri.Builder();
        builder.scheme("http")
                .encodedAuthority("192.168.1.131:1991")
                .appendPath("api")
                .appendPath("klijents");
                //.appendPath("login")
        //.appendQueryParameter("klijent", editText.getText().toString());


        URL url = null;
        try
        {
            url = new URL(builder.build().toString());
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
            return new APIKlijentiResult(LoginResult.ServerError, lista_klijenata);
        }

        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e)
        {
            e.printStackTrace();
            return new APIKlijentiResult(LoginResult.ServerError, lista_klijenata);
        }

        try
        {
            urlConnection.setRequestMethod("GET");
        } catch (ProtocolException e)
        {
            e.printStackTrace();
            return new APIKlijentiResult(LoginResult.ServerError, lista_klijenata);
        }
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        //urlConnection.setDoOutput(true);
        try
        {
            urlConnection.connect();
        } catch (IOException e)
        {
            e.printStackTrace();
            return new APIKlijentiResult(LoginResult.ServerError, lista_klijenata);
        }

        JSONOperater operater = new JSONOperater();
        try
        {
            int response = urlConnection.getResponseCode();
            if (urlConnection.getResponseCode() == 200)
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

                char[] buffer = new char[1024];

                String jsonString = new String();

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                br.close();

                jsonString = sb.toString();
                System.out.println("JSON: " + jsonString);
                JSONArray array = new JSONArray(jsonString);
                lista_klijenata = Konverzije.JSONArray2KlijentArray(array);
                return new APIKlijentiResult(LoginResult.Success, lista_klijenata);
            }
            else if( urlConnection.getResponseCode() == 400 )
            {
                return new APIKlijentiResult(LoginResult.Failed, lista_klijenata);
            }
            else
            {
                return new APIKlijentiResult(LoginResult.ServerError, lista_klijenata);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return new APIKlijentiResult(LoginResult.ServerError, lista_klijenata);
        } catch (JSONException e)
        {
            e.printStackTrace();
            return new APIKlijentiResult(LoginResult.ServerError, lista_klijenata);
        }
    }
}

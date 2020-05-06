package com.example.revizija.Classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class API
{

    public static APILoginResult login( Context context, String username, String password)
    {
        HttpURLConnection urlConnection = null;

        SharedPreferences sharedPref_api = context.getSharedPreferences("API", Context.MODE_PRIVATE);

        android.net.Uri.Builder builder = new android.net.Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(sharedPref_api.getString("API",""))
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
    public static APIKlijentiResult GetKlijenti(Context context)
    {
        HttpURLConnection urlConnection = null;
        ArrayList<JSONKlijent> lista_klijenata = new ArrayList<JSONKlijent>();
        SharedPreferences sharedPref_api = context.getSharedPreferences("API", Context.MODE_PRIVATE);

        android.net.Uri.Builder builder = new android.net.Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(sharedPref_api.getString("API",""))
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
    public static class APIKnjigeResult
    {
        public LoginResult requestState;
        public ArrayList<JSONKnjiga> lista_knjiga;

        public APIKnjigeResult(LoginResult loginState, ArrayList<JSONKnjiga> lista)
        {
            this.requestState = loginState;
            this.lista_knjiga = lista;
        }
    }
    public static APIKnjigeResult GetKnjige(Context context, int id_klijent)
    {
        HttpURLConnection urlConnection = null;
        ArrayList<JSONKnjiga> lista_knjiga = new ArrayList<JSONKnjiga>();
        SharedPreferences sharedPref_api = context.getSharedPreferences("API", Context.MODE_PRIVATE);

        android.net.Uri.Builder builder = new android.net.Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(sharedPref_api.getString("API",""))
                .appendPath("api")
                .appendPath("knjigas")
                .appendPath( String.valueOf(id_klijent) );
        //.appendQueryParameter("klijent", editText.getText().toString());


        URL url = null;
        try
        {
            url = new URL(builder.build().toString());
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
            return new APIKnjigeResult(LoginResult.ServerError, lista_knjiga);
        }

        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e)
        {
            e.printStackTrace();
            return new APIKnjigeResult(LoginResult.ServerError, lista_knjiga);
        }

        try
        {
            urlConnection.setRequestMethod("GET");
        } catch (ProtocolException e)
        {
            e.printStackTrace();
            return new APIKnjigeResult(LoginResult.ServerError, lista_knjiga);
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
            return new APIKnjigeResult(LoginResult.ServerError, lista_knjiga);
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
                lista_knjiga = Konverzije.JSONArray2KnjigaArray(array);
                return new APIKnjigeResult(LoginResult.Success, lista_knjiga);
            }
            else if( urlConnection.getResponseCode() == 400 )
            {
                return new APIKnjigeResult(LoginResult.Failed, lista_knjiga);
            }
            else
            {
                return new APIKnjigeResult(LoginResult.ServerError, lista_knjiga);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return new APIKnjigeResult(LoginResult.ServerError, lista_knjiga);
        } catch (JSONException e)
        {
            e.printStackTrace();
            return new APIKnjigeResult(LoginResult.ServerError, lista_knjiga);
        }
    }
    public static LoginResult UploadImage(Context context,int id_knjiga, byte[] imageInBytes, String naziv, int id_operater)
    {
        HttpURLConnection urlConnection = null;
        ArrayList<JSONKnjiga> lista_knjiga = new ArrayList<JSONKnjiga>();
        SharedPreferences sharedPref_api = context.getSharedPreferences("API", Context.MODE_PRIVATE);

        android.net.Uri.Builder builder = new android.net.Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(sharedPref_api.getString("API",""))
                //.encodedAuthority("localhost:44300")
                .appendPath("api")
                .appendPath("knjigas")
                .appendPath("upload")
                .appendPath( String.valueOf(id_knjiga) )
                .appendPath(naziv)
                .appendPath(String.valueOf(id_operater));
        //.appendQueryParameter("klijent", editText.getText().toString());


        URL url = null;
        try
        {
            url = new URL(builder.build().toString());
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
            return LoginResult.ServerError;
        }

        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e)
        {
            e.printStackTrace();
            return LoginResult.ServerError;
        }

        try
        {
            urlConnection.setRequestMethod("PUT");
        } catch (ProtocolException e)
        {
            e.printStackTrace();
            return LoginResult.ServerError;
        }
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        //urlConnection.setDoOutput(true);
        try
        {
            urlConnection.connect();
            OutputStream os =  urlConnection.getOutputStream();
            //os.write(image64.getBytes());
            os.write(imageInBytes);
            os.close();

        } catch (IOException e)
        {
            //urlConnection.disconnect();
            e.printStackTrace();
            return LoginResult.ServerError;
        }


        try
        {
            if (urlConnection.getResponseCode() == 200)
            {
                //urlConnection.disconnect();
                return LoginResult.Success;
            }

            if( urlConnection.getResponseCode() == 400 )
            {
                //urlConnection.disconnect();
                return LoginResult.Failed;
            }
            else
            {
                //urlConnection.disconnect();
                return LoginResult.ServerError;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return LoginResult.ServerError;
        }
    }
}

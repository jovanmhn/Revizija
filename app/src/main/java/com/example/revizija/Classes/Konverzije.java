package com.example.revizija.Classes;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Konverzije
{
    public static ArrayList<JSONKlijent> JSONArray2KlijentArray(JSONArray json ){
        ArrayList<JSONKlijent> returnLista = new ArrayList<JSONKlijent>();
        if(json!= null){
            for(int i= 0; i<json.length();i++){
                JSONKlijent k = new JSONKlijent();
                try
                {
                    k.id_klijent = json.getJSONObject(i).getInt("id_klijent");
                    k.naziv = json.getJSONObject(i).getString("naziv");
                    k.PIB = json.getJSONObject(i).getString("PIB");
                    k.pdv = json.getJSONObject(i).getString("pdv");
                    k.direktor_ime = json.getJSONObject(i).getString("direktor_ime");
                    returnLista.add(k);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    return new ArrayList<JSONKlijent>();
                }

            }
            return returnLista;
        }
        else{
            return new ArrayList<JSONKlijent>();
        }
    }
}

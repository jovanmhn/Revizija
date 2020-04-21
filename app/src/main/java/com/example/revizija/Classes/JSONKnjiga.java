package com.example.revizija.Classes;

public class JSONKnjiga
{
    public int id;
    public int godina;
    public String opis;
    public String klijent_naziv;

    public JSONKnjiga(int id, int godina, String opis, String klijent_naziv)
    {
        this.id = id;
        this.godina = godina;
        this.opis = opis;
        this.klijent_naziv = klijent_naziv;
    }
}

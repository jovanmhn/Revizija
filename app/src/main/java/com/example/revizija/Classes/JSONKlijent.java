package com.example.revizija.Classes;

public class JSONKlijent
{
    public int id_klijent;
    public String naziv;
    public String PIB;
    public String pdv;
    public String direktor_ime;

    public JSONKlijent(int id_klijent, String naziv, String PIB, String pdv, String direktor_ime)
    {
        this.id_klijent = id_klijent;
        this.naziv = naziv;
        this.PIB = PIB;
        this.pdv = pdv;
        this.direktor_ime = direktor_ime;
    }

    public JSONKlijent()
    {
    }

    public int getId_klijent()
    {
        return id_klijent;
    }

    public String getNaziv()
    {
        return naziv;
    }

    public String getPIB()
    {
        return PIB;
    }

    public String getPdv()
    {
        return pdv;
    }

    public String getDirektor_ime()
    {
        return direktor_ime;
    }
}

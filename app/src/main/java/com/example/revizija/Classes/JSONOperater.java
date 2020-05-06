package com.example.revizija.Classes;

import java.io.Serializable;

public class JSONOperater implements Serializable
{
    public int id_operater ;
    public String username ;
    public String ime ;
    public String prezime ;

    public JSONOperater(int id_operater, String username, String ime, String prezime)
    {
        this.id_operater = id_operater;
        this.username = username;
        this.ime = ime;
        this.prezime = prezime;
    }
    public JSONOperater()
    {
        this.id_operater = 0;
        this.username = "";
        this.ime = "";
        this.prezime = "";
    }
}

package com.example.gogo.orar;

/**
 * Created by GoGo on 11/15/2017.
 */

public class Orar {

    public String getZiua() {
        return ziua;
    }

    public void setZiua(String ziua) {
        this.ziua = ziua;
    }

    public String getMaterie() {
        return materie;
    }

    public void setMaterie(String materie) {
        this.materie = materie;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getOraInceput() {
        return oraInceput;
    }

    public void setOraInceput(String oraInceput) {
        this.oraInceput = oraInceput;
    }

    public String getOraSfarsit() {
        return oraSfarsit;
    }

    public void setOraSfarsit(String oraSfarsit) {
        this.oraSfarsit = oraSfarsit;
    }

    public Boolean getPara() {
        return para;
    }

    public void setPara(Boolean para) {
        this.para = para;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;
    String ziua;
    String materie;
    String profesor;
    String sala;
    String tip;
    String oraInceput;
    String oraSfarsit;
    Boolean para;

    public  Orar(){}

    public Orar(int id,String zziua, String mmaterie, String mprofesor, String msala, String mtip, String so,String sf,Boolean mpara){
        this.id = id;
        this.ziua = zziua;
        this.materie = mmaterie;
        this.profesor = mprofesor;
        this.sala = msala;
        this.tip = mtip;
        this.oraInceput = so;
        this.oraSfarsit = sf;
        this.para = mpara;
    }

    public  Orar(String zziua, String mmaterie, String mprofesor, String msala, String mtip, String so,String sf,Boolean mpara){
        this.ziua = zziua;
        this.materie = mmaterie;
        this.profesor = mprofesor;
        this.sala = msala;
        this.tip = mtip;
        this.oraInceput = so;
        this.oraSfarsit = sf;
        this.para = mpara;
    }
}

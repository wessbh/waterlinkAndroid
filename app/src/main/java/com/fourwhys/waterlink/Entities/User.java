package com.fourwhys.waterlink.Entities;

public class User {
    private int num_contrat;
    private String nom;
    private String prenom;
    private String region;
    private String mail;
    private String password;
    private String api_key;
    private int num_tel;
    private int code_postale;

    public User() {
    }

    public User(int num_contrat, String nom, String prenom, String region, String mail, String api_key, int num_tel, int code_postale) {
        this.num_contrat = num_contrat;
        this.nom = nom;
        this.prenom = prenom;
        this.region = region;
        this.mail = mail;
        this.api_key = api_key;
        this.num_tel = num_tel;
        this.code_postale = code_postale;
    }

    public int getNum_contrat() {
        return num_contrat;
    }

    public void setNum_contrat(int num_contrat) {
        this.num_contrat = num_contrat;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public int getNum_tel() {
        return num_tel;
    }

    public void setNum_tel(int num_tel) {
        this.num_tel = num_tel;
    }

    public int getCode_postale() {
        return code_postale;
    }

    public void setCode_postale(int code_postale) {
        this.code_postale = code_postale;
    }

    @Override
    public String toString() {
        return "User{" +
                "num_contrat=" + num_contrat +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", region='" + region + '\'' +
                ", mail='" + mail + '\'' +
                ", num_tel=" + num_tel +
                ", code_postale=" + code_postale +
                ", api_key='" + api_key + '\'' +
                '}';
    }
}

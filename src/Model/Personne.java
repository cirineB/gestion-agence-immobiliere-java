/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.Serializable;

/**
 *
 * @author serin
 */
public class Personne implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int idPersonne;
    protected String nom;
    protected String prenom;
    protected String numeroTel;
    protected String email;

    public Personne() {}

    public Personne(int idPersonne, String nom, String prenom, String numeroTel, String email) {
        this.idPersonne = idPersonne;
        this.nom = nom;
        this.prenom = prenom;
        this.numeroTel = numeroTel;
        this.email = email;
    }

    // Getters et setters...
    public int getIdPersonne() { return idPersonne; }
    public void setIdPersonne(int idPersonne) { this.idPersonne = idPersonne; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getNumeroTel() { return numeroTel; }
    public void setNumeroTel(String numeroTel) { this.numeroTel = numeroTel; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
}

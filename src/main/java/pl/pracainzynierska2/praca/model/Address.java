package pl.pracainzynierska2.praca.model;

import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Component
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String miasto;
    private String ulica;
    private int numer_budynku;
    private int numer_lokalu;

    public Address() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMiasto() {
        return miasto;
    }

    public void setMiasto(String miasto) {
        this.miasto = miasto;
    }

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

    public int getNumer_budynku() {
        return numer_budynku;
    }

    public void setNumer_budynku(int numer_budynku) {
        this.numer_budynku = numer_budynku;
    }

    public int getNumer_lokalu() {
        return numer_lokalu;
    }

    public void setNumer_lokalu(int numer_lokalu) {
        this.numer_lokalu = numer_lokalu;
    }


    public Address(String miasto, String ulica, int numer_budynku, int numer_lokalu) {
        this.miasto = miasto;
        this.ulica = ulica;
        this.numer_budynku = numer_budynku;
        this.numer_lokalu = numer_lokalu;
    }



    @Override
    public String toString() {
        return miasto + "ul. " + ulica +" " + numer_budynku + "mieszkanie " + numer_lokalu;
    }
}

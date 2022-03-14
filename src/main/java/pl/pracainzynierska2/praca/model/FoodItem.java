package pl.pracainzynierska2.praca.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@Component
@Entity
public class FoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nazwa;
    private double cena;
    private int ilosc=0;
    private String opis;
    private boolean dostepne;

/*
    public FoodOrder getFoodOrder() {
        return foodOrder;
    }

    public void setFoodOrder(FoodOrder foodOrder) {
        this.foodOrder = foodOrder;
    }

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)

*/
    public FoodItem(long id, String nazwa, double cena, int ilosc, String opis, boolean dostepne, String url) {
        this.id = id;
        this.nazwa = nazwa;
        this.cena = cena;
        this.ilosc = ilosc;
        this.opis = opis;
        this.dostepne = dostepne;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

/*
    private List<FoodItem> foodItems;

    public List <FoodItem> getFoodItems()
    {
        return foodItems;
    }
*/

    public FoodItem(String nazwa, double cena, String opis, boolean dostepne) {
        this.nazwa = nazwa;
        this.cena = cena;
        this.opis = opis;
        this.dostepne = dostepne;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    public int getIlosc() {
        return ilosc;
    }

    public void setIlosc(int ilosc) {
        this.ilosc = ilosc;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public boolean isDostepne() {
        return dostepne;
    }

    public void setDostepne(boolean dostepne) {
        this.dostepne = dostepne;
    }

    public boolean getDostepne() {
        return dostepne;
    }

    public FoodItem() {
    }
}

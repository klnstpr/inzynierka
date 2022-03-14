package pl.pracainzynierska2.praca.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Component
@Entity
public class FoodOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public FoodOrder(long id, double cena_koncowa, String czas_zamowienia, String czas_dostawy, String dodatkowe_informacje) {
        this.id = id;
        this.cena_koncowa = cena_koncowa;
        this.czas_zamowienia = czas_zamowienia;
        this.czas_dostawy = czas_dostawy;
        this.dodatkowe_informacje = dodatkowe_informacje;

    }
//
    @ManyToOne
    @JoinColumn(name="user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AppUser appUser;

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }


    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    @ManyToOne
    @JoinColumn(name="fooditem_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private FoodItem foodItem;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @ManyToOne
    @JoinColumn(name="status_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Status status;

    //
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getCena_koncowa() {
        return cena_koncowa;
    }

    public void setCena_koncowa(double cena_koncowa) {
        this.cena_koncowa = cena_koncowa;
    }

    public String getCzas_zamowienia() {
        return czas_zamowienia;
    }

    public void setCzas_zamowienia(String czas_zamowienia) {
        this.czas_zamowienia = czas_zamowienia;
    }

    public String getCzas_dostawy() {
        return czas_dostawy;
    }

    public void setCzas_dostawy(String czas_dostawy) {
        this.czas_dostawy = czas_dostawy;
    }

    public String getDodatkowe_informacje() {
        return dodatkowe_informacje;
    }

    public void setDodatkowe_informacje(String dodatkowe_informacje) {
        this.dodatkowe_informacje = dodatkowe_informacje;
    }

   /* public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
*/
    private double cena_koncowa;
    private String czas_zamowienia;
    private String czas_dostawy;
    private String dodatkowe_informacje;

    public boolean isArchiwum() {
        return archiwum;
    }

    public void setArchiwum(boolean archiwum) {
        this.archiwum = archiwum;
    }

    private boolean archiwum=false;

    public boolean isKoszyk() {
        return koszyk;
    }

    public void setKoszyk(boolean koszyk) {
        this.koszyk = koszyk;
    }

    private boolean koszyk=true;

    //private String status;

    public int getIlosc() {
        return ilosc;
    }

    public void setIlosc(int ilosc) {
        this.ilosc = ilosc;
    }

    private int ilosc;

    @Transient
    public double getSubtotal() {
        return foodItem.getCena() * ilosc;
    }

    @Override
    public String toString() {
        return "Zamówienie [Id=" + id + ", ilosc=" + ilosc + ", foodItem=" + foodItem + ", user=" + appUser + ", status=" + status + ", cena_koncowa=" + cena_koncowa + ", informacje=" + dodatkowe_informacje + ", koszyk=" +koszyk +"]";
    }

    public FoodOrder (){}
}

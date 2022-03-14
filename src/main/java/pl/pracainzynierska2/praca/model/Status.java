package pl.pracainzynierska2.praca.model;

import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;

//statusy zamówienia
// 1 = zapłacone, do doręczenia
// 2 = płatność gotówką
// 3 = w doręczeniu
// 4 = doręczono
// 5 = zamówienie anulowane

@Component
@Entity
public class Status {
    public Status() {
    }

    public Status(int id, String status) {
        this.id = id;
        this.status = status;
    }
    @Id
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;

    @Override
    public String toString() {
        return "ID = " +id + " , status: " + status;
    }
}

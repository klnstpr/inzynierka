package pl.pracainzynierska2.praca.repo;

import com.sun.xml.bind.v2.model.core.ID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.pracainzynierska2.praca.model.Address;
import pl.pracainzynierska2.praca.model.AppUser;
import pl.pracainzynierska2.praca.model.FoodItem;

import java.util.Optional;
@Transactional
@Repository
public interface AppUserRepo extends JpaRepository <AppUser, Long> {

    AppUser findByUsername(String username);
    AppUser findById(long id);
    AppUser save(AppUser appUser);
    AppUser deleteById(ID id);
    public Long countById(long id);
    AppUser findByEmail(String email);
    AppUser findByTelefon(String telefon);

    @Modifying
    @Query("UPDATE AppUser u SET u.address = ?1 WHERE u.id = ?2")
    void updateAdres(Address address, long id);

}

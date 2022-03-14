package pl.pracainzynierska2.praca.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.pracainzynierska2.praca.model.AppUser;
import pl.pracainzynierska2.praca.model.FoodItem;
import pl.pracainzynierska2.praca.model.FoodOrder;
import pl.pracainzynierska2.praca.model.Status;

import java.util.List;

@Transactional
@Repository
public interface FoodOrderRepo extends JpaRepository<FoodOrder, Long> {
    @Override
    void deleteById(Long aLong);

     List<FoodOrder> findByAppUser (AppUser appUser);
     List<FoodOrder> findByAppUserAndArchiwum (AppUser appUser, boolean archiwum);

     FoodOrder findByAppUserAndFoodItem(AppUser appUser, FoodItem foodItem);
    FoodOrder findByAppUserAndFoodItemAndArchiwum(AppUser appUser, FoodItem foodItem, boolean archiwum);
    FoodOrder findByAppUserAndFoodItemAndArchiwumAndKoszyk(AppUser appUser, FoodItem foodItem, boolean archiwum, boolean koszyk);
    // List  <FoodOrder> findByAppUserAndArchiwumAndKoszykAndStatus(AppUser appUser, boolean archiwum, boolean koszyk, Status status);
     FoodOrder findByAppUser_Id(long appuser_id);
     FoodOrder findById(long id);

     @Query(value = "SELECT sum(f.ilosc) FROM FoodOrder f WHERE f.appUser =?1 AND f.koszyk=true AND f.archiwum=false")
     public Integer findIloscByUser(AppUser appUser);

    @Query(value = "SELECT f FROM FoodOrder f WHERE f.appUser =?1 AND f.archiwum=?2 AND f.koszyk=?3" )
    List<FoodOrder> findByAppUserAndArchiwumAndKoszyk (AppUser appUser, boolean archiwum, boolean koszyk);

    @Query(value = "SELECT f FROM FoodOrder f WHERE f.appUser =?1 AND f.archiwum=?2 AND f.koszyk=?3 AND f.status=?4" )
    List  <FoodOrder> findByAppUserAndArchiwumAndKoszykAndStatus(AppUser appUser, boolean archiwum, boolean koszyk, Status status);

    //lista aktualnych zamówień dla admina
    @Query(value = "SELECT f FROM FoodOrder f WHERE f.archiwum=false AND f.koszyk=false")
    List  <FoodOrder> findCurrentOrders();

    //archiwum zamówień dla admina
    @Query(value = "SELECT f FROM FoodOrder f WHERE f.archiwum=true AND f.koszyk=false AND f.status IS NOT NULL")
    List  <FoodOrder> findOldOrders();

    @Query(value = "SELECT sum(f.ilosc) FROM FoodOrder f WHERE f.appUser =?1 AND f.archiwum=false")
    public Integer findIloscByUserAndArchiwumFalse(AppUser appUser);


    @Modifying
    @Query("UPDATE FoodOrder f SET f.ilosc = ?1 WHERE f.appUser = ?2 AND f.foodItem = ?3")
    public void updateQuantity(Integer quantity, AppUser appUser, FoodItem foodItem);

    @Modifying
    @Query("UPDATE FoodOrder f SET f.czas_zamowienia = ?1 WHERE f.appUser = ?2 AND f.id = ?3")
    public void updateCzasZamowienia(String czas_zamowienia, AppUser appUser, long id);

    @Modifying
    @Query("UPDATE FoodOrder f SET f.archiwum = ?1 WHERE f.appUser = ?2 AND f.foodItem = ?3")
    public void updateArchiwum(boolean archiwum, AppUser appUser, FoodItem foodItem);

    @Modifying
    @Query("UPDATE FoodOrder f SET f.koszyk = ?1 WHERE f.appUser = ?2 AND f.id = ?3")
    public void updateKoszyk(boolean koszyk, AppUser appUser, long id);

    @Modifying
    @Query("DELETE FoodOrder f WHERE f.appUser = ?1 AND f.archiwum=false")
    public void deleteByCustomer(AppUser appUser);


    @Modifying
    @Query("DELETE FROM FoodOrder f WHERE f.appUser = ?1 AND f.foodItem = ?2 AND f.archiwum=false")
    public void deleteByCustomerAndProduct(AppUser appUser, FoodItem foodItem);

    @Query("SELECT f.ilosc FROM FoodOrder f WHERE f.appUser = ?1 AND f.foodItem = ?2 AND f.archiwum=false")
    public int getIloscByUserAndProduct(AppUser appUser, FoodItem foodItem);

    @Modifying
    @Query("UPDATE FoodOrder f SET f.status = ?1 WHERE f.appUser = ?2 AND f.id = ?3 AND f.archiwum=false")
    public void updateStatus(Status status, AppUser appUser, long id);

    @Modifying
    @Query("UPDATE FoodOrder f SET f.status = ?1 WHERE f.appUser = ?2 AND f.foodItem = ?3 AND f.id=?4 ") //AND f.archiwum=false AND f.koszyk=true
    public void updateStatus2(Status status, AppUser appUser, FoodItem foodItem, long id);

    @Modifying
    @Query("UPDATE FoodOrder f SET f.dodatkowe_informacje = ?1 WHERE f.appUser = ?2 AND f.foodItem = ?3 AND f.archiwum=false")
    public void updateUwagi(String uwagi, AppUser appUser, FoodItem foodItem);

}

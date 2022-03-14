package pl.pracainzynierska2.praca;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import pl.pracainzynierska2.praca.Service.FoodOrderServiceImpl;
import pl.pracainzynierska2.praca.model.AppUser;
import pl.pracainzynierska2.praca.model.FoodItem;
import pl.pracainzynierska2.praca.model.FoodOrder;
import pl.pracainzynierska2.praca.model.Status;
import pl.pracainzynierska2.praca.repo.AppUserRepo;
import pl.pracainzynierska2.praca.repo.FoodItemRepo;
import pl.pracainzynierska2.praca.repo.FoodOrderRepo;
import pl.pracainzynierska2.praca.repo.StatusRepo;

@SpringBootTest
public class FoodOrderTest {
    @Autowired private FoodOrder foodOrder;
    @Autowired private FoodOrderRepo foodOrderRepo;
    @Autowired private AppUserRepo appUserRepo;
    @Autowired private AppUser appUser;
    @Autowired private FoodItem foodItem;
    @Autowired private FoodItemRepo foodItemRepo;
    @Autowired private FoodOrderServiceImpl foodOrderService;
    @Autowired private StatusRepo statusRepo;


    @Test
    @Transactional
    public void dodawanieProduktuTest(){
        appUser=appUserRepo.getById((long) 1);
        foodItem=foodItemRepo.findById(1);
    foodOrderService.addProduct(foodItem.getId(), 1, appUser);
    }
/*
    @Test
    @Transactional
    public void dodawanieProduktuDoKoszykaTest(){
   appUser=appUserRepo.getById((long) 1);
        foodItem=foodItemRepo.findById(1);
        FoodOrder foodOrder1=new FoodOrder();
        foodOrderRepo.save(foodOrder1);
        foodOrderService.addToCart(1, foodOrder1, appUser.getUsername());
    }
*/

@Test
@Transactional
public void zapisywanieZamowieniaTest(){
    foodOrderService.save(foodOrder);
}
    @Test
    @Transactional
    public void ustawianieStatusuTest(){
    Status status = new Status();
    status=statusRepo.findById(1);
    appUser=appUserRepo.getById((long) 1);
    foodItem=foodItemRepo.findById(1);
        foodOrderService.updateStatus2(status, appUser, foodItem, 1);
    }

    @Test
    @Transactional
    public void znajdzIloscPoUzytkownikuTest() {
        appUser=appUserRepo.getById((long) 1);
        foodItem=foodItemRepo.findById(1);
        foodOrderService.addProduct(foodItem.getId(), 1, appUser);
    foodOrderService.findIloscByAppUser(appUser);
    }

    @Test
    @Transactional
    public void ustawianieUwagiTest() {
        appUser=appUserRepo.getById((long) 1);
        foodItem=foodItemRepo.findById(1);
        foodOrderService.addProduct(foodItem.getId(), 1, appUser);
        foodOrderService.setUwagiForFoodOrder("uwaga", appUser, foodItem);
    }

    @Test
    @Transactional
    public void ustawianieArchiwumTest() {
        appUser=appUserRepo.getById((long) 1);
        foodItem=foodItemRepo.findById(1);
        foodOrderService.addProduct(foodItem.getId(), 1, appUser);
        foodOrderService.setArchiwum(false, appUser, foodItem);
    }

    @Test
    @Transactional
    public void ustawianieKoszykTest() {
        appUser=appUserRepo.getById((long) 1);
        foodItem=foodItemRepo.findById(1);
        foodOrderService.addProduct(foodItem.getId(), 1, appUser);
        foodOrderService.setKoszyk(false, appUser, 1);
    }

    @Test
    @Transactional
    public void usuwanieProduktuZKoszykaTest() {
        appUser=appUserRepo.getById((long) 1);
        foodItem=foodItemRepo.findById(1);
        foodOrderService.addProduct(foodItem.getId(), 1, appUser);
        foodOrderService.removeProduct(foodItem, appUser);
    }

    @Test
    @Transactional
    public void ustawianieCzasuZamowieniaTest() {
        appUser=appUserRepo.getById((long) 1);
        foodItem=foodItemRepo.findById(1);
        foodOrderService.addProduct(foodItem.getId(), 1, appUser);
        foodOrderService.updateCzasZamowienia("15-01-2021 22:22", appUser, 1);
    }

    @Test
    @Transactional
    public void znajdowanieZamowieniaPoIdTest() {
        appUser=appUserRepo.getById((long) 1);
        foodItem=foodItemRepo.findById(1);
        foodOrderService.addProduct(foodItem.getId(), 1, appUser);
        foodOrderService.findFoodOrderById(1);
    }

}


package pl.pracainzynierska2.praca.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pracainzynierska2.praca.model.AppUser;
import pl.pracainzynierska2.praca.model.FoodItem;
import pl.pracainzynierska2.praca.model.FoodOrder;
import pl.pracainzynierska2.praca.model.Status;
import pl.pracainzynierska2.praca.repo.AppUserRepo;
import pl.pracainzynierska2.praca.repo.FoodItemRepo;
import pl.pracainzynierska2.praca.repo.FoodOrderRepo;

import java.util.List;

@Service
public class FoodOrderServiceImpl implements FoodOrderService{
    @Autowired
private FoodOrderRepo foodOrderRepo;
private FoodOrder foodOrder;
@Autowired
private FoodItemRepo foodItemRepo;
@Autowired
private AppUser appUser;
private AppUserRepo appUserRepo;
    @Autowired
    public FoodOrderServiceImpl (FoodOrderRepo foodOrderRepo)
    {
        this.foodOrderRepo=foodOrderRepo;
    }

    @Override
    public FoodOrder saveFoodOrder(FoodOrder foodOrder) {
        this.foodOrder=foodOrder;
        return foodOrder;
    }

    public void save(FoodOrder foodOrder) {
        foodOrderRepo.save(foodOrder);
    }

    public List<FoodOrder> getFoodOrderList()
    {
        return foodOrderRepo.findAll();
    }
    @Override
    public void deleteById(long id) {
    foodOrderRepo.deleteById(id);
    }

    @Override
    public FoodOrder findFoodOrderById(long id)
    {
        return foodOrderRepo.findById(id);
    }



    public void addToCart(int produkt_id, FoodOrder foodOrder, String username)
    {
        FoodItem foodItem = foodItemRepo.findById((long)produkt_id);
        foodOrder.setFoodItem(foodItem);
        foodOrder.setCena_koncowa(foodItem.getCena());
        foodOrder.setAppUser(appUserRepo.findByUsername(username));
        foodOrderRepo.save(foodOrder);
    }

    public  List<FoodOrder> listFoodOrder(AppUser appUser){
        return foodOrderRepo.findByAppUser(appUser);
    }

    public List<FoodOrder> listFoodOrderInShoppingCart(AppUser appUser, boolean archiwum)
    {
       return foodOrderRepo.findByAppUserAndArchiwum(appUser, archiwum);
    }

    public List<FoodOrder> listFoodOrderInShoppingCart2(AppUser appUser, boolean archiwum, boolean w_koszyku)
    {
        return foodOrderRepo.findByAppUserAndArchiwumAndKoszyk(appUser, archiwum, w_koszyku);
    }

    public List<FoodOrder> listFoodOrderInShoppingCart3(AppUser appUser, boolean archiwum, boolean w_koszyku, Status status)
    {
        return foodOrderRepo.findByAppUserAndArchiwumAndKoszykAndStatus(appUser, archiwum, w_koszyku, status);
    }

    public void updateStatus2(Status status, AppUser appUser, FoodItem foodItem, long id)
    {
        foodOrderRepo.updateStatus2(status, appUser, foodItem, id);
    }


    public void updateCzasZamowienia(String czas_zamowienia, AppUser appUser, long id)
    {
        foodOrderRepo.updateCzasZamowienia(czas_zamowienia, appUser, id);
    }

    public List<FoodOrder> znajdzAktualneZamowienia()
    {
        return foodOrderRepo.findCurrentOrders();
    }

    public List<FoodOrder> znajdzStareZamowienia()
    {
        return foodOrderRepo.findOldOrders();
    }


    public Integer findIloscByAppUser (AppUser appUser){
        int ilosc;
        ilosc=foodOrderRepo.findIloscByUser(appUser);
        return ilosc;
    }


    public Integer addProduct(long fooditem_id, Integer ilosc, AppUser appUser){
        Integer dodana_ilosc=ilosc;
        FoodItem foodItem = foodItemRepo.findById(fooditem_id);
        FoodOrder foodOrder= foodOrderRepo.findByAppUserAndFoodItemAndArchiwumAndKoszyk(appUser, foodItem, false, true);

        if(foodOrder != null && foodOrder.isKoszyk()==true && foodOrder.isArchiwum()==false){
            dodana_ilosc=foodOrder.getIlosc()+ilosc;
            foodOrder.setIlosc(dodana_ilosc);
        }
        else{
            foodOrder=new FoodOrder();
            foodOrder.setIlosc(ilosc);
            foodOrder.setAppUser(appUser);
            foodOrder.setFoodItem(foodItem);
        }

        foodOrderRepo.save(foodOrder);
        return dodana_ilosc;
    }


    public void removeProduct(FoodItem foodItem,AppUser appUser) {
        foodOrderRepo.deleteByCustomerAndProduct(appUser, foodItem);
    }

    public int getIloscByUserAndItem(AppUser appUser, FoodItem foodItem)
    {
        foodOrderRepo.getIloscByUserAndProduct(appUser, foodItem);
        return foodOrderRepo.getIloscByUserAndProduct(appUser, foodItem);
    }

    public List<FoodOrder> getFoodItemListByAppUser(AppUser appUser)
    {
        foodOrderRepo.findByAppUser(appUser);
        return foodOrderRepo.findByAppUser(appUser);
    }

    public void setStatusForFoodOrder(Status status, AppUser appUser, long id)
    {
        foodOrderRepo.updateStatus(status,appUser,id);
    }

    public void setUwagiForFoodOrder(String uwagi, AppUser appUser, FoodItem foodItem)
    {
        foodOrderRepo.updateUwagi(uwagi, appUser, foodItem);
    }

    public void setArchiwum(boolean archiwum, AppUser appUser, FoodItem foodItem)
    {
        foodOrderRepo.updateArchiwum(archiwum, appUser, foodItem);
    }

    public void setKoszyk(boolean koszyk, AppUser appUser, long id)
    {
        foodOrderRepo.updateKoszyk(koszyk, appUser, id);
    }


}

package pl.pracainzynierska2.praca.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pracainzynierska2.praca.model.AppUser;
import pl.pracainzynierska2.praca.model.FoodItem;
import pl.pracainzynierska2.praca.model.FoodOrder;
import pl.pracainzynierska2.praca.repo.FoodItemRepo;
import pl.pracainzynierska2.praca.repo.FoodOrderRepo;

import java.util.List;

@Service
public class FoodItemServiceImpl implements FoodItemService{
    @Autowired
    private FoodItemRepo foodItemRepo;

    public FoodItemServiceImpl() {
    }

    public List<FoodItem> getFoodItemList()
    {
        return foodItemRepo.findAll();
    }

    @Autowired
    public FoodItemServiceImpl(FoodItemRepo foodItemRepo)
    {
        this.foodItemRepo=foodItemRepo;
    }

    @Override
    public void deleteById(long id) {
    foodItemRepo.deleteById(id);
    }

    @Override
    public FoodItem findById(long id) {
        return foodItemRepo.findById(id);
    }

    public void save(FoodItem foodItem) {
        foodItemRepo.save(foodItem);
    }


}

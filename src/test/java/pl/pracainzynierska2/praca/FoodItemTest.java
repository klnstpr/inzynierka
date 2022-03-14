package pl.pracainzynierska2.praca;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import pl.pracainzynierska2.praca.Service.FoodItemServiceImpl;

import pl.pracainzynierska2.praca.model.FoodItem;

import pl.pracainzynierska2.praca.repo.FoodItemRepo;

@SpringBootTest
public class FoodItemTest {
    @Autowired private FoodItemRepo foodItemRepo;
    @Autowired private FoodItem foodItem;
    @Autowired private FoodItemServiceImpl foodItemService;

    @Test
    @Transactional
    public void usuwaniePotrawyTest()
    {
        foodItemService.deleteById(1);
    }

    @Test
    @Transactional
    public void szukaniePotrawyPoIdTest()
    {
    foodItemService.findById(1);
    }

    @Test
    @Transactional
    public void zapisywaniePotrawyTest()
    {
        FoodItem foodItem3= new FoodItem();
    foodItemService.save(foodItem3);
    }


}

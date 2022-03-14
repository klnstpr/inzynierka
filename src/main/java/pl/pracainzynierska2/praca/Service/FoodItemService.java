package pl.pracainzynierska2.praca.Service;

import pl.pracainzynierska2.praca.model.FoodItem;

public interface FoodItemService {
   void deleteById(long id);
   FoodItem findById(long id);
}

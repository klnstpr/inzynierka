package pl.pracainzynierska2.praca.Service;

import pl.pracainzynierska2.praca.model.FoodOrder;

public interface FoodOrderService {
    FoodOrder saveFoodOrder (FoodOrder foodOrder);
    void deleteById(long id);
    FoodOrder findFoodOrderById(long id);
}

package pl.pracainzynierska2.praca.repo;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pracainzynierska2.praca.model.FoodItem;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodItemRepo extends JpaRepository <FoodItem, Long> {

    FoodItem save(FoodItem foodItem);

    FoodItem findById(long fooditem_id);
}

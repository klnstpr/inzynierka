package pl.pracainzynierska2.praca.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pracainzynierska2.praca.model.Status;


public interface StatusRepo extends JpaRepository<Status, Integer> {

    Status save(Status status);
    Status findById(int id);
    Status getById(int id);
}

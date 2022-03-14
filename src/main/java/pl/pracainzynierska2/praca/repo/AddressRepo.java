package pl.pracainzynierska2.praca.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pracainzynierska2.praca.model.Address;

@Repository
public interface AddressRepo extends JpaRepository <Address, Integer> {
    Address save(Address address);
    Address findById(int id);
    Address findByUlica(String ulica);
}

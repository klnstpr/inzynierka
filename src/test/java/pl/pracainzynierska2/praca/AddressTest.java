/*
package pl.pracainzynierska2.praca;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import pl.pracainzynierska2.praca.model.Address;
import pl.pracainzynierska2.praca.repo.AddressRepo;

@SpringBootTest
public class AddressTest {
    @Autowired private Address address;
    @Autowired private AddressRepo addressRepo;

    @Test
    @Transactional
    public void zapisywanieAdresu()
    {
        Address address2 = new Address();
        addressRepo.save(address2);
    }

    @Test
    @Transactional
    public void znajdowanieAdresuPoUlicyTest()
    {
        Address address2 = new Address("Zielona Góra", "Ulica", 1, 1);
        addressRepo.save(address2);
        addressRepo.findByUlica("Ulica");
    }

    @Test
    @Transactional
    public void znajdowanieAdresuPoIdTest()
    {
        Address address3 = new Address();
        addressRepo.save(address3);
        addressRepo.findById(1);
    }
}
*/
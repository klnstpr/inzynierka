package pl.pracainzynierska2.praca;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import pl.pracainzynierska2.praca.model.Address;
import pl.pracainzynierska2.praca.model.AppUser;
import pl.pracainzynierska2.praca.repo.AddressRepo;
import pl.pracainzynierska2.praca.repo.AppUserRepo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.List;

@SpringBootTest
public class AppUserTest {
    @Autowired private AppUser appUser;
    @Autowired private AppUserRepo appUserRepo;
    @Autowired private Address address;
    @Autowired private AddressRepo addressRepo;
    @Autowired UserDetailsServiceImpl userDetailsService;

    @Test
    @Transactional
    public void findByUsernameTest(){
        appUser=appUserRepo.getById((long) 1);
       String username= appUser.getUsername();
       appUserRepo.findByUsername(username);
    }
    @Test
    @Transactional
    public void findByEmailTest(){
        appUser=appUserRepo.getById((long) 1);
        String email= appUser.getEmail();
        appUserRepo.findByEmail(email);
    }

    @Test
    @Transactional
    public void findByTelefonTest(){
        appUser=appUserRepo.getById((long) 1);
        String telefon= appUser.getTelefon();
        appUserRepo.findByTelefon(telefon);
    }
    @Test
    @Transactional
    public void appUserSaveTest(){
        appUser=new AppUser("testuser", "Imie", "Nazwisko", "Haslo1321", "ROLE_USER", "email123@o2.pl", "500000111");
        appUserRepo.save(appUser);
    }

    @Test
    @Transactional
    public void appUserAddressTest(){
        appUser=new AppUser("testuser", "Imie", "Nazwisko", "Haslo1321", "ROLE_USER", "email123@o2.pl", "500000111");
        appUserRepo.save(appUser);
        address = new Address("Zielona Góra", "Ulica", 1111, 1111);
        addressRepo.save(address);
        appUserRepo.updateAdres(address, appUser.getId());
    }

    @Test
    @Transactional
    public List<AppUser> getAppUserListTest(){
        return userDetailsService.getUserList();
    }

    @Test
    @Transactional
    public void appUserDeleteTest(){
        userDetailsService.deleteByID(1);
    }

    @Test
    @Transactional
    public void appUserDeleteTestWithException() {
        UsernameNotFoundException usernameNotFoundException = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.deleteByID(4);
        });
    }

    @Test
    @Transactional
    public void findByEmailWithExceptionTest(){
        UsernameNotFoundException usernameNotFoundException = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.findByEmail("takiusernieistnieje@sdsd.pl");
        });
    }

    @Test
    @Transactional
    public void findByTelefonWithExceptionTest(){
        UsernameNotFoundException usernameNotFoundException = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.findByTelefon("012345678");
        });
    }

    @Test
    @Transactional
    public void findByIdWithExceptionTest(){
        UsernameNotFoundException usernameNotFoundException = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.getById(6);
        });
    }

}




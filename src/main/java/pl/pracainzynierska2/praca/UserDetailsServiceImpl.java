package pl.pracainzynierska2.praca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.pracainzynierska2.praca.model.Address;
import pl.pracainzynierska2.praca.model.AppUser;
import pl.pracainzynierska2.praca.repo.AppUserRepo;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    private AppUserRepo appUserRepo;
   // private PasswordEncoder passwordEncoder;
    private Address address;


    @Autowired
    public UserDetailsServiceImpl(AppUserRepo appUserRepo /*,PasswordEncoder passwordEncoder*/) {
        this.appUserRepo = appUserRepo;
       // this.passwordEncoder=passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return appUserRepo.findByUsername(s);
    }

    public List<AppUser> getUserList()
    {
        return appUserRepo.findAll();
    }

    public AppUser getById(long id) throws UsernameNotFoundException{
        AppUser result = appUserRepo.findById(id);
        if(result != null){
            return result;
        }
        throw new UsernameNotFoundException("Nie znaleziono użytkownika o podanym ID: "+id);
    }

    public AppUser findByEmail(String email) throws UsernameNotFoundException {
        AppUser result = appUserRepo.findByEmail(email);
        if(result == null) {
            throw new UsernameNotFoundException("Nie znaleziono użytkownika o podanym emailu");
        }
        throw new UsernameNotFoundException("Użytkownik o podanym emailu już istnieje");
    }

    public AppUser findByTelefon(String telefon) throws UsernameNotFoundException {
        AppUser result = appUserRepo.findByTelefon(telefon);
        if(result == null) {
            throw new UsernameNotFoundException("Nie znaleziono użytkownika o podanym numerze telefonu");
        }
        throw new UsernameNotFoundException("Użytkownik o podanym numerze telefonu już istnieje");
    }

    public void save(AppUser appUser) {
        appUserRepo.save(appUser);
    }

    public void deleteByID(long id){
        Long count = appUserRepo.countById(id);
        if(count==null || count==0)
        {
            throw new UsernameNotFoundException("Nie ma użytkownika o podanym ID: " +id);
        }
        appUserRepo.deleteById(id);
    }

    public void ustawAdres(Address address, long id)
    {
        appUserRepo.updateAdres(address, id);
    }
}

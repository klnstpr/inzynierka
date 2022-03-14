package pl.pracainzynierska2.praca;

import com.vaadin.flow.component.login.LoginForm;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.pracainzynierska2.praca.model.AppUser;
import pl.pracainzynierska2.praca.model.Token;
import pl.pracainzynierska2.praca.repo.AppUserRepo;
import pl.pracainzynierska2.praca.repo.TokenRepo;

import javax.validation.Valid;
import java.util.Optional;


@RestController
public class PracaApi {
    private AppUserRepo appUserRepo;
    private TokenRepo tokenRepo;

    public PracaApi(AppUserRepo appUserRepo, TokenRepo tokenRepo) {
        this.appUserRepo = appUserRepo;
        this.tokenRepo = tokenRepo;
    }


    @GetMapping("/zamowienie/dodaj")
    public String dodajZamowienie(){
        return "Panel do dodawania zamówień";
    }

    @GetMapping("/register/token")
    public String potwierdzenieRejestracji(@RequestParam String value)
    {
        Token byValue = tokenRepo.findByValue(value);
        AppUser appUser = byValue.getAppUser();
        appUser.setEnabled(true);
        appUserRepo.save(appUser);
        return "Użytkownik zarejestrowany pomyślnie. Możesz już się zalogować! ";
    }





    /*
    @RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(Authentication authentication) {
        if(authentication!=null) {
            return authentication.getName();
        }
        else
        {
            return "";
        }
    }
*/

}


package pl.pracainzynierska2.praca;

import com.vaadin.flow.component.ReconnectDialogConfiguration;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.pracainzynierska2.praca.MailService.MailService;
import pl.pracainzynierska2.praca.model.AppUser;
import pl.pracainzynierska2.praca.model.Token;
import pl.pracainzynierska2.praca.repo.AppUserRepo;
import pl.pracainzynierska2.praca.UserDetailsServiceImpl;
import pl.pracainzynierska2.praca.repo.TokenRepo;

import javax.mail.MessagingException;
import javax.validation.constraints.Null;
import java.util.UUID;

@EnableWebSecurity
@Route("register")
@PreserveOnRefresh
@UIScope
public class UserRegister  extends VerticalLayout {

    VerticalLayout vLayout = new VerticalLayout();


    private PasswordEncoder passwordEncoder;
    private AppUser appRegisterUser;
    private AppUser appRegisterUser2;
    private AppUserRepo appUserRepo;
    private UserDetailsServiceImpl userDetailsService;
    private TokenRepo tokenRepo;
    private MailService mailService;

    private String username;
    private String imie;
    private String nazwisko;
    private String haslo;
    private String role= "ROLE_USER";
    private String email;
    private String telefon;
    private String haslo2;

    TextField textFieldUsername = new TextField("Podaj nazwę użytkownika");
    TextField textFieldName = new TextField("Podaj imię");
    TextField textFieldLastname = new TextField("Podaj nazwisko");
    PasswordField passwordField = new PasswordField("Podaj hasło", "Wprowadź hasło");
    PasswordField passwordField2 = new PasswordField("Potwierdź hasło", "Potwierdź hasło");
    EmailField emailField = new EmailField("Podaj swój adres email");
    TextField numberField = new TextField("Podaj swój numer telefonu");

    Button buttonRegister = new Button("Zarejestruj się", new Icon(VaadinIcon.ACADEMY_CAP));
    Button buttonReturn = new Button("Powrót na stronę główną", new Icon(VaadinIcon.ACADEMY_CAP));
    //Label labelPotwierdz = new Label("Użytkownik");

    private void centerPage()
    {
    }

    private void registerUser() {
        username = textFieldUsername.getValue();
        imie=textFieldName.getValue();
        nazwisko= textFieldLastname.getValue();
        haslo=passwordField.getValue();
        haslo2=passwordField2.getValue();
        email= emailField.getValue();
        telefon=numberField.getValue();


        // AppUser appregisterUser = new AppUser (username, imie, nazwisko, passwordEncoder.encode(haslo), role, email, telefon);
        // appUserRepo.save(appregisterUser);
        //labelPotwierdz.setText(" dodany");

    }

    private void validationUser()
    {
        passwordField.setPattern("^(?=.*[0-9])(?=.*[a-zA-Z]).{8}.*$");
        passwordField2.setPattern("^(?=.*[0-9])(?=.*[a-zA-Z]).{8}.*$");
        textFieldLastname.setPattern("[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]*");
        textFieldName.setPattern("[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]*");
        passwordField.setHelperText("Hasło musi zawierać minimum 8 znaków, w tym minimum 1 cyfrę i literę.");
        numberField.setPattern("[1-9]{1}[0-9]{8}");
        //emailField.setClearButtonVisible(true);
        //emailField.setErrorMessage("Wprowadź poprawny adres email");

    }

    @Autowired
    public UserRegister(PasswordEncoder passwordEncoder, UserDetailsServiceImpl userDetailsService, AppUserRepo appUserRepo, TokenRepo tokenRepo, MailService mailService) {

        this.passwordEncoder = passwordEncoder;
        this.userDetailsService=userDetailsService;
        this.appUserRepo=appUserRepo;
        this.tokenRepo=tokenRepo;
        this.mailService=mailService;

        add(textFieldUsername, textFieldName, textFieldLastname, passwordField, passwordField2, emailField, numberField, buttonRegister, buttonReturn); //labelPotwierdz);
        validationUser();
        //centerPage();
        buttonRegister.addClickListener(clickEvent -> {
            validationUser();
            registerUser();

            if(username.trim().isEmpty()){
                Notification.show("Wprowadź login");
            }

            else if(imie.isEmpty() || !imie.matches("[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]*"))
            {
                Notification.show("Podaj swoje imię");
            }
            else if(nazwisko.isEmpty() || !nazwisko.matches("[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]*"))
            {
                Notification.show("Podaj swoje nazwisko");
            }
            else if(haslo.isEmpty() || !haslo.equals(haslo2) || !haslo.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8}.*$"))
            {
                Notification.show("Podaj hasło zgodnie z zaleceniem lub sprawdź czy hasła są takie same");
            }
            else if(emailField.isInvalid() || emailField.isEmpty())
            {
                Notification.show("Podaj poprawny adres email");
            }
            else if(numberField.isInvalid() || numberField.isEmpty())
            {
                Notification.show("Podaj poprawny numer telefonu (wzór: 500500500)");
            }
            else if(appUserRepo.findByUsername(username) != null)
            {
                Notification.show("Użytkownik o podanym loginie już istnieje");
            }
            else if(appUserRepo.findByEmail(email) != null)
            {
                Notification.show("Użytkownik o podanym emailu już istnieje");
            }
            else if(appUserRepo.findByTelefon(telefon) != null)
            {
                Notification.show("Użytkownik o podanym numerze telefonu już istnieje");
            }


            else if(appUserRepo.findByUsername(username) == null && appUserRepo.findByEmail(email) == null && appUserRepo.findByTelefon(telefon) == null){
                AppUser appregisterUser = new AppUser(username, imie, nazwisko, passwordEncoder.encode(haslo), role, email, telefon);
                appUserRepo.save(appregisterUser);
                    try {
                        sendToken(appregisterUser, mailService);
                        Notification.show("Potwierdź swój adres email, klikając na nim link aktywacyjny");
                    }
                    catch (MessagingException e) {
                        e.printStackTrace();
                    }
                    if(appregisterUser.isEnabled())
                    {
                        appUserRepo.save(appregisterUser);
                        Notification.show("Zarejestrowano pomyślnie");
                    }


            }




        } );

        buttonReturn.addClickListener(clickEvent -> UI.getCurrent().getPage().setLocation("/"));


    }

    private void sendToken(AppUser appRegisterUser, MailService mailService) throws MessagingException {
        this.mailService=mailService;
        this.appRegisterUser=appRegisterUser;
        String tokenValue = UUID.randomUUID().toString();
        Token token = new Token();
        token.setValue(tokenValue);
        token.setAppUser(appRegisterUser);
        tokenRepo.save(token);

        String url = "Potwierdź swój adres e-mail klikając w podany link: http://localhost:8088/register/token?value=" + tokenValue;

        mailService.sendMail(appRegisterUser.getEmail(), "Potwierdzenie rejestracji konta", url, false);
    }

    public UserRegister() {}
}

/* (!username.trim().isEmpty() && !haslo.isEmpty() && haslo.equals(haslo2) && !nazwisko.isEmpty() && !imie.isEmpty() ) */



/*
       this.id = id;
        this.username = username;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.haslo = haslo;
        this.role = role;
        this.email = email;
        this.telefon = telefon;
 */


/* zapisywanie użytkownika do bazy danych
    public void get(){
        AppUser appUser = new AppUser(1, "Jan123", "Jan", "Nowak",
                passwordEncoder().encode("1234"), "ROLE_USER", "jannowak@gmail.com", "500200300");
        AppUser appUser2 = new AppUser(2, "Admin", "Admin", "Admin",
                passwordEncoder().encode("admin1"), "ROLE_ADMIN", "admin@gmail.com", "500300300");
        appUserRepo.save(appUser);
        appUserRepo.save(appUser2);

    }
 */

/* kontrolki
 TextField textFieldName = new TextField("Podaj imię");
        Button buttonName = new Button("Hello", new Icon(VaadinIcon.ACADEMY_CAP));
        Label labelName = new Label();

        buttonName.addClickListener(clickEvent ->
                labelName.setText("hello " + textFieldName.getValue()));

        add(textFieldName, buttonName, labelName);
 */

        /*
            else if(appUserRepo.findByUsername(username).getUsername().equals(username))
            {
                Notification.show("Użytkownik o podanym loginie już istnieje");
            }

             */
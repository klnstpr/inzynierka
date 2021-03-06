
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

    TextField textFieldUsername = new TextField("Podaj nazw?? u??ytkownika");
    TextField textFieldName = new TextField("Podaj imi??");
    TextField textFieldLastname = new TextField("Podaj nazwisko");
    PasswordField passwordField = new PasswordField("Podaj has??o", "Wprowad?? has??o");
    PasswordField passwordField2 = new PasswordField("Potwierd?? has??o", "Potwierd?? has??o");
    EmailField emailField = new EmailField("Podaj sw??j adres email");
    TextField numberField = new TextField("Podaj sw??j numer telefonu");

    Button buttonRegister = new Button("Zarejestruj si??", new Icon(VaadinIcon.ACADEMY_CAP));
    Button buttonReturn = new Button("Powr??t na stron?? g????wn??", new Icon(VaadinIcon.ACADEMY_CAP));
    //Label labelPotwierdz = new Label("U??ytkownik");

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
        textFieldLastname.setPattern("[A-Za-z????????????????????????????????????]*");
        textFieldName.setPattern("[A-Za-z????????????????????????????????????]*");
        passwordField.setHelperText("Has??o musi zawiera?? minimum 8 znak??w, w tym minimum 1 cyfr?? i liter??.");
        numberField.setPattern("[1-9]{1}[0-9]{8}");
        //emailField.setClearButtonVisible(true);
        //emailField.setErrorMessage("Wprowad?? poprawny adres email");

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
                Notification.show("Wprowad?? login");
            }

            else if(imie.isEmpty() || !imie.matches("[A-Za-z????????????????????????????????????]*"))
            {
                Notification.show("Podaj swoje imi??");
            }
            else if(nazwisko.isEmpty() || !nazwisko.matches("[A-Za-z????????????????????????????????????]*"))
            {
                Notification.show("Podaj swoje nazwisko");
            }
            else if(haslo.isEmpty() || !haslo.equals(haslo2) || !haslo.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8}.*$"))
            {
                Notification.show("Podaj has??o zgodnie z zaleceniem lub sprawd?? czy has??a s?? takie same");
            }
            else if(emailField.isInvalid() || emailField.isEmpty())
            {
                Notification.show("Podaj poprawny adres email");
            }
            else if(numberField.isInvalid() || numberField.isEmpty())
            {
                Notification.show("Podaj poprawny numer telefonu (wz??r: 500500500)");
            }
            else if(appUserRepo.findByUsername(username) != null)
            {
                Notification.show("U??ytkownik o podanym loginie ju?? istnieje");
            }
            else if(appUserRepo.findByEmail(email) != null)
            {
                Notification.show("U??ytkownik o podanym emailu ju?? istnieje");
            }
            else if(appUserRepo.findByTelefon(telefon) != null)
            {
                Notification.show("U??ytkownik o podanym numerze telefonu ju?? istnieje");
            }


            else if(appUserRepo.findByUsername(username) == null && appUserRepo.findByEmail(email) == null && appUserRepo.findByTelefon(telefon) == null){
                AppUser appregisterUser = new AppUser(username, imie, nazwisko, passwordEncoder.encode(haslo), role, email, telefon);
                appUserRepo.save(appregisterUser);
                    try {
                        sendToken(appregisterUser, mailService);
                        Notification.show("Potwierd?? sw??j adres email, klikaj??c na nim link aktywacyjny");
                    }
                    catch (MessagingException e) {
                        e.printStackTrace();
                    }
                    if(appregisterUser.isEnabled())
                    {
                        appUserRepo.save(appregisterUser);
                        Notification.show("Zarejestrowano pomy??lnie");
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

        String url = "Potwierd?? sw??j adres e-mail klikaj??c w podany link: http://localhost:8088/register/token?value=" + tokenValue;

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


/* zapisywanie u??ytkownika do bazy danych
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
 TextField textFieldName = new TextField("Podaj imi??");
        Button buttonName = new Button("Hello", new Icon(VaadinIcon.ACADEMY_CAP));
        Label labelName = new Label();

        buttonName.addClickListener(clickEvent ->
                labelName.setText("hello " + textFieldName.getValue()));

        add(textFieldName, buttonName, labelName);
 */

        /*
            else if(appUserRepo.findByUsername(username).getUsername().equals(username))
            {
                Notification.show("U??ytkownik o podanym loginie ju?? istnieje");
            }

             */
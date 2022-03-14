/* Pierwotna strona startowa tworzona z wykorzystaniem Vaadin
package pl.pracainzynierska2.praca;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.pracainzynierska2.praca.model.AppUser;
import pl.pracainzynierska2.praca.repo.AppUserRepo;

@EnableWebSecurity
@PreserveOnRefresh
@UIScope

@Route(value="/")
public class StartPage extends VerticalLayout {
    private AppUserRepo appUserRepo;
    private AppUser appUserTest;
    private String role="ROLE_UNLOGGED";
    private String username;
    private UserDetailsServiceImpl userDetailsService;


    private void checkRole()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null || (authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            username = currentUserName;

            try {
                appUserTest = appUserRepo.findByUsername(username);
                if (appUserTest.getUsername() != null) {
                    role = appUserTest.getRole();
                } else {
                    role = "ROLE_UNLOGGED";
                }
            } catch (NullPointerException e) {
                //role = "ROLE_UNLOGGED";
                //e.printStackTrace();
            }
        }
    }


@Autowired
    public StartPage(AppUserRepo appUserRepo, AppUser appUserTest, UserDetailsServiceImpl userDetailsService)

    {

        this.userDetailsService=userDetailsService;
        this.appUserRepo=appUserRepo;
        this.appUserTest=appUserTest;

        MenuBar menuBar = new MenuBar();

        menuBar.setOpenOnHover(true);

        Text selected = new Text("");
        Div message = new Div(new Text("Selected: "), selected);

        MenuItem project = menuBar.addItem("Project");
        MenuItem account = menuBar.addItem("Konto");
        MenuItem panelAdmina = menuBar.addItem("Panel admina", e -> UI.getCurrent().getPage().setLocation("/admin"));
        MenuItem logowanie = menuBar.addItem("Zaloguj się", e -> UI.getCurrent().getPage().setLocation("/login"));
        MenuItem wylogowanie = menuBar.addItem("Wyloguj się", e -> UI.getCurrent().getPage().setLocation("/logout"));
        //panelAdmina.setVisible(false);


        SubMenu projectSubMenu = project.getSubMenu();
        MenuItem users = projectSubMenu.addItem("Users");
        MenuItem billing = projectSubMenu.addItem("Billing");

        SubMenu usersSubMenu = users.getSubMenu();
        usersSubMenu.addItem("List", e -> selected.setText("List"));
        usersSubMenu.addItem("Add", e -> selected.setText("Add"));

        SubMenu billingSubMenu = billing.getSubMenu();
        billingSubMenu.addItem("Invoices", e -> selected.setText("Invoices"));
        billingSubMenu.addItem("Balance Events",
                e -> selected.setText("Balance Events"));

        account.getSubMenu().addItem("Zarejestruj się",
                e -> UI.getCurrent().navigate("register"));
                account.getSubMenu().addItem("Ustawienia prywatności",
                e -> selected.setText("Ustawienia prywatności"));
        add(menuBar, message);


        checkRole();
        if(role.equals("ROLE_ADMIN"))
        {
            panelAdmina.setVisible(true);
            logowanie.setVisible(false);
            wylogowanie.setVisible(true);
        }
        if(role.equals("ROLE_USER"))
        {
            panelAdmina.setVisible(false);
            logowanie.setVisible(false);
            wylogowanie.setVisible(true);
        }
        if (role.equals("ROLE_UNLOGGED"))
        {
            logowanie.setVisible(true);
            panelAdmina.setVisible(false);
            wylogowanie.setVisible(false);
        }


        /*
        TextField textFieldName = new TextField("Podaj imię");
        Button buttonName = new Button("Hello", new Icon(VaadinIcon.ACADEMY_CAP));
        Label labelName = new Label();

        buttonName.addClickListener(clickEvent ->
                labelName.setText("hello " + textFieldName.getValue()));

        add(textFieldName, buttonName, labelName);

                is_admin();
        panelAdmina.setVisible(false);

             if(role =="ROLE_ADMIN")
        {
            panelAdmina.setVisible(true);
        }

        AppUser appUser,  AppUserRepo appUserRepo, UserDetailsServiceImpl userDetailsService
            this.appUser=appUser;
        this.appUserRepo=appUserRepo;
        this.userDetailsService=userDetailsService;

         */
    //}
//}

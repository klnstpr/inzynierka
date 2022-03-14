package pl.pracainzynierska2.praca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.pracainzynierska2.praca.model.Address;
import pl.pracainzynierska2.praca.model.AppUser;
import pl.pracainzynierska2.praca.model.FoodItem;
import pl.pracainzynierska2.praca.model.Status;
import pl.pracainzynierska2.praca.repo.*;


@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;
    private AppUserRepo appUserRepo;
    private FoodItem foodItem;
    private FoodItemRepo foodItemRepo;
    private StatusRepo statusRepo;
    @Autowired
    private AddressRepo addressRepo;


    @Autowired
    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, AppUserRepo appUserRepo, FoodItem foodItem, FoodItemRepo foodItemRepo, StatusRepo statusRepo  ) {
        this.userDetailsService = userDetailsService;
        this.appUserRepo=appUserRepo;
        this.foodItemRepo=foodItemRepo;
        this.foodItem=foodItem;
        this.statusRepo=statusRepo;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().defaultSuccessUrl("/", true);
        http.authorizeRequests()
               // .antMatchers("/").hasAnyRole("USER", "ADMIN")
                .antMatchers("/templates/**").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/index").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/vaadinServlet/UIDL/**").permitAll()
                .antMatchers("/vaadinServlet/HEARTBEAT/**").permitAll()
                .antMatchers("/zamowienie/dodaj").hasRole("ADMIN")
                .antMatchers("/admin*").hasRole("ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/admin/users").hasRole("ADMIN")
                .antMatchers("/admin/users/edit").hasRole("ADMIN")
                .antMatchers("/admin/users/delete").hasRole("ADMIN")
                .antMatchers("/admin/users/edit/*").hasRole("ADMIN")
                .antMatchers("/admin/users/delete/*").hasRole("ADMIN")
                .antMatchers("/admin/menu").hasRole("ADMIN")
                .antMatchers("/admin/menu/*").hasRole("ADMIN")
                .antMatchers("/admin/menu/edit").hasRole("ADMIN")
                .antMatchers("/admin/menu/delete").hasRole("ADMIN")
                .antMatchers("/admin/menu/edit/*").hasRole("ADMIN")
                .antMatchers("/admin/menu/delete/*").hasRole("ADMIN")
                .and()
                .formLogin().loginPage("/login").permitAll().and().rememberMe()
                .and()
                .csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //testowe tworzenie użytkownika
    @EventListener(ApplicationReadyEvent.class)
    public void get(){
        AppUser appUser = new AppUser(/*1,*/ "Jan123", "Jan", "Nowak",
                passwordEncoder().encode("1234"), "ROLE_USER", "jannowak@gmail.com", "500200300");
        AppUser appUser2 = new AppUser(/*2,*/ "Admin", "Administrator", "Admin",
                passwordEncoder().encode("admin"), "ROLE_ADMIN", "admin@gmail.com", "500300300");
        appUser.setEnabled(true);
        appUser2.setEnabled(true);
        Address address = new Address("Zielona Góra", "Podgórna", 942, 1);
        addressRepo.save(address);
        appUser2.setAddress(address);
        appUserRepo.save(appUser);
        appUserRepo.save(appUser2);

        //przykładowe dania
        FoodItem foodItem = new FoodItem("Pizza kebab", 32.50, "Pizza z cielęcym mięsem kebab, serem mozarella, pieczarkami i cebulą.", true);
        foodItem.setUrl("https://media.istockphoto.com/photos/delicious-fresh-kebab-pizza-dinner-served-on-plate-on-wooden-table-picture-id694114220");
        foodItem.setDostepne(true);
        foodItemRepo.save(foodItem);

        FoodItem foodItem2 = new FoodItem("Pizza salami", 34.99, "Pizza z kiełbasą salami, serem mozarella i bazylią", true);
        foodItem2.setUrl("https://thumbs.dreamstime.com/z/pizza-salami-27986340.jpg");
        foodItem2.setDostepne(true);
        foodItemRepo.save(foodItem2);

        FoodItem foodItem3 = new FoodItem("Pizza 4 sery", 33.99, "Pizza z serem mozarella, parmezanem, gorgonzolą oraz serem fontina", false);
        foodItem3.setUrl("https://media.istockphoto.com/photos/pizza-with-cheese-isolated-on-white-background-clipping-path-full-of-picture-id1295773428?k=20&m=1295773428&s=612x612&w=0&h=CAlttj4z_cXWebcwG6bg36ZLUUk_sTUSfqbMdqrit8c=");
        foodItem3.setDostepne(false);
        foodItemRepo.save(foodItem3);



        //statusy zamówienia
        // 1 = zapłacone, do doręczenia
        // 2 = płatność gotówką
        // 3 = w doręczeniu
        // 4 = doręczono
        // 5 = zamówienie anulowane

        Status status1 = new Status(1, "Zapłacono, do doręczenia");
        Status status2 = new Status(2, "Płatność gotówką przy odbiorze, do doręczenia");
        Status status3 = new Status(3, "W doręczeniu");
        Status status4 = new Status(4, "Doręczono");
        Status status5 = new Status(5, "Anulowane");
        statusRepo.save(status1);
        statusRepo.save(status2);
        statusRepo.save(status3);
        statusRepo.save(status4);
        statusRepo.save(status5);
    }



}

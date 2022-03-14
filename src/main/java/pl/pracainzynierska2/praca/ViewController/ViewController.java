package pl.pracainzynierska2.praca.ViewController;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.auditing.config.AuditingBeanDefinitionRegistrarSupport;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.pracainzynierska2.praca.Service.AddressServiceImpl;
import pl.pracainzynierska2.praca.Service.FoodItemServiceImpl;
import pl.pracainzynierska2.praca.Service.FoodOrderServiceImpl;
import pl.pracainzynierska2.praca.Service.StatusServiceImpl;
import pl.pracainzynierska2.praca.UserDetailsServiceImpl;
import pl.pracainzynierska2.praca.model.*;
import pl.pracainzynierska2.praca.repo.*;

import javax.validation.Valid;
import java.security.Principal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;



@Controller
@Configuration


public class ViewController implements WebMvcConfigurer {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    //public String currentUserName1;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private AppUser appUser;
    private AppUserRepo appUserRepo;
    @Autowired
    private FoodItemServiceImpl foodItemServiceImpl;
    private FoodItem foodItem;
    @Autowired
    private FoodOrderRepo foodOrderRepo;
    //@Autowired
    private FoodOrder foodOrder;
    @Autowired
    private FoodOrderServiceImpl foodOrderServiceImpl;
    @Autowired
    private FoodItemRepo foodItemRepo;
    @Autowired
    private Status status;
    @Autowired
    private StatusServiceImpl statusService;
    @Autowired
    private StatusRepo statusRepo;
    @Autowired
    private AddressRepo addressRepo;
    @Autowired
    private AddressServiceImpl addressService;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/admin.html").setViewName("admin");
      //  registry.addViewController("/admin/users.html").setViewName("admin/users");
       // registry.addViewController("/admin/users/edit/{id}.html").setViewName("admin/users/edit");
       // registry.addViewController( "/" ).setViewName( "forward:/home" );
      //  registry.setOrder( Ordered.HIGHEST_PRECEDENCE );
    }

    @GetMapping("/login")
    public String viewLoginPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }

            //this.currentUserName1 = authentication.getName();
            return "redirect:/";

    }


    @GetMapping("/")
    public String viewIndex(Model m, Model model, Authentication authentication)
    {
        if(authentication != null) {
            String username = authentication.getName();
            AppUser appUser = (AppUser) userDetailsService.loadUserByUsername(username);
            try {
                if (foodOrderServiceImpl.findIloscByAppUser(appUser) != null) {
                    m.addAttribute("quantity", foodOrderServiceImpl.findIloscByAppUser(appUser));
                    //System.out.println(m);
                }
            }
            catch (NullPointerException n)
            {
              //  System.out.println(m);
            }
        }

        try{
            m.addAttribute("menuList2", foodItemServiceImpl.getFoodItemList());
            model.addAttribute("zamowienie", new FoodOrder());
          //  m.addAttribute("quantity", foodOrder.getIlosc());

        }
        catch (NullPointerException n)
        {
            System.out.println("brak menu");
            return "index";
        }
        System.out.println(m);
        return "index";
    }

    @GetMapping("/koszyk")
    public String viewKoszyk(Model m, Authentication authentication)
    {
        if(authentication != null) {
            String username = authentication.getName();
            AppUser appUser = (AppUser) userDetailsService.loadUserByUsername(username);
            List<FoodOrder> foodOrderList = foodOrderServiceImpl.listFoodOrderInShoppingCart2(appUser, false, true);
            long user_id=appUser.getId();
            double cena_zamowienia = 0.0f;

            for (FoodOrder item : foodOrderList) {
                item.setCena_koncowa(item.getSubtotal());
                cena_zamowienia += item.getSubtotal();
                int temp = (int)(cena_zamowienia*100.0);
                cena_zamowienia = ((double)temp)/100.0;
            }

            try {
                m.addAttribute("zamowienieList", foodOrderList);
                m.addAttribute("pageTitle", "Koszyk");
                m.addAttribute("cena_zamowienia", cena_zamowienia);
                m.addAttribute("user_id", user_id);

                if (foodOrderServiceImpl.findIloscByAppUser(appUser) != null)
                {
                    m.addAttribute("quantity", foodOrderServiceImpl.findIloscByAppUser(appUser));
                }
            } catch (NullPointerException n) {
                System.out.println("brak zamówień");
                return "koszyk";
            }
        }
        System.out.println(m);
        return "koszyk";
    }
    @PostMapping("/koszyk/add/{pid}/{qty}")
    public String addToCart(@PathVariable("pid") Integer productId, @PathVariable("qty") Integer ilosc, Authentication authentication){
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "Musisz się zalogować lub zarejestrować, aby złożyć zamówienie";
        }
        String username = authentication.getName();
        AppUser appUser = (AppUser) userDetailsService.loadUserByUsername(username);
        if( appUser == null)
        {
            return "Musisz się zalogować lub zarejestrować, aby złożyć zamówienie";
        }
        Integer dodanaIlosc = foodOrderServiceImpl.addProduct(productId, ilosc, appUser);
        return "index";
    }

    @PostMapping("/koszyk/add/{pid}")
    public String addToCart2(@PathVariable("pid") Integer productId, Authentication authentication){
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        String username = authentication.getName();
        AppUser appUser = (AppUser) userDetailsService.loadUserByUsername(username);
        if( appUser == null)
        {
            return "redirect:/login";
        }

        Integer dodanaIlosc = foodOrderServiceImpl.addProduct(productId, 1, appUser);
       // quantity=quantity+1;
        return "redirect:/";
    }

    @GetMapping("/koszyk/delete/{productId}")
    public String removeProduct(@PathVariable("productId") Integer productId, Authentication authentication) {
        try {
            String username = authentication.getName();
            AppUser appUser = (AppUser) userDetailsService.loadUserByUsername(username);
            foodItem = foodItemRepo.findById(productId);
            long item_id = foodItem.getId();
          //  System.out.println(appUser);
            // System.out.println(foodItem);
            foodOrderServiceImpl.setArchiwum(true, appUser, foodItem);
            foodOrderServiceImpl.setKoszyk(false, appUser, item_id);
            foodOrderServiceImpl.removeProduct(foodItem, appUser);
            return "redirect:/koszyk";

        } catch (UsernameNotFoundException e) {
            return "redirect:/login";
        }
    }

    @GetMapping("/koszyk/plus/{productId}")
    public String productPlus(@PathVariable("productId") Integer productId, Authentication authentication) {
        try {
            String username = authentication.getName();
            AppUser appUser = (AppUser) userDetailsService.loadUserByUsername(username);
            if( appUser == null)
            {
                return "redirect:/login";
            }
            Integer dodanaIlosc = foodOrderServiceImpl.addProduct(productId, 1, appUser);
            return "redirect:/koszyk";

        } catch (UsernameNotFoundException e) {
            return "redirect:/login";
        }
    }

    @GetMapping("/koszyk/minus/{productId}")
    public String productMinus(@PathVariable("productId") Integer productId, Authentication authentication) {
        try {
            String username = authentication.getName();
            AppUser appUser = (AppUser) userDetailsService.loadUserByUsername(username);
            if( appUser == null)
            {
                return "redirect:/login";
            }
            foodItem = foodItemRepo.findById(productId);
            int ilosc = foodOrderServiceImpl.getIloscByUserAndItem(appUser, foodItem);
            if(ilosc>1) {
                Integer dodanaIlosc = foodOrderServiceImpl.addProduct(productId, -1, appUser);
                return "redirect:/koszyk";
            }
            return "redirect:/koszyk";
        } catch (UsernameNotFoundException e) {
            return "redirect:/login";
        }
    }

    @PostMapping("/koszyk/platnosc/{appuser_id}")
    public String koszykZaplac(@PathVariable("appuser_id") Integer appuser_id, Authentication authentication, Model model) {
        try {
            String username = authentication.getName();
            AppUser appUser = (AppUser) userDetailsService.loadUserByUsername(username);
            if (appUser == null) {
                return "redirect:/login";
            }
            List<FoodOrder> foodOrderList = foodOrderServiceImpl.listFoodOrderInShoppingCart2(appUser, false, true);
            double cena_zamowienia = 0;

            for (FoodOrder item : foodOrderList) {
                item.setCena_koncowa(item.getSubtotal());
                cena_zamowienia += item.getSubtotal();
                int temp = (int)(cena_zamowienia*100.0);
                cena_zamowienia = ((double)temp)/100.0;
            }

            model.addAttribute("zamowienieList", foodOrderList);
            model.addAttribute("cena_zamowienia", cena_zamowienia);
            model.addAttribute("quantity", foodOrderServiceImpl.findIloscByAppUser(appUser));
            model.addAttribute("zamowienie", new FoodOrder());
            model.addAttribute("user", userDetailsService.getById(appuser_id));
            return "koszyk/platnosc";
        }

         catch(UsernameNotFoundException e){
                return "redirect:/login";
            }

    }
    @GetMapping("/koszyk/uwagi/{id}")
    public String dodajUwage(@PathVariable Integer id, Authentication authentication, Model model) {
        try {
            String username = authentication.getName();
            AppUser appUser = (AppUser) userDetailsService.loadUserByUsername(username);
            if (appUser == null) {
                return "redirect:/login";
            }

            model.addAttribute("potrawa", foodOrderServiceImpl.findFoodOrderById(id));
            System.out.println(foodOrderServiceImpl.findFoodOrderById(id));
            model.addAttribute("quantity", foodOrderServiceImpl.findIloscByAppUser(appUser));
            return "koszyk/uwagi";
        }

        catch(UsernameNotFoundException e){
            return "redirect:/login";
        }
    }

    @PostMapping("/koszyk/uwagi/save")
    public String dodajUwage(FoodOrder foodOrder)
    {
        try
        {
            foodOrderRepo.save(foodOrder);
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        return "redirect:/koszyk";
    }


    @GetMapping("/koszyk/platnosc/online")
    public String platnoscZaakceptowana(Authentication authentication, Model model) {

        //imitacja płatności, zaakceptowana lub nie
        //1-25 niezaakceptowana
        //26-50 zaakceptowana
        //platnosc gotowka - zaakceptowane
        Random rand = new Random();
        int n = rand.nextInt(50);
        Status status1;
        status1=statusRepo.findById(1);
        try {
            String username = authentication.getName();
            AppUser appUser = (AppUser) userDetailsService.loadUserByUsername(username);
            if (appUser == null) {
                return "redirect:/login";
            }
            java.util.Date dt = new java.util.Date();
            java.text.SimpleDateFormat sdf =
                    new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(dt);



            List<FoodOrder> foodOrderList = foodOrderServiceImpl.listFoodOrderInShoppingCart2(appUser, false, true);
            System.out.println("Lista w płatności online:");
            System.out.println(foodOrderList);
            for (FoodOrder item : foodOrderList) {
                long item_id= item.getId();
                System.out.println(item_id);

                if (n < 26 ) {//&& item.getStatus()==null) {
                   status=statusRepo.findById(5);
                    foodItem=foodItemRepo.findById(item.getId());
                  //foodOrderServiceImpl.setStatusForFoodOrder(status, appUser, foodItem);

                  //  System.out.println(item);
                    System.out.println("5");
                }
                if (n >= 26 && item.getStatus()==null)
                    {
                       // status=statusRepo.findById(1);
                        foodItem=foodItemRepo.findById(item.getId());
                        foodOrderServiceImpl.setStatusForFoodOrder(status1, appUser, item_id);
                        //foodOrderServiceImpl.updateStatus2(status, appUser, foodItem, item_id);
                        foodOrderServiceImpl.updateCzasZamowienia(currentTime, appUser, item_id);
                        foodOrderServiceImpl.setKoszyk(false, appUser, item_id);
                        System.out.println("item:");
                        System.out.println(item);

                  //      System.out.println(item);
                        System.out.println("1");

                }

            }

        }
        catch(UsernameNotFoundException e){
            return "redirect:/login";
        }

            if (n>=26)
            {
                return "redirect:/koszyk/platnosc/online/udana";
            }
            else return "redirect:/koszyk/platnosc/online/nieudana";

    }

    @GetMapping("/koszyk/platnosc/online/nieudana")
    public String platnoscNieudana(Authentication authentication, Model model) {
        try {
            String username = authentication.getName();
            AppUser appUser = (AppUser) userDetailsService.loadUserByUsername(username);
            if (appUser == null) {
                return "redirect:/login";
            }
        }
        catch(UsernameNotFoundException e){
            return "redirect:/login";
        }
        return "koszyk/platnosc/online/nieudana";
    }

    @GetMapping("/koszyk/platnosc/online/udana")
    public String platnoscUdana(Authentication authentication, Model model) {
        try {
            String username = authentication.getName();
            AppUser appUser = (AppUser) userDetailsService.loadUserByUsername(username);
            if (appUser == null) {
                return "redirect:/login";
            }
            List<FoodOrder> foodOrderList = foodOrderServiceImpl.listFoodOrderInShoppingCart2(appUser, false, false);
            System.out.println("Płatnośc udana:");
            System.out.println(foodOrderList);
            for (FoodOrder item : foodOrderList) {
              long  item_id=item.getId();
             //   status=statusRepo.findById(1);
                foodItem=foodItemRepo.findById(item.getId());
                //foodOrderServiceImpl.setStatusForFoodOrder(status, appUser, foodItem);
                foodOrderServiceImpl.setKoszyk(false, appUser, item_id);
                // foodOrderServiceImpl.setArchiwum(true, appUser, foodItem);
                System.out.println("1");

            }
           // List<FoodOrder> listFoodOrderInShoppingCart2 = foodOrderServiceImpl.listFoodOrderInShoppingCart2(appUser, false, false);
            model.addAttribute("zamowienie", foodOrderList);


            // System.out.println("3");
        }

        catch(UsernameNotFoundException e){
            return "redirect:/login";
        }
        return "koszyk/platnosc/online/udana";
    }

        @GetMapping("/koszyk/platnosc/gotowka")
    public String platnosGotowka(Authentication authentication, Model model) {

        //imitacja płatności, zaakceptowana lub nie
        //1-25 niezaakceptowana
        //26-50 zaakceptowana
        //platnosc gotowka - zaakceptowane


        try {
            java.util.Date dt = new java.util.Date();
            java.text.SimpleDateFormat sdf =
                    new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(dt);
            String username = authentication.getName();
            AppUser appUser = (AppUser) userDetailsService.loadUserByUsername(username);
            if (appUser == null) {
                return "redirect:/login";
            }
            Status status2;
            status2=statusRepo.findById(2);
            List<FoodOrder> foodOrderList = foodOrderServiceImpl.listFoodOrder(appUser);

            for (FoodOrder item : foodOrderList) {
                long item_id= item.getId();
                    foodItem=foodItemRepo.findById(item.getId());
                 foodOrderServiceImpl.setStatusForFoodOrder(status2, appUser, item_id);
                    foodOrderServiceImpl.setKoszyk(false, appUser, item_id);
                   // foodOrderServiceImpl.setArchiwum(true, appUser, foodItem);
                    foodOrderServiceImpl.updateCzasZamowienia(currentTime, appUser, item_id);
                    System.out.println("2");
                List<FoodOrder> listFoodOrderInShoppingCart2 = foodOrderServiceImpl.listFoodOrderInShoppingCart2(appUser, false, false);
                model.addAttribute("zamowienie", listFoodOrderInShoppingCart2);

            }

            System.out.println("3");
        }

        catch(UsernameNotFoundException e){
            return "redirect:/login";
        }
        return "redirect:/koszyk/platnosc/gotowka/udana";
    }

    @GetMapping("/koszyk/platnosc/gotowka/udana")
    public String platnoscGotowkaUdana(Authentication authentication, Model model) {
        try {
            String username = authentication.getName();
            AppUser appUser = (AppUser) userDetailsService.loadUserByUsername(username);
            if (appUser == null) {
                return "redirect:/login";
            }
            List<FoodOrder> foodOrderList = foodOrderServiceImpl.listFoodOrderInShoppingCart2(appUser, false, false);
            System.out.println("Płatnośc gotówką");
            System.out.println(foodOrderList);
            for (FoodOrder item : foodOrderList) {
                long item_id= item.getId();
                //   status=statusRepo.findById(1);
                foodItem = foodItemRepo.findById(item.getId());
                //foodOrderServiceImpl.setStatusForFoodOrder(status, appUser, foodItem);
                foodOrderServiceImpl.setKoszyk(false, appUser, item_id);
                // foodOrderServiceImpl.setArchiwum(true, appUser, foodItem);
                System.out.println("1");

            }
            // List<FoodOrder> listFoodOrderInShoppingCart2 = foodOrderServiceImpl.listFoodOrderInShoppingCart2(appUser, false, false);
            model.addAttribute("zamowienie", foodOrderList);


            // System.out.println("3");
        }
        catch(UsernameNotFoundException e){
            return "redirect:/login";
        }
        return "/koszyk/platnosc/gotowka/udana";
    }

    @GetMapping("/uzytkownik/adres")
    String wprowadzAdres(Authentication authentication, Model model){
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        String username = authentication.getName();
        AppUser appUser = (AppUser) userDetailsService.loadUserByUsername(username);
        if( appUser == null)
        {
            return "redirect:/login";
        }
        model.addAttribute("adres", new Address());

        return "uzytkownik/adres";
    }

    @PostMapping("/uzytkownik/adres/save")
            String zapiszAdres(Authentication authentication, Address address)
    {
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        String username = authentication.getName();
        AppUser appUser = (AppUser) userDetailsService.loadUserByUsername(username);
        if( appUser == null)
        {
            return "redirect:/login";
        }
        try{
            address.setMiasto("Zielona Góra");
            addressService.zapiszAdres(address);
            userDetailsService.ustawAdres(address, appUser.getId());
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/admin")
    String index(Principal principal) {
        return principal != null ? "admin" : "/";
    }

    @GetMapping("/kontakt")
    String kontakt() {
        return "/kontakt";
    }

    @GetMapping("/about")
    String about() {
    return "/about";
    }

    @GetMapping("/admin/users")
    public String getUserList(Model m)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        m.addAttribute("userList", userDetailsService.getUserList());
        //System.out.println(m);
        return "admin/users";
    }

    @GetMapping("/admin/users/add")
    public String addNewUser(Model model){
        model.addAttribute("users", new AppUser());
        model.addAttribute("pageTittle", "Dodawanie użytkownika");
        return "admin/users/add";
    }



    @PostMapping("/admin/users/add/save")
    public String addAndSaveUser(AppUser appUser, RedirectAttributes ra)
    {
        String haslo;
        haslo=passwordEncoder.encode(appUser.getHaslo());
        if (!StringUtils.isEmpty(appUser.getPassword())) {
            appUser.setHaslo(haslo);
            userDetailsService.save(appUser);
        }
       // userDetailsService.save(appUser);
        ra.addFlashAttribute("message", "Użytkownik zapisany pomyślnie!");
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/edit/{id}")
    public String getEditForm(@PathVariable("id") long id, Model m, RedirectAttributes redirectAttributes)
    {
       try {
           m.addAttribute("users", userDetailsService.getById(id));
           m.addAttribute("pageTittle","Edycja użytkownika (ID: " +id +")");
          // redirectAttributes.addFlashAttribute("message_s", "Użytkownik edytowany pomyślnie!");
           return "admin/users/edit";
       } catch (UsernameNotFoundException e){
           //redirectAttributes.addFlashAttribute("message_f", "Użytkownik edytowany pomyślnie.");
           return "admin/users";
       }
    }

    @PostMapping("/admin/users/edit/save")
    public String saveUser(AppUser appUser, RedirectAttributes ra)
    {
        userDetailsService.save(appUser);
        ra.addFlashAttribute("message", "Użytkownik edytowany pomyślnie!");
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/delete/{id}")
    public String getEditForm(@PathVariable("id") long id, RedirectAttributes redirectAttributes)
    {
        try {
            userDetailsService.deleteByID(id);
             redirectAttributes.addFlashAttribute("message", "Użytkownik usunięty pomyślnie!");

        } catch (UsernameNotFoundException e){
            redirectAttributes.addFlashAttribute("message", "Wystąpił błąd.");

        }
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/menu")
    public String getMenuList(Model m)
    {
        try{
            m.addAttribute("menuList", foodItemServiceImpl.getFoodItemList());
        }
        catch (NullPointerException n)
        {
            return "admin/menu";
        }
        System.out.println(m);
        return "admin/menu";

    }

    @GetMapping("/admin/menu/add")
    public String addNewFoodItem(Model model){
        model.addAttribute("menu", new FoodItem());
        model.addAttribute("pageTittle", "Dodawanie dania");
        return "admin/menu/add";
    }

    @PostMapping("/admin/menu/add/save")
    public String addAndSaveFoodItem(FoodItem fooditem, RedirectAttributes ra)
    {
        foodItemServiceImpl.save(fooditem);
        ra.addFlashAttribute("message", "Danie dodano pomyślnie!");
        return "redirect:/admin/menu";
    }

    @GetMapping("/admin/menu/edit/{id}")
    public String getFoodEditForm(@PathVariable("id") long id, Model m, RedirectAttributes redirectAttributes)
    {
        try {
            m.addAttribute("menu", foodItemServiceImpl.findById(id));
            m.addAttribute("pageTittle","Edycja dania (ID: " +id +")");
            return "admin/menu/edit";
        } catch (NullPointerException e){
            return "admin/menu";
        }
    }

    @PostMapping("/admin/menu/edit/save")
    public String saveMenu(FoodItem fooditem, RedirectAttributes ra)
    {
        foodItemServiceImpl.save(fooditem);
        ra.addFlashAttribute("message", "Danie edytowane pomyślnie!");
        return "redirect:/admin/menu";
    }

    @GetMapping("/admin/menu/delete/{id}")
    public String deleteFoodItem(@PathVariable("id") long id, RedirectAttributes redirectAttributes)
    {
        try {
            foodItemServiceImpl.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Danie usunięte pomyślnie!");

        } catch (NullPointerException e){
            redirectAttributes.addFlashAttribute("message", "Wystąpił błąd.");

        }
        return "redirect:/admin/menu";
    }

    @GetMapping("/admin/zamowienia")
    public String getAktualneZamowienia(Model m)
    {
        try{
            m.addAttribute("aktualneZamowienia", foodOrderServiceImpl.znajdzAktualneZamowienia());
        }
        catch (NullPointerException n)
        {
            return "admin";
        }
        return "admin/zamowienia";
    }

    @GetMapping("/admin/zamowienia/archiwum")
    public String getArchiwalneZamowienia(Model m)
    {
        try{
            m.addAttribute("archiwalneZamowienia", foodOrderServiceImpl.znajdzStareZamowienia());
        }
        catch (NullPointerException n)
        {
            return "admin";
        }
        return "/admin/zamowienia/archiwum";
    }

    @GetMapping("/admin/zamowienia/potwierdz/{id}")
    public String potwierdzZamowienie(@PathVariable("id") long id, Model m)
    {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        try {
            m.addAttribute("zamowienie", foodOrderServiceImpl.findFoodOrderById(id));
            m.addAttribute("pageTittle","Zamowienie (ID: " +id +")");
            m.addAttribute("statusy", statusRepo.findAll());
            return "admin/zamowienia/potwierdz";
        } catch (NullPointerException e){
            return "admin/zamowienia";
        }
    }

    @PostMapping("/admin/zamowienia/potwierdz/save")
    public String saveZamowienie(@Valid @ModelAttribute("foodOrder")FoodOrder foodOrder)
    {
      foodOrderServiceImpl.save(foodOrder);
        return "redirect:/admin/zamowienia";
    }

    @GetMapping("/admin/zamowienia/archiwum/przywroc/{id}")
    public String przywrocZamowienie(@PathVariable("id") long id, Model m)
    {
        try {
            m.addAttribute("zamowienie", foodOrderServiceImpl.findFoodOrderById(id));
            m.addAttribute("pageTittle","Zamowienie (ID: " +id +")");
            return "admin/zamowienia/archiwum/przywroc";
        } catch (NullPointerException e){
            return "admin/zamowienia";
        }
    }

    @PostMapping("/admin/zamowienia/archiwum/przywroc/save")
    public String przywrocZamowienieZapisz(@Valid @ModelAttribute("foodOrder")FoodOrder foodOrder)
    {
        foodOrderServiceImpl.save(foodOrder);
        return "redirect:/admin/zamowienia";
    }
}

package pl.pracainzynierska2.praca.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.pracainzynierska2.praca.UserDetailsServiceImpl;
import pl.pracainzynierska2.praca.repo.AppUserRepo;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Entity
public class AppUser implements UserDetails {

    /*
    @Autowired
    private AppUserRepo appUserRepo;
*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public AppUser()
    {

    }

    public AppUser(/*long id,*/ String username, String imie, String nazwisko, String haslo, String role, String email, String telefon) {
        /*this.id = id;*/
        this.username = username;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.haslo = haslo;
        this.role = role;
        this.email = email;
        this.telefon = telefon;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
    public boolean getisEnabled()
    {
        return isEnabled;
    }
    public void setisEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    private String username;
    private String imie;
    private String nazwisko;
    private String haslo;
    private String role;
    private String email;
    private String telefon;
    private boolean isEnabled=false;


    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @OneToOne
    @JoinColumn(name="adres_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Address address;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return haslo;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public String toString() {
        return "User [login=" + username + ", imie=" + imie + ", nazwisko=" + nazwisko + ", rola=" + role + ", email=" + email + ", telefon=" + telefon + ", aktywny=" + isEnabled+ "]";
    }
/*
    public AppUser getCurrentlyLoogedInAppUser (Authentication authentication){
        if (authentication==null) return null;
        AppUser appUser = null;
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetailsServiceImpl){
            appUser=((UserDetailsServiceImpl)principal).get
        }
    }
    */
}

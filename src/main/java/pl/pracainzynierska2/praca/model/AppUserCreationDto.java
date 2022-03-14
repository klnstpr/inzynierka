package pl.pracainzynierska2.praca.model;
import java.util.ArrayList;
import java.util.List;

public class AppUserCreationDto {
    private List<AppUser> appUserList;

    public AppUserCreationDto() {
        this.appUserList = new ArrayList<>();
    }

    public AppUserCreationDto(List<AppUser> appUserList) {
        this.appUserList = appUserList;
    }

    public List<AppUser> getUserList() {
        return appUserList;
    }

    public void setAppUserList(List<AppUser> appUserList) {
        this.appUserList = appUserList;
    }

    public void addAppUserList(AppUser appUserList) {
        this.appUserList.add(appUserList);
    }
}

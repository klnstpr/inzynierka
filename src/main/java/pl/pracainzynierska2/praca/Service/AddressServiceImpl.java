package pl.pracainzynierska2.praca.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pracainzynierska2.praca.model.Address;
import pl.pracainzynierska2.praca.repo.AddressRepo;

@Service
public class AddressServiceImpl  {
    @Autowired
   private Address address;
    @Autowired
   private AddressRepo addressRepo;

public void zapiszAdres(Address address)
{
    addressRepo.save(address);
}
public Address znajdzPoUlicy(String ulica)
{
    return addressRepo.findByUlica(ulica);
}
public Address znajdzPoId(int id)
    {
        return addressRepo.findById(id);
    }
}

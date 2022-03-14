package pl.pracainzynierska2.praca.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pracainzynierska2.praca.model.Status;
import pl.pracainzynierska2.praca.repo.StatusRepo;

@Service
public class StatusServiceImpl {
    @Autowired
    private StatusRepo statusRepo;
    private Status status;

    public Status findById(int id)
    {
        return statusRepo.getById(id);
    }

}

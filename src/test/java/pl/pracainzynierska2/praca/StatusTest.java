package pl.pracainzynierska2.praca;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import pl.pracainzynierska2.praca.model.Status;
import pl.pracainzynierska2.praca.repo.StatusRepo;

@SpringBootTest
public class StatusTest {
    @Autowired private Status status;
    @Autowired private StatusRepo statusRepo;

    @Test
    @Transactional
    public void szukanieStatusuPoIdTest()
    {
        statusRepo.findById(1);
    }

    @Test
    @Transactional
    public void zapisywanieStatusuTest()
    {
        Status status2 = new Status();
        statusRepo.save(status2);
    }

    @Test
    @Transactional
    public void otrzymywanieStatusuPoId()
    {
        statusRepo.getById(1);
    }
}

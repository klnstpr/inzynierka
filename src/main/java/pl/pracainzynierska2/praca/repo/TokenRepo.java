package pl.pracainzynierska2.praca.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pracainzynierska2.praca.model.Token;
import com.sun.xml.bind.v2.model.core.ID;
import java.util.Optional;

@Repository
public interface TokenRepo extends JpaRepository<Token, Long> {
 Token findByValue(String value);
}

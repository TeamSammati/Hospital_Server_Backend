package site.sammati_hospital.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.sammati_hospital.entity.Token;


public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("""
      select t from Token t inner join Doctor u on t.doctor.doctorId = u.doctorId
      where u.doctorId= :doctorId and (t.expired = false or t.revoked = false)
      """)

    List<Token> findAllValidTokenByUser(Integer doctorId);
    Optional<Token> findByToken(String token);
}

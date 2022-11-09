package tutorial.mintic.security.repositories;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import tutorial.mintic.security.models.Role;
import tutorial.mintic.security.models.User;

import java.util.List;


public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByUsername(String username);

    List<User> findByRole(Role role);

    @Query("{'username': {'$regex': ?0, '$options': 'i'}}")
    List<User> findByMatchUsername(String term);
}

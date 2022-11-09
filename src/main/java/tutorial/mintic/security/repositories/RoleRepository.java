package tutorial.mintic.security.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import tutorial.mintic.security.models.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
    Role findByName(String name);
}

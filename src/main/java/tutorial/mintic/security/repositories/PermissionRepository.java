package tutorial.mintic.security.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import tutorial.mintic.security.models.Permission;

import java.util.List;

public interface PermissionRepository extends MongoRepository<Permission, String> {
    List<Permission> findByUrl(String url);

    List<Permission> findByMethod(String method);

    @Query("{'url': ?0, 'method': ?1}")
    Permission findPermission(String url, String method);
}

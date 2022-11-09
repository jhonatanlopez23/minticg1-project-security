package tutorial.mintic.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tutorial.mintic.security.models.Role;
import tutorial.mintic.security.models.User;
import tutorial.mintic.security.repositories.RoleRepository;
import tutorial.mintic.security.repositories.UserRepository;

import java.util.List;

@RestController
@RequestMapping("role")
public class RoleController {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public List<Role> getAll() {
        return this.roleRepository.findAll();
    }

    @PostMapping
    public Role create(@RequestBody Role data) {
        Role previousRole = this.roleRepository.findByName(data.getName());
        if (previousRole == null) {
            return this.roleRepository.save(data);
        } else {
            previousRole.setDescription(data.getDescription());
            return this.roleRepository.save(previousRole);
        }
    }

    @PutMapping("{id}")
    public Role update(@PathVariable String id, @RequestBody Role data) {
        Role currentRole = this.roleRepository.findById(id).orElse(null);
        if (currentRole == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El rol no existe");
        }
        currentRole.setName(data.getName());
        currentRole.setDescription(data.getDescription());
        return this.roleRepository.save(currentRole);
    }

    @GetMapping("{id}")
    public Role getById(@PathVariable String id) {
        return this.roleRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "El rol no existe")
        );
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        this.roleRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "El rol no existe")
        );
        this.roleRepository.deleteById(id);
    }


    @GetMapping("{id}/users")
    public List<User> getAllUsersByRole(@PathVariable String id) {
        Role role = this.roleRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        return this.userRepository.findByRole(role);
    }
}

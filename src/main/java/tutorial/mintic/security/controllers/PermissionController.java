package tutorial.mintic.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tutorial.mintic.security.models.Permission;
import tutorial.mintic.security.repositories.PermissionRepository;

import java.util.List;

@RestController
@RequestMapping("permissions")
public class PermissionController {
    @Autowired
    public PermissionRepository permissionRepository;

    @GetMapping("")
    public List<Permission> getAll() {
        return this.permissionRepository.findAll();
    }

    @PostMapping("")
    public Permission create(@RequestBody Permission data) {
        return this.permissionRepository.save(data);
    }

    @PutMapping("{id}")
    public Permission update(@PathVariable String id, @RequestBody Permission data) {
        Permission permission = this.permissionRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        permission.setUrl(data.getUrl());
        permission.setMethod(data.getMethod());
        return this.permissionRepository.save(permission);
    }

    @GetMapping("{id}")
    public Permission getById(@PathVariable String id) {
        return this.permissionRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        Permission permission = this.permissionRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        this.permissionRepository.delete(permission);
    }

    @GetMapping("url/{url}")
    public List<Permission> getAllByUrl(@PathVariable String url) {
        return this.permissionRepository.findByUrl("/" + url);
    }

    @GetMapping("method/{method}")
    public List<Permission> getAllByMethod(@PathVariable String method) {
        return this.permissionRepository.findByMethod(method.toUpperCase());
    }
}

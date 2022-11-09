package tutorial.mintic.security.controllers;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tutorial.mintic.security.models.Permission;
import tutorial.mintic.security.models.Role;
import tutorial.mintic.security.models.RolePermission;
import tutorial.mintic.security.repositories.PermissionRepository;
import tutorial.mintic.security.repositories.RolePermissionRepository;
import tutorial.mintic.security.repositories.RoleRepository;

import java.util.List;

@RestController
@RequestMapping("role-permission")
public class RolePermissionController {
    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @GetMapping("")
    public List<RolePermission> getAll() {
        return this.rolePermissionRepository.findAll();
    }

    @GetMapping("{id}")
    public RolePermission getById(@PathVariable String id) {
        return this.rolePermissionRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    @PostMapping("rol/{idRole}/permission/{idPermission}")
    public RolePermission create(@PathVariable String idRole, @PathVariable String idPermission) {
        Role role = this.roleRepository.findById(idRole).orElse(null);
        Permission permission = this.permissionRepository.findById(idPermission).orElse(null);
        if (role == null || permission == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        RolePermission exist = this.rolePermissionRepository.findByRoleAndPermission(role.get_id(), permission.get_id());
        if (exist != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else {
            RolePermission rolePermission = new RolePermission(role, permission);
            return this.rolePermissionRepository.save(rolePermission);
        }
    }

    @PutMapping("{id}/role/{idRole}/permission/{idPermission}")
    public RolePermission update(@PathVariable String id, @PathVariable String idRole, @PathVariable String idPermission) {
        RolePermission rolePermission = this.rolePermissionRepository.findById(id).orElse(null);
        Role role = this.roleRepository.findById(idRole).orElse(null);
        Permission permission = this.permissionRepository.findById(idPermission).orElse(null);

        if (rolePermission == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (role == null || permission == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        boolean exist = this.checkIfAlreadyExists(role, permission);
        if (exist) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else {
            rolePermission.setRole(role);
            rolePermission.setPermission(permission);
            return this.rolePermissionRepository.save(rolePermission);
        }
    }

    public boolean checkIfAlreadyExists(Role role, Permission permission) {
        /*
        Esta función tiene como objetivo verificar si ya existe la relacion entre el rol y el permiso dado
         */
        List<RolePermission> items = this.rolePermissionRepository.findByRole(role);
        for (int i = 0; i < items.size(); i++) {
            boolean exist = items.get(i).getPermission().get_id().equals(permission.get_id());
            if (exist) {
                return true;
            }
        }
        return false;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        RolePermission rolePermission = this.rolePermissionRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        this.rolePermissionRepository.deleteById(id);
    }

    @GetMapping("role/{idRole}")
    public List<RolePermission> getAllByRole(@PathVariable String idRole) {
        Role role = this.roleRepository.findById(idRole).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        return this.rolePermissionRepository.findByRole(role);
    }

    @GetMapping("permission/{idPermission}")
    public List<RolePermission> getAllByPermission(@PathVariable String idPermission) {
        Permission permission = this.permissionRepository.findById(idPermission).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        return this.rolePermissionRepository.findByPermission(permission);
    }

    @GetMapping("role/{idRole}/permission/{idPermission}")
    public void getByRoleAndPermission(@PathVariable String idRole, @PathVariable String idPermission) {
        Role role = this.roleRepository.findById(idRole).orElse(null);
        Permission permission = this.permissionRepository.findById(idPermission).orElse(null);
        if (role == null || permission == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        boolean exist = this.checkIfAlreadyExists(role, permission);
        if (!exist) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("validate/role/{idRole}")
    public RolePermission validatePermission(@PathVariable String idRole, @RequestBody InfoPermission info) {
        Permission permission = this.permissionRepository.findPermission(info.getUrl(), info.getMethod());
        Role role = this.roleRepository.findById(idRole).orElse(null);
        if (role == null || permission == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        RolePermission rolePermission = this.rolePermissionRepository.findByRoleAndPermission(role.get_id(), permission.get_id());
        if (rolePermission == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return rolePermission;
    }
}

@Data
class InfoPermission {
    private String url;
    private String method;

    public InfoPermission(String url, String method) {
        this.url = url;
        this.method = method;
    }
}
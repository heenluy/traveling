package dev.henriqueluiz.travelling.service;

import dev.henriqueluiz.travelling.model.AppRole;
import dev.henriqueluiz.travelling.model.AppUser;

import java.util.List;

public interface UserService {
    AppUser saveUser(AppUser user);
    AppUser getUserByEmail(String email);
    AppRole saveRole(AppRole role);
    List<AppRole> getAllRoles();
    void addRolesToUser(String roleName, String email);
}

package dev.henriqueluiz.travelling.service;

import dev.henriqueluiz.travelling.model.AppRole;
import dev.henriqueluiz.travelling.model.AppUser;

public interface UserService {
    AppUser saveUser(AppUser user);
    AppUser getUserByEmail(String email);
    AppRole saveRole(AppRole role);
    void addRolesToUser(String roleName, String email);
}

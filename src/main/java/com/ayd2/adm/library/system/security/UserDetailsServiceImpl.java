package com.ayd2.adm.library.system.security;

import com.ayd2.adm.library.system.exception.LibException;
import com.ayd2.adm.library.system.model.AdmRole;
import com.ayd2.adm.library.system.repository.AdmUserRepository;
import com.ayd2.adm.library.system.service.AdmRoleService;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AdmUserRepository userRepository;
    private final AdmRoleService roleService;

    public UserDetailsServiceImpl(
            AdmUserRepository userRepository,
            AdmRoleService roleService
    ) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userOpt = userRepository.findByEmail(username);
        if (userOpt.isEmpty()) throw new LibException("user_by_email_not_found").status(HttpStatus.NOT_FOUND);

        var user = userOpt.get();
        var roles = roleService.findRolesByUserId(user.getUserId());
        var authorities = roles
                .stream()
                .map(AdmRole::getRoleId)
                .map(String::valueOf)
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new User(user.getEmail(), user.getPassword(), authorities);
    }
}

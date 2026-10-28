package bootcamp.hibernate_practical.service;

import bootcamp.hibernate_practical.entity.AppUser;
import bootcamp.hibernate_practical.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        AppUser appUser = userRepository
                .findUserByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User " + username + " not found"));

        return User
                .withUsername(appUser.getUsername())
                .password(appUser.getPassword())
                .roles(appUser.getRole())
                .disabled(!appUser.isEnabled())
                .build();
    }
}

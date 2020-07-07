package ru.otus.spring.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.domain.User;
import ru.otus.spring.repositories.UserRepository;

import java.util.Collection;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(name)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден по переданному имени"));
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
                getAuthorities(user));
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return AuthorityUtils.createAuthorityList("ROLE_USER");
    }
}

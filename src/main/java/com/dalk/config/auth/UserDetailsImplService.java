package com.dalk.config.auth;


import com.dalk.domain.User;
import com.dalk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsImplService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User findUser = userRepository.findByUsername(userName).orElseThrow(
                () -> new UsernameNotFoundException("가입되지 않은 이메일입니다.")
        );

        return new UserDetailsImpl(findUser);
    }
}
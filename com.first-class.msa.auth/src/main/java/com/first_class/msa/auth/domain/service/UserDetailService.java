package com.first_class.msa.auth.domain.service;

import com.first_class.msa.auth.domain.model.User;
import com.first_class.msa.auth.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public User loadUserByUsername(String account){
        return userRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException((account)));
    }
}

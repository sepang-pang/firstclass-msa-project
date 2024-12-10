package com.first_class.msa.auth.domain.service;

import com.first_class.msa.auth.config.jwt.JwtUtil;
import com.first_class.msa.auth.domain.dto.ReqLoginDTO;
import com.first_class.msa.auth.domain.model.User;
import com.first_class.msa.auth.domain.dto.ReqUserPostDTO;
import com.first_class.msa.auth.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;

    public String save(ReqUserPostDTO dto) {
        String encodedPassword = bCryptPasswordEncoder.encode(dto.getPassword());
        User user = ReqUserPostDTO.toEntity(dto);
        user.setPassword(encodedPassword);

        return userRepository.save(user).getAccount();
    }

    
    public String signIn(ReqLoginDTO dto) {
        // 사용자 정보를 찾기
        User user = userRepository.findByAccount(dto.getAccount())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        System.out.println("Entered password: " + dto.getPassword());
        System.out.println("Stored password: " + user.getPassword());

        // 비밀번호 검증
        if (!bCryptPasswordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        // JWT 토큰 생성
        return jwtUtil.generateToken(user.getAccount());
    }

    public User findById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}
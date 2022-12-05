package com.mysite.sbb.user;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    private SiteUserDto of(SiteUser siteUser) {
        return this.modelMapper.map(siteUser, SiteUserDto.class);
    }

    private SiteUser of(SiteUserDto siteUserDto) {
        return this.modelMapper.map(siteUserDto, SiteUser.class);
    }

    public SiteUserDto create(String username, String email, String password, String cellphone) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setCellphone(cellphone);
        this.userRepository.save(user);
        return of(user);
    }

    public SiteUserDto getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        if (siteUser.isPresent()) {
            return of(siteUser.get());
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }

    public SiteUserDto getEmail(String email) {
        Optional<SiteUser> siteUser = this.userRepository.findByemail(email);
        if (siteUser.isPresent()) {
            return of(siteUser.get());
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }

    public SiteUserDto getCellphone(String cellphone) {
        Optional<SiteUser> siteUser = this.userRepository.findBycellphone(cellphone);
        if (siteUser.isPresent()) {
            return of(siteUser.get());
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }

    public void delete(SiteUserDto siteUserDto) {
        this.userRepository.delete(of(siteUserDto));
    }

    public boolean checkPassword(String username, String checkPassword) {
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        String realPassword;
        if (siteUser.isPresent()) {
            realPassword = of(siteUser.get()).getPassword();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
        return passwordEncoder.matches(checkPassword, realPassword);
    }
}

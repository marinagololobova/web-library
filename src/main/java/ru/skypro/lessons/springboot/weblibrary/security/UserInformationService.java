package ru.skypro.lessons.springboot.weblibrary.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.lessons.springboot.weblibrary.dto.UserDTO;
import ru.skypro.lessons.springboot.weblibrary.repository.UserRepository;
import ru.skypro.lessons.springboot.weblibrary.service.UserMapper;

@Service
public class UserInformationService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserInformation userInformation;

    public UserInformationService(UserRepository userRepository,
                                  UserMapper userMapper,
                                  UserInformation userInformation) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userInformation = userInformation;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDTO = userRepository.findByLogin(username)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new UsernameNotFoundException("Подьзователь %s не найден!".formatted(username)));
        userInformation.setUserDTO(userDTO);
        return userInformation;
    }
}

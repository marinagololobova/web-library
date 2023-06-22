package ru.skypro.lessons.springboot.weblibrary.service;

import org.springframework.stereotype.Component;
import ru.skypro.lessons.springboot.weblibrary.dto.UserDTO;
import ru.skypro.lessons.springboot.weblibrary.entity.User;

@Component
public class UserMapper {

    public UserDTO toDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setLogin(user.getLogin());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        return userDTO;
    }
}

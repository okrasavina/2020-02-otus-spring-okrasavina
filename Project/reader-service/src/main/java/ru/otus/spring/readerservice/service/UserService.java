package ru.otus.spring.readerservice.service;

import ru.otus.spring.readerservice.dto.UserDTO;

public interface UserService {
    UserDTO getUserByDetails(UserDTO userDTO);

    UserDTO registerNewUser(UserDTO userDTO);
}

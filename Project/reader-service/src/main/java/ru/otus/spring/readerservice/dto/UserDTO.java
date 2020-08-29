package ru.otus.spring.readerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.readerservice.domain.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    String username;
    String password;
    String email;

    public static UserDTO toDTO(User user) {
        return new UserDTO(user.getUsername(), user.getPassword(),
                user.getEmail());
    }
}

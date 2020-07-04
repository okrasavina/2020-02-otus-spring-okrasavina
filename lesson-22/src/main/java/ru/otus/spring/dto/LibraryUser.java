package ru.otus.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.domain.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryUser {
    private String userName;
    private String email;
    private String password;

    public static LibraryUser toDto(User user) {
        LibraryUser libraryUser = new LibraryUser();
        libraryUser.setUserName(user.getUserName());
        libraryUser.setEmail(user.getEmail());
        return libraryUser;
    }
}

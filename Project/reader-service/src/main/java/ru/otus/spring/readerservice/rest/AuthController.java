package ru.otus.spring.readerservice.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.readerservice.dto.UserDTO;
import ru.otus.spring.readerservice.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;

    @GetMapping("/login")
    public ResponseEntity<UserDTO> loginByUserDetails(@PathVariable UserDTO userDTO) {
        log.info("get user {}", userDTO);
        return ResponseEntity.ok(userService.getUserByDetails(userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
        log.info("post user {}", userDTO);
        return ResponseEntity.ok(userService.registerNewUser(userDTO));
    }
}

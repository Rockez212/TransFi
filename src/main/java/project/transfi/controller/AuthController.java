package project.transfi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.transfi.command.SignInCommand;
import project.transfi.command.SignUpCommand;
import project.transfi.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpCommand command) {
        authService.signUp(command);
        return ResponseEntity.ok("Sign up successful");
    }

    @PostMapping("sign-in")
    public ResponseEntity<String> signIn(@RequestBody @Valid SignInCommand command) {
        authService.signIn(command);
        return ResponseEntity.ok("Sign in successful");
    }
}

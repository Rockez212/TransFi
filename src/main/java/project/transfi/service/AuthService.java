package project.transfi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.transfi.command.SignInCommand;
import project.transfi.command.SignUpCommand;
import project.transfi.dto.TokenResponseDto;
import project.transfi.entity.User;
import project.transfi.exception.InvalidPasswordException;
import project.transfi.exception.UserAlreadyExistsException;
import project.transfi.exception.UserNotFoundException;
import project.transfi.repository.UserRepository;
import project.transfi.utill.JwtUtill;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtill jwtUtill;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void signUp(SignUpCommand command) {
        checkIfUsernameExistsOrEmail(command.getUsername(), command.getEmail());
        User newUser = new User(command.getUsername(), command.getEmail(), passwordEncoder.encode(command.getPassword()));
        userRepository.save(newUser);
    }


    public TokenResponseDto signIn(SignInCommand command) {
        UsernamePasswordAuthenticationToken authInPutToken = new UsernamePasswordAuthenticationToken
                (command.getUsername(), command.getPassword());
        try {
            authenticationManager.authenticate(authInPutToken);
        } catch (BadCredentialsException e) {

            throw new InvalidPasswordException("Invalid credentials");
        }
        User currentUser = userRepository.findByUsername(command.getUsername()).orElseThrow(() -> new UsernameNotFoundException(command.getUsername()));
        userRepository.save(currentUser);
        String accessToken = jwtUtill.generateAccessToken(command.getUsername());

        return new TokenResponseDto(accessToken);
    }


    private void checkIfUsernameExistsOrEmail(String username, String email) {
        if (userRepository.existsByUsernameOrEmail(username, email)) {
            throw new UserAlreadyExistsException("Username already exists");
        }
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        throw new UserNotFoundException("User not found");
    }

}

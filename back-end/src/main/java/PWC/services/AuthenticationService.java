package PWC.services;


import PWC.entities.Role;
import PWC.entities.User;
import PWC.repository.UserRepository;
import PWC.requests.AuthenticationRequest;
import PWC.requests.AuthenticationResponse;
import PWC.requests.RegisterRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity register(RegisterRequest request) {
        User user1 = repository.findByEmail(request.getEmail());
        if (user1 != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(AuthenticationResponse.builder()
                            .error("Email already exists")
                            .build());
        }


        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf(String.valueOf(request.getRole())))
                .tel(request.getTel())
                .isActive(request.isActive())
                .informations(request.getInformations())
                .image(request.getImage())
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        jwtService.generateRefreshToken(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(AuthenticationResponse.builder()
                        .accessToken(jwtToken)
                        .build());

    }

    public ResponseEntity authenticate(AuthenticationRequest request) {
        AuthenticationResponse response;
        User user = repository.findByEmail(request.getEmail());
        if (user == null) {
            // Return error if email doesn't exist
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(AuthenticationResponse.builder()
                            .error("Email not found")
                            .build());
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            var jwtToken = jwtService.generateToken(user);
            jwtService.extractClaim(jwtToken, claims -> {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    return objectMapper.writeValueAsString(claims);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    return null;
                }
            });
            response = AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .build();
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(AuthenticationResponse.builder()
                            .error("Incorrect Password")
                            .build());
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(response);
    }







    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            User user = this.repository.findByEmail(userEmail);

            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);

                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
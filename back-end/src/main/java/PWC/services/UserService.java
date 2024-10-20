package PWC.services;


import PWC.dto.UserDTO;
import PWC.entities.User;
import PWC.repository.UserRepository;
import PWC.requests.ChangePasswordRequest;
import PWC.requests.ChangePasswordResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    @NonNull
    HttpServletRequest request;
    public ChangePasswordResponse changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        ChangePasswordResponse response = new ChangePasswordResponse();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            response.setMessage("Wrong password");
            return response;
        }

        // check if the two new passwords are the same


        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);

        response.setMessage("Password changed successfully");
        return response;
    }

    public UserDTO getUserDataByToken() {
        try {
            final String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                final String jwt = authHeader.substring(7);
                final String userEmail = jwtService.extractUsername(jwt);

                User user = repository.findByEmail(userEmail);
                if (user != null) {
                    return UserDTO.fromEntity(user);
                } else {
                    throw new RuntimeException("User not found for email: " + userEmail);
                }
            } else {
                throw new IllegalArgumentException("Invalid or missing Authorization header");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions appropriately (log or throw)
            throw new RuntimeException("Error fetching user data: " + e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        return repository.findAll(); // Example using JPA repository
    }
    @Transactional
    public String updateUserImage(Long userId, byte[] imageBytes) {
        Optional<User> optionalUser = repository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setImage(imageBytes); // Assuming User entity has setImage method for imageBytes
            repository.save(user); // Save the updated user entity
            return "User image updated successfully";
        } else {
            return "User not found with id: " + userId;
        }
    }

}
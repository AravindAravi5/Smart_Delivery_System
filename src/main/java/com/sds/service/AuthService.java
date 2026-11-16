package com.sds.service;

import com.sds.dao.UserDAO;
import com.sds.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public boolean register(String username, String rawPassword, String email, User.Role role) {
        if (userDAO.findByUsername(username).isPresent()) {
            System.out.println("Username already exists!");
            return false;
        }

        String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(hashedPassword);
        newUser.setEmail(email);
        newUser.setRole(role);

        return userDAO.save(newUser);
    }

    public Optional<User> login(String username, String rawPassword) {
        Optional<User> userOpt = userDAO.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            try {
                // If it's a valid BCrypt hash, this will work
                if (user.getPassword().startsWith("$2a$") && BCrypt.checkpw(rawPassword, user.getPassword())) {
                    return Optional.of(user);
                } 
                // Fallback for legacy plain-text passwords inserted directly via SQL
                else if (user.getPassword().equals(rawPassword)) {
                    return Optional.of(user);
                } else {
                    System.out.println("Invalid credentials.");
                }
            } catch (Exception e) {
                if (user.getPassword().equals(rawPassword)) {
                    return Optional.of(user);
                } else {
                    System.out.println("Invalid credentials.");
                }
            }
        } else {
            System.out.println("User not found.");
        }
        return Optional.empty();
    }
}

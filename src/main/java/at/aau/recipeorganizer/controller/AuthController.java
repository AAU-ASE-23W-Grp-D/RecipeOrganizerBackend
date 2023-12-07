package at.aau.recipeorganizer.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@SuppressWarnings("unused")
public class AuthController {
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        // TODO: Perform authentication logic here
        if (loginRequest.getUsername().equals("admin") && loginRequest.getPassword().equals("12345678")) {
            return "success";
        } else {
            return "failure";
        }
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}

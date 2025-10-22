package com.riva.oauth.controller;

import com.riva.oauth.model.User;
import com.riva.oauth.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    private final UserRepository userRepository;

    public PageController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home() {
        return "index"; // make sure index.html exists in src/main/resources/templates/
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal == null) return "redirect:/";

        // Safe attribute extraction
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        String avatarUrl = principal.getAttribute("picture");

        // GitHub fallback
        if (email == null) email = principal.getAttribute("login") + "@github.com";
        if (name == null) name = principal.getAttribute("login");
        if (avatarUrl == null) avatarUrl = principal.getAttribute("avatar_url");

        // Default fallback
        if (name == null) name = "Cowboy";
        if (avatarUrl == null) avatarUrl = "";

        // Find or create user safely
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setDisplayName(name);
            user.setAvatarUrl(avatarUrl);
            userRepository.save(user);
        }

        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal OAuth2User principal,
                                @RequestParam String displayName,
                                @RequestParam String bio) {
        String email = principal.getAttribute("email");
        if (email == null) email = principal.getAttribute("login") + "@github.com";

        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            user.setDisplayName(displayName);
            user.setBio(bio);
            userRepository.save(user);
        }
        return "redirect:/profile";
    }
}

package com.riva.oauth.service;

import com.riva.oauth.model.AuthProvider;
import com.riva.oauth.model.ProviderType;
import com.riva.oauth.model.User;
import com.riva.oauth.repository.AuthProviderRepository;
import com.riva.oauth.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final AuthProviderRepository authProviderRepository;

    public CustomOAuth2UserService(UserRepository userRepository, AuthProviderRepository authProviderRepository) {
        this.userRepository = userRepository;
        this.authProviderRepository = authProviderRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String email = null;
        String name;
        String avatarUrl;

        if ("google".equalsIgnoreCase(registrationId)) {
            email = oauth2User.getAttribute("email");
            name = oauth2User.getAttribute("name");
            avatarUrl = oauth2User.getAttribute("picture");
        } else if ("github".equalsIgnoreCase(registrationId)) {
            email = oauth2User.getAttribute("email");
            name = oauth2User.getAttribute("login");
            avatarUrl = oauth2User.getAttribute("avatar_url");
        } else {
            avatarUrl = null;
            name = null;
        }

        if (email == null) email = name + "@" + registrationId + ".com";

        String finalEmail = email;
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(finalEmail);
            newUser.setDisplayName(name);
            newUser.setAvatarUrl(avatarUrl);
            return userRepository.save(newUser);
        });

        String finalEmail1 = email;
        authProviderRepository.findByProviderUserId(oauth2User.getName())
                .orElseGet(() -> {
                    AuthProvider authProvider = new AuthProvider();
                    authProvider.setUser(user);
                    authProvider.setProvider(ProviderType.valueOf(registrationId.toUpperCase()));
                    authProvider.setProviderUserId(oauth2User.getName());
                    authProvider.setProviderEmail(finalEmail1);
                    return authProviderRepository.save(authProvider);
                });

        return oauth2User;
    }
}

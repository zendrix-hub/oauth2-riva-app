package com.riva.oauth.repository;

import com.riva.oauth.model.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AuthProviderRepository extends JpaRepository<AuthProvider, Long> {
    Optional<AuthProvider> findByProviderUserId(String providerUserId);
}

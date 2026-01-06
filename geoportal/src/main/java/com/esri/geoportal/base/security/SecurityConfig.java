package com.esri.geoportal.base.security;

import java.util.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration

public class SecurityConfig {

        private final KeycloakConfig keycloakConfig;

        public SecurityConfig(KeycloakConfig keycloakConfig) {
                this.keycloakConfig = keycloakConfig;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                // Use AntPathRequestMatcher to avoid MVC dependency
                                .authorizeHttpRequests(authorize -> authorize.requestMatchers(
                                                new AntPathRequestMatcher("/rest/**"),
                                                new AntPathRequestMatcher("/catalog/**"))
                                                .authenticated().anyRequest().permitAll())
                                .oauth2Login(oauth2 -> oauth2
                                                .loginPage("/oauth2/authorization/keycloak")
                                                .userInfoEndpoint(userInfo -> userInfo
                                                                .userAuthoritiesMapper(
                                                                                this::mapAuthorities)))
                                .csrf(csrf -> csrf.disable()); // optional

                return http.build();
        }

        /**
         * Map Keycloak realm and client roles to Spring authorities
         */
        private Set<GrantedAuthority> mapAuthorities(
                        Collection<? extends GrantedAuthority> authorities) {
                Set<GrantedAuthority> mapped = new HashSet<>();
                if (authorities == null)
                        return mapped;

                for (GrantedAuthority authority : authorities) {
                        if (authority instanceof OAuth2UserAuthority) {
                                OAuth2UserAuthority oauth = (OAuth2UserAuthority) authority;
                                Map<String, Object> claims = oauth.getAttributes();

                                // Realm roles
                                Map<String, Object> realmAccess =
                                                (Map<String, Object>) claims.get("realm_access");
                                if (realmAccess != null && realmAccess.get("roles") != null) {
                                        ((List<String>) realmAccess.get("roles"))
                                                        .forEach(role -> mapped.add(
                                                                        new SimpleGrantedAuthority(
                                                                                        keycloakConfig.getRolePrefix()
                                                                                                        + role)));
                                }

                                // Client roles
                                Map<String, Object> resourceAccess =
                                                (Map<String, Object>) claims.get("resource_access");
                                if (resourceAccess != null && resourceAccess
                                                .containsKey(keycloakConfig.getClientId())) {
                                        Map<String, Object> clientRoles =
                                                        (Map<String, Object>) resourceAccess.get(
                                                                        keycloakConfig.getClientId());
                                        List<String> roles =
                                                        (List<String>) clientRoles.get("roles");
                                        if (roles != null) {
                                                roles.forEach(role -> mapped
                                                                .add(new SimpleGrantedAuthority(
                                                                                keycloakConfig.getRolePrefix()
                                                                                                + role)));
                                        }
                                }
                        }
                }
                return mapped;
        }
}

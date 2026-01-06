package com.esri.geoportal.base.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.*;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@Configuration
public class OAuth2ClientConfig {

    @Value("${keycloakBaseUrl}")
    private String keycloakBaseUrl; // e.g. https://dev2.crrel.mil/auth

    @Value("${keycloakRealm}")
    private String keycloakRealm; // e.g. cwbi

    @Value("${clientId}")
    private String clientId; // e.g. datacatalog

    @Value("${keycloakAuthUrl}")
    private String authorizationUri; // full auth endpoint if needed

    @Value("${keycloakTokenUrl}")
    private String tokenUri; // full token endpoint if needed

    @Value("${redirectUrl}")
    private String redirectUrl; // full redirect callback

    @Value("${KEYCLOAK_SCOPES:openid,profile,email}")
    private String[] scopes;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {

        String userInfoUri = String.format("%s/realms/%s/protocol/openid-connect/userinfo",
                keycloakBaseUrl, keycloakRealm);

        String logoutUri = String.format("%s/realms/%s/protocol/openid-connect/logout",
                keycloakBaseUrl, keycloakRealm);

        ClientRegistration keycloak =
                ClientRegistration.withRegistrationId("keycloak").clientId(clientId)
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .redirectUri(redirectUrl) // Use the environment-provided callback
                        .scope(scopes).authorizationUri(authorizationUri).tokenUri(tokenUri)
                        .userInfoUri(userInfoUri).userNameAttributeName("preferred_username")
                        .clientName("Keycloak").build();

        return new InMemoryClientRegistrationRepository(keycloak);
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService(
            ClientRegistrationRepository clientRegistrationRepository) {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
    }
}

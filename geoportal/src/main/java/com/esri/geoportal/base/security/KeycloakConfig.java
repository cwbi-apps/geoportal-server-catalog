package com.esri.geoportal.base.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Configuration bean for Keycloak OAuth2 integration.
 */
@Component("keycloakConfig")
public class KeycloakConfig {

    /** Base URL of the Keycloak server, e.g. https://keycloak.example.com/auth */
    @Value("${keycloakAuthUrl:https://keycloak.example.com/auth}")
    private String keycloakAuthUrl;

    /** Keycloak realm name */
    @Value("${keycloakRealm:geoportal}")
    private String realm;

    /** Client ID registered in Keycloak */
    @Value("${clientId:geoportal-client}")
    private String clientId;

    /** Prefix for roles, e.g., "ROLE_" */
    @Value("${KEYCLOAK_ROLE_PREFIX:ROLE_}")
    private String rolePrefix;

    /** Redirect URI for OAuth2 login */
    @Value("${KEYCLOAK_REDIRECT_URI:/geoportal/login/oauth2/code/keycloak}")
    private String redirectUri;

    /** Accessor methods */

    public String getKeycloakAuthUrl() {
        return String.format("%s/realms/%s/protocol/openid-connect/auth", keycloakAuthUrl, realm);
    }

    public String getRealm() {
        return realm;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRolePrefix() {
        return rolePrefix;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

}

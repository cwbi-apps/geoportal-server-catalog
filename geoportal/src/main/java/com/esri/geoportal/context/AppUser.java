package com.esri.geoportal.context;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.SecurityContext;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import com.esri.geoportal.base.security.Group;

/**
 * Represents the user associated with a request. Supports modern Spring Security OAuth2 principals.
 */
public class AppUser {

  private List<Group> groups;
  private boolean isAdmin;
  private boolean isAnonymous;
  private boolean isPublisher;
  private String username;

  public AppUser() {}

  public AppUser(HttpServletRequest request, SecurityContext sc) {
    init(request);
  }

  public AppUser(String username, boolean isAdmin, boolean isPublisher) {
    init(username, isAdmin, isPublisher);
  }

  public List<Group> getGroups() {
    return groups;
  }

  public String getUsername() {
    return username;
  }

  public boolean isAdmin() {
    return isAdmin;
  }

  public boolean isAnonymous() {
    return isAnonymous;
  }

  public boolean isPublisher() {
    return isPublisher;
  }

  private void init(HttpServletRequest request) {
    init(null, false, false);
    if (request == null)
      return;

    Principal p = request.getUserPrincipal();
    if (p == null)
      return;

    groups = new ArrayList<>();
    username = p.getName();
    if (username != null && !username.isEmpty()) {
      isAnonymous = false;
      isAdmin = request.isUserInRole("ADMIN");
      isPublisher = request.isUserInRole("PUBLISHER");
    } else {
      isAnonymous = true;
    }

    Collection<? extends GrantedAuthority> authorities = null;

    // Modern Spring Security principal handling
    if (p instanceof Authentication) {
      Authentication auth = (Authentication) p;
      if (auth.isAuthenticated()) {
        authorities = auth.getAuthorities();
      }
    }

    if (authorities != null) {
      for (GrantedAuthority authority : authorities) {
        if (authority != null) {
          String name = authority.getAuthority();
          if (name != null) {
            if (name.startsWith("ROLE_"))
              name = name.substring(5);
            // Only add non-standard roles as groups
            if (!Arrays.asList("ADMIN", "PUBLISHER", "USER").contains(name.toUpperCase())) {
              groups.add(new Group(name));
            }
          }
        }
      }
    }

    // Include GeoportalContext-defined groups
    GeoportalContext gc = GeoportalContext.getInstance();
    HashMap<String, ArrayList<Group>> userGroupMap = gc.getUserGroupMap();
    if (userGroupMap.containsKey(username)) {
      groups.addAll(userGroupMap.get(username));
    }
  }

  private void init(String username, boolean isAdmin, boolean isPublisher) {
    this.username = username;
    if (this.username != null && !this.username.isEmpty()) {
      this.isAnonymous = false;
      this.isAdmin = isAdmin;
      this.isPublisher = isPublisher;
    } else {
      this.isAnonymous = true;
      this.isAdmin = false;
      this.isPublisher = false;
    }
  }

}

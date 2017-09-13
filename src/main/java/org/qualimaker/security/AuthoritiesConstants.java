package org.qualimaker.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
    public static final String VERIFICATEUR = "ROLE_VERIFICATEUR";
    public static final String REDACTEUR = "ROLE_REDACTEUR";
    public static final String APPROBATEUR = "ROLE_APPROBATEUR";
    public static final String SUPERVISEUR ="ROLE_SUPERVISEUR";



    private AuthoritiesConstants() {
    }
}

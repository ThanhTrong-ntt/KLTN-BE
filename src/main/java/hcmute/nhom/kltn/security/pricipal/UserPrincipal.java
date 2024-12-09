package hcmute.nhom.kltn.security.pricipal;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import hcmute.nhom.kltn.model.User;

/**
 * Class UserPrincipal.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@NoArgsConstructor
public class UserPrincipal implements UserDetails, OAuth2User {
    private String id;
    private String email;
    private String password;
    private Boolean isActive;

    private Collection<? extends GrantedAuthority> authorities;

    private OAuth2User oAuth2User;

    private Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Constructor.
     *
     * @param id
     * @param email
     * @param password
     * @param authorities
     */
    public UserPrincipal(String id, String email, String password, Collection<? extends GrantedAuthority> authorities,
                         Boolean isActive, OAuth2User oAuth2User) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.isActive = isActive;
        this.oAuth2User = oAuth2User;
    }

    /**
    * Create UserPrincipal.
    *
    * @param user User
     * @return UserPrincipal
     */
    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getRoleName().toString())
        ).collect(Collectors.toList());
        UserPrincipal userPrincipal = new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities,
                user.getActiveFlag(),
                null
        );
        return userPrincipal;
    }

    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    // New method to create UserPrincipal from OAuth2User
    public static UserPrincipal createFromOAuth2User(OAuth2User oauth2User) {
        List<GrantedAuthority> authorities = oauth2User.getAuthorities().stream().collect(Collectors.toList());
        String email = oauth2User.getAttribute("email"); // Assuming the OAuth2 provider returns an "email" attribute

        return new UserPrincipal(
                null, // No ID for OAuth2 users initially
                email,
                null, // Password is not used for OAuth2
                authorities,
                true, // Assuming the account is active
                oauth2User // Store the OAuth2User for future use
        );
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    @Override
    public String getName() {
        return email;
    }
}

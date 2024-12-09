package hcmute.nhom.kltn.security.oauth2.dto;

import java.util.Map;

/**
 * Class OAuth2GithubUser.
 *
 * @author: ThanhTrong
 **/
public class OAuth2GithubUser extends OAuth2UserDetail {
    public OAuth2GithubUser(Map<String, Object> attributes) {
        super(attributes);
    }
    @Override
    public String getEmail() {
        return attributes.get("login").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }

    @Override
    public String getAvatarUrl() {
        return attributes.get("avatar_url").toString();
    }
}

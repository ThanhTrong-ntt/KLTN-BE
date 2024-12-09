package hcmute.nhom.kltn.security.oauth2.dto;

import java.util.Map;

/**
 * Class OAuth2GoogleUser.
 *
 * @author: ThanhTrong
 **/
public class OAuth2GoogleUser extends OAuth2UserDetail {
    public OAuth2GoogleUser(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }

    @Override
    public String getAvatarUrl() {
        return attributes.get("picture").toString();
    }
}

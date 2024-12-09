package hcmute.nhom.kltn.security.oauth2.dto;

import java.util.Map;

/**
 * Class OAuth2User.
 *
 * @author: ThanhTrong
 **/
public abstract class OAuth2UserDetail {
    protected Map<String, Object> attributes;

    protected OAuth2UserDetail(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getEmail();

    public abstract String getName();

    public abstract String getAvatarUrl();
}

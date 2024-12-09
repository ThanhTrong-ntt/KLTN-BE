package hcmute.nhom.kltn.security.oauth2;

import java.util.Map;
import org.springframework.http.HttpStatus;
import hcmute.nhom.kltn.exception.BaseException;
import hcmute.nhom.kltn.security.oauth2.dto.OAuth2FacebookUser;
import hcmute.nhom.kltn.security.oauth2.dto.OAuth2GithubUser;
import hcmute.nhom.kltn.security.oauth2.dto.OAuth2GoogleUser;
import hcmute.nhom.kltn.security.oauth2.dto.OAuth2UserDetail;
import hcmute.nhom.kltn.security.oauth2.dto.RegistrationId;

/**
 * Class OAuth2UserDetailFactory.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
public class OAuth2UserDetailFactory {
    public static OAuth2UserDetail getOAuth2UserDetail(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(RegistrationId.GOOGLE.name())) {
            return new OAuth2GoogleUser(attributes);
        } else if (registrationId.equalsIgnoreCase(RegistrationId.FACEBOOK.name())) {
            return new OAuth2FacebookUser(attributes);
        } else if (registrationId.equalsIgnoreCase(RegistrationId.GITHUB.name())) {
            return new OAuth2GithubUser(attributes);
        } else {
            throw new BaseException(HttpStatus.BAD_REQUEST.value(), "Sorry! Login with " + registrationId
                    + " is not supported yet.");
        }
    }
}

package hcmute.nhom.kltn.security.pricipal;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import hcmute.nhom.kltn.model.User;
import hcmute.nhom.kltn.repository.UserRepository;

/**
 * Class CustomUserDetailService.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(CustomUserDetailService.class);
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByEmail(username);
        logger.info("UserDetailService start!");
        User user = userRepository.findByEmail(username);
        if (Objects.nonNull(user)) {
            return UserPrincipal.create(user);
        }
       throw new UsernameNotFoundException("User not found with email: " + username);

    }
}

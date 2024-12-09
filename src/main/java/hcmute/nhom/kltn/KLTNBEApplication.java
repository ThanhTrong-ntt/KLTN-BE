// file: src/main/hcmute/nhom/kltn

package hcmute.nhom.kltn;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import hcmute.nhom.kltn.config.AuditorAwareImpl;
import hcmute.nhom.kltn.dto.ImageDTO;
import hcmute.nhom.kltn.dto.RoleDTO;
import hcmute.nhom.kltn.dto.UserDTO;
import hcmute.nhom.kltn.dto.UserProfileDTO;
import hcmute.nhom.kltn.enums.RoleName;
import hcmute.nhom.kltn.service.ImageService;
import hcmute.nhom.kltn.service.RoleService;
import hcmute.nhom.kltn.service.UserProfileService;
import hcmute.nhom.kltn.service.UserService;
import hcmute.nhom.kltn.util.Constants;

/**
 * Class KLTNBEApplication.<br>
 */
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class KLTNBEApplication {

    public static void main(String[] args) {
        SpringApplication.run(KLTNBEApplication.class, args);
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }

    @Bean
    @Transactional
    CommandLineRunner runner(
            UserService userService, PasswordEncoder passwordEncoder, RoleService roleService,
            UserProfileService userProfileService, ImageService mediaFileService) {
        return args -> {
            ImageDTO mediaFile = mediaFileService.findByFileName("default-avatar.png");
            if (Objects.isNull(mediaFile)) {
                String fileName = "default-avatar.png";
                mediaFile = new ImageDTO();
                mediaFile.setFileName(fileName);
                mediaFile.setFileType(fileName.substring(fileName.lastIndexOf(".")));
                mediaFile.setUrl(Constants.DEFAULT_AVATAR);
                mediaFile.setProduct(null);
                mediaFile = mediaFileService.save(mediaFile);
            }

            RoleDTO roleUser = roleService.findByRoleName(RoleName.USER.name());
            if (Objects.isNull(roleUser)) {
                roleUser = new RoleDTO();
                roleUser.setRoleName(RoleName.USER.name());
                roleUser.setRemovalFlag(false);
                roleUser = roleService.save(roleUser);
            }
            RoleDTO roleAdmin = roleService.findByRoleName(RoleName.ADMIN.name());
            if (Objects.isNull(roleAdmin)) {
                roleAdmin = new RoleDTO();
                roleAdmin.setRoleName(RoleName.ADMIN.name());
                roleAdmin.setRemovalFlag(false);
                roleAdmin = roleService.save(roleAdmin);
            }
            UserDTO admin = userService.findByEmail("admin@store.com");
            if (Objects.isNull(admin)) {
                admin = new UserDTO();
                UserProfileDTO userProfile = userProfileService.findProfileByEmail("admin@store.com");
                if (Objects.isNull(userProfile)) {
                    userProfile = new UserProfileDTO();
                    userProfile.setName("Thanh Trong");
                    userProfile.setAvatar(mediaFile);
                    userProfile.setRemovalFlag(false);
                    userProfile = userProfileService.save(userProfile);
                }
                admin.setUserProfile(userProfile);
                admin.setUserName("admin");
                admin.setEmail("admin@store.com");
                admin.setActiveFlag(true);
                admin.setPassword(passwordEncoder.encode("Store@@admin"));
                Set<RoleDTO> roles = new HashSet<>();
                roles.add(roleAdmin);
                roles.add(roleUser);
                admin.setRoles(roles);
                admin.setRemovalFlag(false);
                userService.save(admin);
            }
        };
    }
}

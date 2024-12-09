package hcmute.nhom.kltn.service.impl;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import hcmute.nhom.kltn.dto.UserDTO;
import hcmute.nhom.kltn.email.DataMail;
import hcmute.nhom.kltn.email.EmailSender;
import hcmute.nhom.kltn.service.ClientService;
import hcmute.nhom.kltn.util.Constants;

/**
 * Class ClientServiceImpl.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Service
public class ClientServiceImpl implements ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);
    private EmailSender emailSender;

    public ClientServiceImpl(final EmailSender emailSender) {
        this.emailSender = emailSender;
    }
    @Override
    public void forgotPassword(UserDTO user, String password) {
        DataMail dataMail = new DataMail();

        dataMail.setTo(user.getEmail());
        dataMail.setSubject(Constants.SEND_MAIL.CLIENT_FORGET_PASSWORD);

        Map<String, Object> props = new HashMap<>();
        props.put("fullName", user.getUserProfile().getName());
        props.put("password", password);
        dataMail.setProps(props);

        emailSender.sendHtml(dataMail, Constants.TEMPLATE_FILE_NAME.CLIENT_FORGET_PASSWORD);
    }

    @Override
    public void activeUser(String email) {
        DataMail dataMail = new DataMail();

        dataMail.setTo(email);
        dataMail.setSubject(Constants.SEND_MAIL.CLIENT_ACTIVE_USER);

        Map<String, Object> props = new HashMap<>();
        props.put("email", email);
        dataMail.setProps(props);

        emailSender.sendHtml(dataMail, Constants.TEMPLATE_FILE_NAME.CLIENT_ACTIVE_USER);
    }
}

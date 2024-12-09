package hcmute.nhom.kltn.email;

import hcmute.nhom.kltn.dto.UserDTO;

/**
 * Class EmailSender.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
public interface EmailSender {
    void send(UserDTO user, String template);

    void sendHtml(DataMail dataMail, String template);
}

package hcmute.nhom.kltn.service;

import hcmute.nhom.kltn.dto.UserDTO;

/**
 * Class ClientService.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
public interface ClientService {
    void forgotPassword(UserDTO user, String password);

    void activeUser(String email);
}

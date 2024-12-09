package hcmute.nhom.kltn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import hcmute.nhom.kltn.common.AbstractMessage;

/**
 * Class HomeController.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@RestController
@RequestMapping("/api")
public class HomeController extends AbstractMessage {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("/home")
    public String home() {
        String messageStart = getMessageStart("123", "GET /api/home");
        logger.info("{}", messageStart);
        return "Hello World!";
    }
}

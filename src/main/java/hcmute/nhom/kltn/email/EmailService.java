package hcmute.nhom.kltn.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import hcmute.nhom.kltn.dto.UserDTO;

/**
 * Class EmailService.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Service
public class EmailService implements EmailSender {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public void send(UserDTO user, String template) {
        logger.info("Sending email to " + user.getEmail());
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            Context context = new Context();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            String mainMessage = templateEngine.process(template, context);

            helper.setTo(user.getEmail());

            helper.setSubject("Testing from Spring Boot");

            helper.setText(mainMessage, true);
            mailSender.send(msg);
        } catch (MessagingException exception) {
            logger.error("fail to send email", exception);
            throw new IllegalStateException("fail to send email");
        }
    }

    @Override
    public void sendHtml(DataMail dataMail, String template) {
        logger.info("Sending email to " + dataMail.getTo());
            try {
                MimeMessage message = mailSender.createMimeMessage();

                MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

                Context context = new Context();
                context.setVariables(dataMail.getProps());
                String html = templateEngine.process(template, context);
                helper.setTo(dataMail.getTo());
                helper.setSubject(dataMail.getSubject());
                helper.setText(html, true);

                mailSender.send(message);
            } catch (MessagingException exception) {
                logger.error("fail to send email", exception);
                throw new IllegalStateException("fail to send email");
            }
        }
    }

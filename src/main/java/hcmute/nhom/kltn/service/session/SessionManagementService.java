package hcmute.nhom.kltn.service.session;

import java.util.Objects;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Class SessionManagementService.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Service
public class SessionManagementService {

    @Autowired
    private SessionRepository sessionRepository;

    public HttpSession getSession() {
        return session;
    }

    HttpSession session;

    /**
     * Find session by current request.
     * @return HttpSession.
     */
    public HttpSession findSessionByCurrentRequest() {
        if (this.session == null) {
            final ServletRequestAttributes attr =
                    (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return attr.getRequest().getSession(true);
        }
        return this.session;
    }

    /**
     * Find attribute by session id.
     * @param sessionId
     * @param attrName
     * @return
     */
    public Object findAttributeBySessionId(String sessionId, String attrName) {
        Session session = sessionRepository.findById(sessionId);
        if (Objects.isNull(session)) {
            return null;
        }
        return session.getAttribute(attrName);
    }

    /**
     * Update attribute into current session.
     *
     * @param attrName String.
     * @param attrValue Enum.
     */
    public void setAttribute(String attrName, Enum<?> attrValue) {
        setAttribute(attrName, attrValue.toString());
    }

    /**
     * Update attribute into current session.
     *
     * @param attrName String.
     * @param attrValue String.
     */
    @SuppressWarnings("unchecked")
    public void setAttribute(String attrName, Object attrValue) {
        HttpSession httpSession = this.findSessionByCurrentRequest();
        Session session = sessionRepository.findById(httpSession.getId());
        if (session == null) {
            httpSession.setAttribute(attrName, attrValue);
        } else {
            session.setAttribute(attrName, attrValue);
            sessionRepository.save(session);
        }
    }

    /**
     * Get value attribute base on Session ID.
     *
     * @param attrName String.
     * @return value Object.
     */
    public Object getAttribute(String attrName) {
        HttpSession httpSession = this.findSessionByCurrentRequest();
        var tmpSessison = this.findAttributeBySessionId(httpSession.getId(), attrName);
        if (tmpSessison == null) {
            return httpSession.getAttribute(attrName);
        }
        return tmpSessison;
    }

    /**
     * Get value attribute base on Session ID.
     *
     * @param attrName String.
     * @return value Object.
     */
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String attrName, Class<T> clazz) {
        HttpSession httpSession = this.findSessionByCurrentRequest();
        var tmpSessison = this.findAttributeBySessionId(httpSession.getId(), attrName);
        if (tmpSessison == null) {
            return (T) httpSession.getAttribute(attrName);
        }
        return (T) tmpSessison;
    }

    /**
     * Remove value attribute base on Session ID.
     *
     * @param attrName
     */
    public void removeAttribute(String attrName) {
        HttpSession httpSession = this.findSessionByCurrentRequest();
        if (Objects.nonNull(httpSession)) {
            httpSession.removeAttribute(attrName);

            Session session = sessionRepository.findById(httpSession.getId());
            if (session != null) {
                session.removeAttribute(attrName);
            }
        }
    }

    /**
     * Destroy current Session - Cookies.
     */
    public HttpSession destroy() {
        HttpSession httpSession = this.findSessionByCurrentRequest();
        sessionRepository.deleteById(httpSession.getId());
        httpSession.invalidate();
        return httpSession;
    }

    public void destroySession(String sessionId) {
        sessionRepository.deleteById(sessionId);
    }
}

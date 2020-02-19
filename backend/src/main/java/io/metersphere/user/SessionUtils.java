package io.metersphere.user;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.util.Optional;

import static io.metersphere.commons.constants.SessionConstants.ATTR_USER;

public class SessionUtils {

    public static SessionUser getUser() {
        try {
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession();
            return (SessionUser) session.getAttribute(ATTR_USER);
        } catch (Exception e) {
            return null;
        }
    }

    //
    public static void putUser(SessionUser sessionUser) {
        SecurityUtils.getSubject().getSession().setAttribute(ATTR_USER, sessionUser);
    }

    public static String getCurrentWorkspaceId() {
        return Optional.ofNullable(getUser()).orElse(new SessionUser()).getWorkspaceId();
    }

    public static String getCurrentOrganizationId() {
        return Optional.ofNullable(getUser()).orElse(new SessionUser()).getOrganizationId();
    }
}

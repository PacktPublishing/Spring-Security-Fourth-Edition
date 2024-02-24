package com.packtpub.springsecurity.web.controllers;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Allows users to view the active sessions associated to their account and remove sessions that should no longer be active.
 *
 * @author bnasslahsen
 *
 */
@Controller
public class UserSessionController {

    private final SessionRegistry sessionRegistry;

    public UserSessionController(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @GetMapping("/user/sessions/")
    public String sessions(Authentication authentication, ModelMap model) {
        List<SessionInformation> sessions = sessionRegistry.getAllSessions(authentication.getPrincipal(),
                false);
        model.put("sessions", sessions);
        return "user/sessions";
    }

    @PostMapping(value="/user/sessions/{sessionId}")
    public String removeSession(@PathVariable String sessionId, RedirectAttributes redirectAttrs) {
        SessionInformation sessionInformation = sessionRegistry.getSessionInformation(sessionId);
        if(sessionInformation != null) {
            sessionInformation.expireNow();
        }
        redirectAttrs.addFlashAttribute("message", "Session was removed");
        return "redirect:/user/sessions/";
    }
}

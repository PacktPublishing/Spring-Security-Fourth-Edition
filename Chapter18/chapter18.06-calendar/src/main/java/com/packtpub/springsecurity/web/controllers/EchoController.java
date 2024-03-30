package com.packtpub.springsecurity.web.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Demonstrates how to use a Proxy Ticket to call a service. This client will call the Calendar applications My Events
 * page and echo the JSON response back.
 *
 * <p>
 * Note that this controller will not work until the entire Proxy Ticket authentication section has been completed.
 * </p>
 *
 * @author bnasslahsen
 *
 */
@RestController
public class EchoController {

	public final RestTemplate restTemplate = new RestTemplate();

	private String targetUrl;

	@GetMapping("/echo")
    public String echo()  {
        final CasAuthenticationToken token = (CasAuthenticationToken) SecurityContextHolder
                .getContext()
                .getAuthentication();
		// The proxyTicket could be cached in session and reused if we wanted to
        final String proxyTicket = token.getAssertion().getPrincipal().getProxyTicketFor(targetUrl);
        // Make a remote call using the proxy ticket
        return restTemplate.getForObject(targetUrl+"?ticket={pt}", String.class, proxyTicket);
    }

	@Value("${service.base.url}")
	public void setTargetHost(String targetHost) {
		this.targetUrl = targetHost+"/events/my";
	}
}

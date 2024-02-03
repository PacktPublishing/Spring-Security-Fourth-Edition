package com.packtpub.springsecurity.service;

import java.util.List;

import com.packtpub.springsecurity.dataaccess.CalendarUserDao;
import com.packtpub.springsecurity.dataaccess.EventDao;
import com.packtpub.springsecurity.domain.CalendarUser;
import com.packtpub.springsecurity.domain.Event;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * A default implementation of {@link CalendarService} that delegates to {@link EventDao} and {@link CalendarUserDao}.
 *
 *  @author bnasslahsen
 */
@Repository
public class DefaultCalendarService implements CalendarService {
	private final EventDao eventDao;
	private final CalendarUserDao userDao;
	private final MutableAclService aclService;
	private final UserContext userContext;


	public DefaultCalendarService(EventDao eventDao, CalendarUserDao userDao, MutableAclService aclService, UserContext userContext) {
		this.eventDao = eventDao;
		this.userDao = userDao;
		this.aclService = aclService;
		this.userContext = userContext;
	}

	public Event getEvent(int eventId) {
		return eventDao.getEvent(eventId);
	}

	@Transactional
	@Override
	public int createEvent(Event event) {
		int result = eventDao.createEvent(event);
		event.setId(result);
		// Add new ACL Entry:
		MutableAcl acl = aclService.createAcl(new ObjectIdentityImpl(event));
		PrincipalSid sid = new PrincipalSid(userContext.getCurrentUser().getEmail());
		acl.setOwner(sid);
		acl.insertAce(0,  BasePermission.READ, sid, true);
		aclService.updateAcl(acl);
		return result;
	}


	public List<Event> findForUser(int userId) {
		return eventDao.findForUser(userId);
	}

	public List<Event> getEvents() {
		return eventDao.getEvents();
	}

	public CalendarUser getUser(int id) {
		return userDao.getUser(id);
	}

	public CalendarUser findUserByEmail(String email) {
		return userDao.findUserByEmail(email);
	}

	public List<CalendarUser> findUsersByEmail(String partialEmail) {
		return userDao.findUsersByEmail(partialEmail);
	}
	
	public int createUser(CalendarUser user) {
		return userDao.createUser(user);
	}

}
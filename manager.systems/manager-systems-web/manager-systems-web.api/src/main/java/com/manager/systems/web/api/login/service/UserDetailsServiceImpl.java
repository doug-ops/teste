/*
 * Date create 30/06/2020.
 */
package com.manager.systems.web.api.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.manager.systems.web.api.common.constants.MessageConstants;
import com.manager.systems.web.api.login.domain.entity.User;
import com.manager.systems.web.api.login.domain.repository.UserRepository;

@Service(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final User user = this.userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(MessageConstants.MSG_FORBIDDEN);
		}
		return user;
	}
}

/*
 * Date create 30/06/2020.
 */
package com.manager.systems.web.api.login.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manager.systems.web.api.common.constants.ControllerConstants;
import com.manager.systems.web.api.common.controller.BaseController;

@RestController
@RequestMapping(ControllerConstants.URL_API_USER_DETAILS)
public class UserDetailsController extends BaseController {
	@GetMapping(ControllerConstants.URL_API_USER_DETAILS_USER_INFO)
	public UserDetails userInfo(@AuthenticationPrincipal final UserDetails user) {
		return user;
	}
}
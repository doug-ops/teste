/*
 * Date create 30/06/2020.
 */
package com.manager.systems.web.api.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.manager.systems.web.api.common.constants.CommonConstants;
import com.manager.systems.web.api.common.constants.ControllerConstants;
import com.manager.systems.web.api.common.constants.ParameterConstants;
import com.manager.systems.web.api.common.constants.SecurityConstants;
import com.manager.systems.web.api.common.controller.BaseController;
import com.manager.systems.web.api.login.domain.dto.UserDTO;
import com.manager.systems.web.api.login.service.UserService;

@RestController
@RequestMapping(ControllerConstants.URL_API + ControllerConstants.URL_API_VERSION + ControllerConstants.URL_API_USERS)
public class UserController extends BaseController {

	@Autowired
	private UserService service;
	
	@GetMapping(ControllerConstants.URL_API_GET_ALL)
	public ResponseEntity<?> getAll(
			@RequestParam(value = ParameterConstants.PARAMETER_PAGE, defaultValue = CommonConstants.ZERO) final Integer page,
			@RequestParam(value = ParameterConstants.PARAMETER_SIZE, defaultValue = CommonConstants.TEN) final Integer size) {
		return ResponseEntity.ok(this.service.getAll(PageRequest.of(page, size)));
	}
	
	@GetMapping(ControllerConstants.URL_API_PARAMETER_ID)
	public ResponseEntity<?> get(@PathVariable(ParameterConstants.PARAMETER_ID) final Long id) {
		return ResponseEntity.ok(this.service.get(id));
	}
	
	@Secured({ SecurityConstants.ROLE_ADMIN })
	@PostMapping(ControllerConstants.URL_API_SAVE)
	public ResponseEntity<?> save(@RequestBody final UserDTO dto) {
		populateDataDTO(dto);
		final UserDTO result = this.service.save(dto);
		return ResponseEntity.created(getUri(result.getId())).build();
	}

	/**
	@GetMapping("/info")
	public UserDTO userInfo(@AuthenticationPrincipal final User user) {
		//UserDetails userDetails = JwtUtil.getUserDetails(); //get logged user
		return UserDTO.create(user);
	}
	

	@GetMapping("/usersroles")
    public ResponseEntity<?> findUsersAndRoles() {
        final List<UsuarioRoleDTO> list = service.findUsersAndRoles();
        return list.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(list);
    }

    @GetMapping("/{role}")
    public ResponseEntity<?> findUsersByRole(@PathVariable("role") String role) {
        List<UserDTO> list = service.findUsersByRole(role);
        return list.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(list);
    }
    */
	
	/**
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody JwtLoginInput login){
		String username = login.getUsername();
		String password = login.getPassword();
		
		if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			throw new BadCredentialsException("Invalid username/password.");
		}
		
		Authentication auth = new UsernamePasswordAuthenticationToken(username, password);
		auth.isAuthenticated();
		return ResponseEntity.ok(login.toString());
	}
	*/
}
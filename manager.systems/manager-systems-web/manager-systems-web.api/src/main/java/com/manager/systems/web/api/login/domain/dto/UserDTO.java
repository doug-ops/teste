/*
 * Date create 30/06/2020.
 */
package com.manager.systems.web.api.login.domain.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.systems.web.api.common.domain.dto.BaseDTO;
import com.manager.systems.web.api.login.domain.entity.Role;
import com.manager.systems.web.api.login.domain.entity.User;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonInclude(value = Include.NON_NULL)
public class UserDTO extends BaseDTO{
    private Long id;
    private String name;
    private String username;
    private String password;
    private String email;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    
	// token jwt
	private String token;

	private List<String> roles;

	public static UserDTO create(final User user) {
		final UserDTO dto = new ModelMapper().map(user, UserDTO.class);

		// dto.roles = user.getRoles().stream().map(role ->
		// role.getNome()).collect(Collectors.toList());
		if(user.getRoles()!=null)
		{
			dto.roles = user.getRoles().stream().map(Role::getAlias).collect(Collectors.toList());			
		}
		dto.setUsername(null);
		dto.setPassword(null);
		return dto;
	}

	public static UserDTO create(final User user, final String token) {
		final UserDTO dto = create(user);
		dto.token = token;
		return dto;
	}

	public String toJson() throws JsonProcessingException {
		final ObjectMapper m = new ObjectMapper();
		return m.writeValueAsString(this);
	}
}

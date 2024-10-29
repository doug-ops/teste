/*
 * Date create 30/06/2020.
 */
package com.manager.systems.web.api.login.domain.dto;

import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.manager.systems.web.api.common.domain.dto.BaseDTO;
import com.manager.systems.web.api.login.domain.entity.Role;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonInclude(value = Include.NON_NULL)
public class RoleDTO extends BaseDTO {
	private Integer id;
	private String alias;
	private String description;
	
	public static RoleDTO createDTO(final Role entity) {
		return new ModelMapper().map(entity, RoleDTO.class);
	}
}
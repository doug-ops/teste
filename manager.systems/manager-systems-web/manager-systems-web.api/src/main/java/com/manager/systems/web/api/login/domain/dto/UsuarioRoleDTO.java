/*
 * Date create 30/06/2020.
 */
package com.manager.systems.web.api.login.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioRoleDTO {
	String username;
	String email;
	String role;
}
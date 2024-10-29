/*
 * Date create 30/06/2020.
 */
package com.manager.systems.web.api.common.security.jwt;

import lombok.Data;

@Data
public class JwtLoginInput {
	private String username;
	private String password;
}
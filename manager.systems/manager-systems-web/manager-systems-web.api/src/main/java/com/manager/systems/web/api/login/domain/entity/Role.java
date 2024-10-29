/*
 * Date create 30/06/2020.
 */
package com.manager.systems.web.api.login.domain.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import com.manager.systems.web.api.common.constants.TableConstants;
import com.manager.systems.web.api.common.domain.entity.BaseObject;
import com.manager.systems.web.api.common.enums.OperationsType;
import com.manager.systems.web.api.login.domain.dto.RoleDTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = TableConstants.TB_API_ROLE)
public class Role extends BaseObject implements GrantedAuthority {
	private static final long serialVersionUID = 6852835097528104013L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 20, nullable = false, unique = true)
	private String alias;
	@Column(length = 50, nullable = false)
	private String description;

	@Override
	public String getAuthority() {
		return this.alias;
	}
	
	public void populateDataFromDTO(final RoleDTO dto) {
		this.id = dto.getId();
		this.alias = dto.getAlias();
		this.description = dto.getDescription();
		this.setInactive(dto.getInactive());
		if (OperationsType.INSERT == dto.getOperationsType()) {
			this.setCreatedAt((Date.from(dto.getActualDate().toInstant())));
			this.setCreatedUser(dto.getUserId());
		}
		this.setUpdatedAt(Date.from(dto.getActualDate().toInstant()));
		this.setUpdatedUser(dto.getUserId());
	}
}
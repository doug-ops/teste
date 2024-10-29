/*
 * Date create 30/06/2020.
 */
package com.manager.systems.web.api.login.domain.entity;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.manager.systems.web.api.common.constants.ColumnConstants;
import com.manager.systems.web.api.common.constants.TableConstants;
import com.manager.systems.web.api.common.domain.entity.BaseObject;
import com.manager.systems.web.api.common.enums.OperationsType;
import com.manager.systems.web.api.login.domain.dto.UserDTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = TableConstants.TB_API_USER)
public class User extends BaseObject implements UserDetails {
	private static final long serialVersionUID = 6397560640797380840L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String username;
    private String password;
    private String email;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = TableConstants.TB_API_USER_ROLES,
            joinColumns = @JoinColumn(name = ColumnConstants.COLUMN_USER_ID, referencedColumnName = ColumnConstants.COLUMN_ID),
            inverseJoinColumns = @JoinColumn(name = ColumnConstants.COLUMN_ROLE_ID, referencedColumnName = ColumnConstants.COLUMN_ID))
    private List<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return !this.getInactive();
    }
    
	public void populateDataFromDTO(final UserDTO dto) {
	    this.id = dto.getId();
	    this.name = dto.getName();
	    this.username = dto.getUsername();
	    this.password = dto.getPassword();
	    this.email = dto.getEmail();
	    this.accountNonExpired = dto.isAccountNonExpired();
	    this.accountNonLocked = dto.isAccountNonLocked();
	    this.credentialsNonExpired = dto.isCredentialsNonExpired();
	    
		this.setInactive(dto.getInactive());
		if (OperationsType.INSERT == dto.getOperationsType()) {
			this.setCreatedAt((Date.from(dto.getActualDate().toInstant())));
			this.setCreatedUser(dto.getUserId());
		}
		this.setUpdatedAt(Date.from(dto.getActualDate().toInstant()));
		this.setUpdatedUser(dto.getUserId());
	}
}
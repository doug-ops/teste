/*
 * Date create 30/06/2020.
 */
package com.manager.systems.web.api.login.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.manager.systems.web.api.common.constants.ParameterConstants;
import com.manager.systems.web.api.login.domain.dto.UsuarioRoleDTO;
import com.manager.systems.web.api.login.domain.entity.User;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(final String username);
    @Query(value = "SELECT new com.manager.systems.web.api.login.domain.dto.UsuarioRoleDTO(u.username, u.email, r.alias) FROM User u inner join u.roles r")
    List<UsuarioRoleDTO> findUsersAndRoles();

    @Query(value = "SELECT u FROM User u inner join u.roles r WHERE r.alias like %:role%")
    List<User> findUsersByRole(@Param(ParameterConstants.PARAMETER_ROLE) final String role);
}

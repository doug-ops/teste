/*
 * Date create 30/06/2020.
 */
package com.manager.systems.web.api.login.domain.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.manager.systems.web.api.login.domain.entity.Role;

public interface RoleRepository extends JpaRepository<Role,Integer> {
	List<Role> findByInactive(boolean inactive, Pageable pageable);
}
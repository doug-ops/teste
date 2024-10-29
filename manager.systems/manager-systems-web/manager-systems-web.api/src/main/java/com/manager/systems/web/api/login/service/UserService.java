/*
 * Date create 30/06/2020.
 */
package com.manager.systems.web.api.login.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.manager.systems.web.api.common.constants.MessageConstants;
import com.manager.systems.web.api.common.enums.OperationsType;
import com.manager.systems.web.api.login.domain.dto.UserDTO;
import com.manager.systems.web.api.login.domain.dto.UsuarioRoleDTO;
import com.manager.systems.web.api.login.domain.entity.User;
import com.manager.systems.web.api.login.domain.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;

	public List<UserDTO> getUsers() {
		return this.repository.findAll().stream().map(UserDTO::create).collect(Collectors.toList());
	}

	public List<UsuarioRoleDTO> findUsersAndRoles() {
		return this.repository.findUsersAndRoles();
	}

	public List<UserDTO> findUsersByRole(final String role) {
		return this.repository.findUsersByRole(role).stream().map(UserDTO::create).collect(Collectors.toList());
	}

	public List<UserDTO> getAll(final Pageable pageable) {
		return this.repository.findAll(pageable).stream().map(UserDTO::create).collect(Collectors.toList());
	}

	public UserDTO get(final Long id) {
		final Optional<User> entity = this.repository.findById(id);
		return entity.map(UserDTO::create)
				.orElseThrow(() -> new EntityNotFoundException(MessageConstants.MSG_CLIENT_NOT_FOUND));
	}

	public UserDTO inactive(final UserDTO dto) {
		final Optional<User> optional = this.repository.findById(dto.getId());
		if (!optional.isPresent()) {
			throw new EntityNotFoundException(MessageConstants.MSG_CLIENT_NOT_FOUND);
		}
		final User entity = optional.get();
		dto.setOperationsType(OperationsType.UPDATE);
		entity.populateDataFromDTO(dto);
		return UserDTO.create(this.repository.save(entity));
	}

	public UserDTO save(final UserDTO dto) {
		User entity = null;
		if (dto.getId() != null) {
			final Optional<User> optional = this.repository.findById(dto.getId());
			if (optional.isPresent()) {
				dto.setOperationsType(OperationsType.UPDATE);
				entity = optional.get();
			} else {
				dto.setOperationsType(OperationsType.INSERT);
				entity = new User();
			}
		} else {
			dto.setOperationsType(OperationsType.INSERT);
			entity = new User();
		}
		entity.populateDataFromDTO(dto);
		return UserDTO.create(this.repository.save(entity));
	}
}
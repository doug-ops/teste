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
import com.manager.systems.web.api.login.domain.dto.RoleDTO;
import com.manager.systems.web.api.login.domain.entity.Role;
import com.manager.systems.web.api.login.domain.repository.RoleRepository;

@Service
public class RoleService {
	
	@Autowired
	private RoleRepository repository;

	public List<RoleDTO> getAll(final Pageable pageable) {
		return this.repository.findAll(pageable).stream().map(RoleDTO::createDTO).collect(Collectors.toList());
	}

	public RoleDTO get(final Integer id) {
		final Optional<Role> entity = this.repository.findById(id);
		return entity.map(RoleDTO::createDTO)
				.orElseThrow(() -> new EntityNotFoundException(MessageConstants.MSG_CLIENT_NOT_FOUND));
	}

	public RoleDTO inactive(final RoleDTO dto) {
		final Optional<Role> optional = this.repository.findById(dto.getId());
		if (!optional.isPresent()) {
			throw new EntityNotFoundException(MessageConstants.MSG_CLIENT_NOT_FOUND);
		}
		final Role entity = optional.get();
		dto.setOperationsType(OperationsType.UPDATE);
		entity.populateDataFromDTO(dto);
		return RoleDTO.createDTO(this.repository.save(entity));
	}

	public RoleDTO save(final RoleDTO dto) {
		Role entity = null;
		if(dto.getId()!=null) {
			final Optional<Role> optional = this.repository.findById(dto.getId());
			if (optional.isPresent()) {
				dto.setOperationsType(OperationsType.UPDATE);
				entity = optional.get();
			} else {
				dto.setOperationsType(OperationsType.INSERT);
				entity = new Role();
			}			
		}
		else
		{
			dto.setOperationsType(OperationsType.INSERT);
			entity = new Role();			
		}
		entity.populateDataFromDTO(dto);
		return RoleDTO.createDTO(this.repository.save(entity));
	}
}
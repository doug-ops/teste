package com.manager.systems.web.jobs.service.impl;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.web.jobs.dao.DocumentSincDao;
import com.manager.systems.web.jobs.dao.impl.DocumentSincDaoImpl;
import com.manager.systems.web.jobs.dto.DocumentSincDTO;
import com.manager.systems.web.jobs.dto.JobDTO;
import com.manager.systems.web.jobs.service.DocumentSincService;

public class DocumentSincServiceImpl implements DocumentSincService
{
	private DocumentSincDao documentSincDao;
	
	public DocumentSincServiceImpl() 
	{
		super();
		this.documentSincDao = new DocumentSincDaoImpl();
	}
	
	@Override
	public List<DocumentSincDTO> getAllMajorVersionRecord(final JobDTO job, final Connection connection) throws Exception 
	{
		return this.documentSincDao.getAllMajorVersionRecord(job, connection);
	}

	@Override
	public boolean save(final DocumentSincDTO documentSinc, final Connection connection) throws Exception 
	{
		return this.documentSincDao.save(documentSinc, connection);
	}
}
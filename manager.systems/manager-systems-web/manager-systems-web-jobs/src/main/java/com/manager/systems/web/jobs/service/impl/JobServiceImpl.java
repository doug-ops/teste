package com.manager.systems.web.jobs.service.impl;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.web.jobs.dao.JobDao;
import com.manager.systems.web.jobs.dao.impl.JobDaoImpl;
import com.manager.systems.web.jobs.dto.JobDTO;
import com.manager.systems.web.jobs.service.JobService;

public class JobServiceImpl implements JobService 
{
	private JobDao jobDao;
	
	public JobServiceImpl() 
	{
		super();
		this.jobDao = new JobDaoImpl();
	}

	@Override
	public boolean save(final JobDTO job, final Connection connection) throws Exception
	{
		return this.jobDao.save(job, connection);
	}

	@Override
	public boolean inactive(final JobDTO job, final Connection connection) throws Exception
	{
		return this.jobDao.inactive(job, connection);
	}

	@Override
	public void get(final JobDTO job, final Connection connection) throws Exception 
	{
		this.jobDao.get(job, connection);
	}

	@Override
	public List<JobDTO> getAll(final Connection connection) throws Exception
	{
		return this.jobDao.getAll(connection);
	}
}
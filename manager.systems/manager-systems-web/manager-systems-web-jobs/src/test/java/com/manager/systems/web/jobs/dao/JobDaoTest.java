package com.manager.systems.web.jobs.dao;

import java.sql.Connection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.manager.systems.web.jobs.dao.impl.JobDaoImpl;
import com.manager.systems.web.jobs.dto.JobDTO;
import com.manager.systems.web.jobs.utils.ConnectionUtils;

public class JobDaoTest 
{
	private Connection connection = null;
	private JobDao jobDao = null;
	
	@Before
	public void inicializa() throws Exception
	{
		this.connection = ConnectionUtils.getConnection();
		this.connection.setAutoCommit(false);
		this.jobDao = new JobDaoImpl();
	}
	
	@Test
	public void crud() throws Exception
	{		
		Assert.assertNotNull(this.connection);
		Assert.assertNotNull(this.jobDao);
		
		//Save insert
		JobDTO job = new JobDTO();
		job.setId(1);
		job.setParentId(0);
		job.setDescription("Sincronia de Documentos");
		job.setSyncTimer("0 0/1 * * * ?");
		job.setInactive(false);
		job.setUserChange(1);
		boolean isSaved = this.jobDao.save(job, connection);
		Assert.assertTrue(isSaved);
		
		//Get
		job = new JobDTO();
		job.setId(1);
		job.setParentId(0);
		job.setInactive(false);
		job.setUserChange(1);		
		this.jobDao.get(job, connection);
		Assert.assertEquals("Sincronia de Documentos", job.getDescription());


		//Inactive
		job = new JobDTO();
		job.setId(1);
		job.setParentId(0);
		job.setInactive(true);
		job.setUserChange(1);		
		final boolean isInactive = this.jobDao.inactive(job, connection);
		Assert.assertTrue(isInactive);

		//Get
		job = new JobDTO();
		job.setId(1);
		job.setParentId(0);
		job.setInactive(true);
		job.setUserChange(1);		
		this.jobDao.get(job, connection);
		Assert.assertTrue(job.isInactive());
		
		//Save update
		job = new JobDTO();
		job.setId(1);
		job.setParentId(0);
		job.setDescription("Sincronia de Documentos");
		job.setSyncTimer("0 0/1 * * * ?");
		job.setInactive(false);
		job.setUserChange(1);
		final boolean isUpdated = this.jobDao.save(job, connection);
		Assert.assertTrue(isUpdated);		
		
		final List<JobDTO> jobs = this.jobDao.getAll(connection);
		Assert.assertTrue(jobs.size()>0);
	}

	@After
	public void finaliza() throws Exception
	{
		if(this.connection!=null)
		{
			this.connection.rollback();
			this.connection.close();
		}
		Assert.assertTrue(this.connection.isClosed());
		this.connection = null;		
	}
}
package com.manager.systems.web.jobs.dao;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.web.jobs.dto.JobDTO;

public interface JobDao 
{
	boolean save(JobDTO job, Connection connection) throws Exception;
	boolean inactive(JobDTO job, Connection connection) throws Exception;
	void get(JobDTO job, Connection connection) throws Exception;
	List<JobDTO> getAll(Connection connection) throws Exception;
}
package com.manager.systems.web.jobs.service;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.web.jobs.dto.JobDTO;

public interface JobService 
{
	boolean save(JobDTO job, Connection connection) throws Exception;
	boolean inactive(JobDTO job, Connection connection) throws Exception;
	void get(JobDTO job, Connection connection) throws Exception;
	List<JobDTO> getAll(Connection connection) throws Exception;
}
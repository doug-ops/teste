package com.manager.systems.web.jobs.dao;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.web.jobs.dto.DocumentSincDTO;
import com.manager.systems.web.jobs.dto.JobDTO;

public interface DocumentSincDao 
{
	List<DocumentSincDTO> getAllMajorVersionRecord(JobDTO job, Connection connection) throws Exception;
	boolean save(DocumentSincDTO documentSinc, Connection connection) throws Exception;
}
package com.manager.systems.web.jobs.service;

import java.sql.Connection;
import java.util.List;

import com.manager.systems.web.jobs.dto.DocumentSincDTO;
import com.manager.systems.web.jobs.dto.JobDTO;

public interface DocumentSincService 
{
	List<DocumentSincDTO> getAllMajorVersionRecord(JobDTO job, Connection connection) throws Exception;
	boolean save(DocumentSincDTO documentSinc, Connection connection) throws Exception;
}
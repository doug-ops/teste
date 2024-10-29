package com.manager.systems.web.jobs.dao;

import java.sql.Connection;

import com.manager.systems.web.jobs.dto.ScriptDTO;

public interface ScriptDao 
{
	void getScriptsTable(ScriptDTO script, Connection connection) throws Exception;
}
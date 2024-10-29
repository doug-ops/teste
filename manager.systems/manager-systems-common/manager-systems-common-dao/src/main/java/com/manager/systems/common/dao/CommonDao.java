package com.manager.systems.common.dao;

public interface CommonDao 
{
	long getNextCode(String tableName, long initial) throws Exception;
}
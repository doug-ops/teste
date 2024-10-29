package com.manager.systems.common.service;

public interface CommonService 
{
	long getNextCode(String tableName, long initial) throws Exception;
}
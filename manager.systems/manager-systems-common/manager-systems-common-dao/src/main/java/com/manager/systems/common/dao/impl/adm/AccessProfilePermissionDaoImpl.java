package com.manager.systems.common.dao.impl.adm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.manager.systems.common.dao.adm.AccessProfilePermissionDao;
import com.manager.systems.common.dao.utils.DatabaseConstants;
import com.manager.systems.common.dto.adm.AccessProfilePermissionDTO;
import com.manager.systems.common.dto.adm.AccessProfilePermissionFilterDTO;
import com.manager.systems.common.vo.OperationType;

public class AccessProfilePermissionDaoImpl implements AccessProfilePermissionDao
{
	private Connection connection;

	public AccessProfilePermissionDaoImpl(final Connection connection) 
	{
		super();
		this.connection = connection;
	}
	
	@Override
	public boolean save(final AccessProfilePermissionDTO accessProfilePermission) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_access_profile_permission");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _id int,
		query.append("?, "); //03 - IN _parent_id int,
		query.append("?, "); //04 - IN _access_profile_id int,
		query.append("?, "); //05 - IN _permission bit, 
		query.append("?, "); //06 - IN _inactive int, 
		query.append("?  "); //07 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.SAVE.getType()); //01 - IN _operation INT,
			statement.setInt(2, accessProfilePermission.getPermissionId()); //02 - IN _id int,
			statement.setInt(3, accessProfilePermission.getPermissionParentId()); //03 - IN _parent_id int,
			statement.setInt(4, accessProfilePermission.getAccessProfileId()); //04 - IN _access_profile_id int, 
			statement.setBoolean(5, accessProfilePermission.isPermission()); //05 - IN _permission bit, 
			statement.setBoolean(6, accessProfilePermission.isInactive()); //06 - IN _inactive bit, 
			statement.setLong(7, accessProfilePermission.getUserChange()); //06 - IN _user_change BIGINT			
	        result = (statement.executeUpdate()>0);  
		}
		catch(final Exception ex)
		{
			throw ex;
		}
		finally
		{
			if(statement!=null)
			{
				statement.close();
			}
		}
		return result;
	}

	@Override
	public boolean inactiveAll(final AccessProfilePermissionDTO accessProfilePermission) throws Exception 
	{
		boolean result = false;
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_access_profile_permission");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _id int,
		query.append("?, "); //03 - IN _parent_id int,
		query.append("?, "); //04 - IN _access_profile_id int,
		query.append("?, "); //05 - IN _permission bit, 
		query.append("?, "); //06 - IN _inactive int, 
		query.append("?  "); //07 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.INACTIVE_ALL.getType()); //01 - IN _operation INT,
			statement.setInt(2, accessProfilePermission.getPermissionId()); //02 - IN _id int,
			statement.setInt(3, accessProfilePermission.getPermissionParentId()); //03 - IN _parent_id int,
			statement.setInt(4, accessProfilePermission.getAccessProfileId()); //04 - IN _access_profile_id int, 
			statement.setBoolean(5, accessProfilePermission.isPermission()); //05 - IN _permission bit, 
			statement.setBoolean(6, accessProfilePermission.isInactive()); //06 - IN _inactive bit, 
			statement.setLong(7, accessProfilePermission.getUserChange()); //06 - IN _user_change BIGINT			
	        result = (statement.executeUpdate()>0);  
		}
		catch(final Exception ex)
		{
			throw ex;
		}
		finally
		{
			if(statement!=null)
			{
				statement.close();
			}
		}
		
		return result;
	}

	@Override
	public Map<Integer, AccessProfilePermissionDTO> getByAccessProfile(final AccessProfilePermissionFilterDTO filter) throws Exception 
	{
		final Map<Integer, AccessProfilePermissionDTO> items = new TreeMap<Integer, AccessProfilePermissionDTO>();
		
		CallableStatement statement = null;
		final StringBuilder query = new StringBuilder();
		
		query.append("{");
		query.append("call pr_maintenance_access_profile_permission");
		query.append("(");
		query.append("?, "); //01 - IN _operation INT,
		query.append("?, "); //02 - IN _id int,
		query.append("?, "); //03 - IN _parent_id int,
		query.append("?, "); //04 - IN _access_profile_id int,
		query.append("?, "); //05 - IN _permission bit, 
		query.append("?, "); //06 - IN _inactive int, 
		query.append("?  "); //07 - IN _user_change BIGINT
		query.append(")");
		query.append("}");
		
		ResultSet resultSet = null;
		
		try
		{
			statement = this.connection.prepareCall(query.toString());
			statement.setInt(1, OperationType.GET.getType()); //01 - IN _operation INT,
			statement.setInt(2, filter.getPermissionId()); //02 - IN _id int,
			statement.setInt(3, filter.getPermissionParentId()); //03 - IN _parent_id int,
			statement.setInt(4, filter.getAccessProfileId()); //04 - IN _access_profile_id int, 
			statement.setNull(5, Types.BIT); //05 - IN _permission bit, 
			statement.setBoolean(6, filter.isInactive()); //06 - IN _inactive bit, 
			statement.setNull(7, Types.BIGINT); //06 - IN _user_change BIGINT			
	        
			resultSet = statement.executeQuery();  
	        while (resultSet.next()) 
	        {
	        	final int permissionId = resultSet.getInt(DatabaseConstants.COLUMN_PERMISSION_ID);
	        	final int permissionParentId = resultSet.getInt(DatabaseConstants.COLUMN_PERMISSION_PARENT_ID);

	        	AccessProfilePermissionDTO permission = null;
	        	if(permissionParentId==0)
	        	{
	        		if(items.containsKey(permissionId))
	        		{
	        			permission = items.get(permissionId);	        			
	        		}
	        		
	        		if(permission == null) {
	        			permission = AccessProfilePermissionDTO.builder()
	        					.permissionId(permissionId)
	        					.permissionParentId(permissionParentId)	        					
	        					.permissionDescription(resultSet.getString(DatabaseConstants.COLUMN_PERMISSION_DESCRIPTION))
	        					.permissionRole(resultSet.getString(DatabaseConstants.COLUMN_PERMISSION_ROLE))
	        					.permission(resultSet.getBoolean(DatabaseConstants.COLUMN_PERMISSION))
	        					.build();
	        		}
	        		
	        		items.put(permissionId, permission);
	        	}
	        	else
	        	{
	        		final Optional<AccessProfilePermissionDTO> permissionOp = items.values().parallelStream().filter(x -> x.getPermissionId() == permissionParentId).findFirst();
	        		if(permissionOp.isPresent()) {
	        			permission = permissionOp.get();
	        			permission.addItem(AccessProfilePermissionDTO.builder()
	        					.permissionId(permissionId)
	        					.permissionParentId(permissionParentId)	        					
	        					.permissionDescription(resultSet.getString(DatabaseConstants.COLUMN_PERMISSION_DESCRIPTION))
	        					.permissionRole(resultSet.getString(DatabaseConstants.COLUMN_PERMISSION_ROLE))
	        					.permission(resultSet.getBoolean(DatabaseConstants.COLUMN_PERMISSION))
	        					.build());
	        			items.put(permissionParentId, permission);
	        		} else {
	        			for(final Map.Entry<Integer, AccessProfilePermissionDTO> entry : items.entrySet()) {
	        				if(entry.getValue().getItems() != null && entry.getValue().getItems().size() > 0) {
	        					for(final Map.Entry<Integer, AccessProfilePermissionDTO> entryItem : entry.getValue().getItems().entrySet()) {
	        						final AccessProfilePermissionDTO valueItem = entryItem.getValue();
	        						if(valueItem.getPermissionId() == permissionParentId) {
		        						valueItem.addItem(AccessProfilePermissionDTO.builder()
					        					.permissionId(permissionId)
					        					.permissionParentId(permissionParentId)	        					
					        					.permissionDescription(resultSet.getString(DatabaseConstants.COLUMN_PERMISSION_DESCRIPTION))
					        					.permissionRole(resultSet.getString(DatabaseConstants.COLUMN_PERMISSION_ROLE))
					        					.permission(resultSet.getBoolean(DatabaseConstants.COLUMN_PERMISSION))
					        					.build());	        							        							
	        						}
	        					}
	        				}
	        			}
	        		}
	        	}
	        }	    
		}
		catch(final Exception ex)
		{
			throw ex;
		}
		finally
		{
			if(resultSet!=null)
			{
				resultSet.close();
			}
			if(statement!=null)
			{
				statement.close();
			}
		}
		return items;
	}
}
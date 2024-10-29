package com.manager.systems.web.jobs.tasks;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.manager.systems.web.jobs.dao.FactoryConnection;
import com.manager.systems.web.jobs.dao.impl.MovementDataProccessDTO;
import com.manager.systems.web.jobs.dto.JobDTO;
import com.manager.systems.web.jobs.dto.MovementDataProductSincDTO;
import com.manager.systems.web.jobs.dto.MovementDataProductSincItemDTO;
import com.manager.systems.web.jobs.service.JobService;
import com.manager.systems.web.jobs.service.MovementDataProcessService;
import com.manager.systems.web.jobs.service.MovementDataProductSincService;
import com.manager.systems.web.jobs.service.impl.JobServiceImpl;
import com.manager.systems.web.jobs.service.impl.MovementDataProcessServiceImpl;
import com.manager.systems.web.jobs.service.impl.MovementDataProductSincServiceImpl;
import com.manager.systems.web.jobs.utils.ConstantDataManager;
import com.manager.systems.web.jobs.vo.OperationType;

public class SincronizeProductMovementTask implements Runnable 
{
	private static final Log log = LogFactory.getLog(SincronizeProductMovementTask.class);

	private JobDTO job;
	
	public SincronizeProductMovementTask(final JobDTO job) 
	{
		super();
		this.job = job;
	}
	
	@Override
	public void run() 
	{
		try
		{
			if(this.job!=null && this.job.getParentId()>0)
			{
				if(ConstantDataManager.TASK_SINC_PRODUCT_MOVEMENT.equalsIgnoreCase(this.job.getDescription()))
				{
					log.info(ConstantDataManager.GLOBAL_MSG_INITIAL_SINC + ConstantDataManager.TASK_SINC_PRODUCT_MOVEMENT);

					MovementDataProductSincDTO movement = this.getProductsMovement();
					int itensSize = movement.getItens().size();
					while(itensSize>0)
					{
						log.info(ConstantDataManager.GLOBAL_MSG_ITENS_TO_SINC + ConstantDataManager.SPACE + movement.getItens().size() + ConstantDataManager.SPACE + ConstantDataManager.TASK_SINC_PRODUCT_MOVEMENT);
						if(itensSize>0)
						{
							log.info(ConstantDataManager.GLOBAL_MSG_SAVE_ITENS + ConstantDataManager.SPACE + movement.getItens().size() + ConstantDataManager.SPACE + ConstantDataManager.TASK_SINC_PRODUCT_MOVEMENT);
							this.saveProductsMovement(movement.getItens());
							this.changeSincStateProductMovementsPendingSinc(movement.getItens());
							job.setInitialRecord(String.valueOf(movement.getItens().get(0).getMovementLegacyId()));
							job.setVersionInitialRecord(movement.getItens().get(0).getVersionRecord());
							job.setFinalRecord(String.valueOf(movement.getItens().get(movement.getItens().size()-1).getMovementLegacyId()));
							job.setVersionFinalRecord(movement.getItens().get(movement.getItens().size()-1).getVersionRecord());	
							log.info(ConstantDataManager.GLOBAL_MSG_SAVE_JOB + movement.getItens().size() + ConstantDataManager.SPACE + ConstantDataManager.TASK_SINC_PRODUCT_MOVEMENT);
							saveJob(job);				
						}

						log.info(ConstantDataManager.GLOBAL_MSG_SINC_FINISH + ConstantDataManager.SPACE + movement.getItens().size() + ConstantDataManager.SPACE + ConstantDataManager.TASK_SINC_PRODUCT_MOVEMENT);
						movement = this.getProductsMovement();
						itensSize = movement.getItens().size();
					}					
					log.info(ConstantDataManager.GLOBAL_MSG_PROCESS_MOVEMENT_INIT);
					processMovement();
					log.info(ConstantDataManager.GLOBAL_MSG_PROCESS_MOVEMENT_FINISH);
				}
			}
		}
		catch (final Exception e) 
		{
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
	
	private MovementDataProductSincDTO getProductsMovement() throws Exception
	{
		final MovementDataProductSincDTO movement = new MovementDataProductSincDTO();
		movement.setOperationType(OperationType.GET_ALL);
		
		Connection connection = null;
		try
		{
			connection = FactoryConnection.getConnectionQuarterHorse();
			final MovementDataProductSincService movementDataProductSincService = new MovementDataProductSincServiceImpl();
			movementDataProductSincService.getProductMovementsPendingSinc(movement, connection);
		}
		catch (final Exception e) 
		{
			throw e;
		}
		finally
		{
			if(connection!=null)
			{
				connection.close();
			}
		}		
		return movement;
	}
	
	private void saveProductsMovement(final List<MovementDataProductSincItemDTO> itens) throws Exception
	{		
		Connection connection = null;
		try
		{
			connection = FactoryConnection.getConnectionPortal();
			final MovementDataProductSincService movementDataProductSincService = new MovementDataProductSincServiceImpl();
			for (final MovementDataProductSincItemDTO item : itens) 
			{
				movementDataProductSincService.saveProductMovementPendingSinc(item, connection);				
			}
		}
		catch (final Exception e) 
		{
			throw e;
		}
		finally
		{
			if(connection!=null)
			{
				connection.close();
			}
		}
	}
	
	private boolean processMovement() throws Exception
	{		
		boolean result = false;
		
		final MovementDataProccessDTO movementData = new MovementDataProccessDTO();
		movementData.setOffline(false);
		movementData.setOperation(OperationType.GET);
		
		Connection connection = null;
		try
		{
			connection = FactoryConnection.getConnectionPortal();
			connection.setAutoCommit(false);
			
			final MovementDataProcessService movementDataProcessService = new MovementDataProcessServiceImpl(connection);
			movementDataProcessService.getNextMovementProcess(movementData);
			
			log.info(ConstantDataManager.GLOBAL_MSG_FILTERED_MOVEMENT+ConstantDataManager.SPACE+movementData.toString());
			
			if(movementData.getCompanyId()>0 && movementData.getMovementsIds()!=null && movementData.getMovementsIds().length()>0)
			{
				movementData.setPreview(false);
				movementData.setUserId(1L);
				result = movementDataProcessService.processMovementData(movementData);
				if(!result)
				{
					throw new Exception(ConstantDataManager.MSG_PROCESS_MOVMENT_ERROR);
				}
				connection.commit();
			}
			else
			{
				log.info(ConstantDataManager.GLOBAL_MSG_HAS_NO_MOVEMENT_TO_PROCESS);				
			}
			log.info(ConstantDataManager.GLOBAL_MSG_PROCESS_MOVEMENT_FINISH+ConstantDataManager.SPACE+movementData.toString());
		}
		catch (final Exception e) 
		{
			if(connection!=null)
			{
				connection.rollback();
			}
			throw e;
		}
		finally
		{
			if(connection!=null)
			{
				connection.close();
			}
		}
		
		return result;
	}
	
	private void changeSincStateProductMovementsPendingSinc(final List<MovementDataProductSincItemDTO> itens) throws Exception
	{		
		Connection connection = null;
		try
		{
			connection = FactoryConnection.getConnectionQuarterHorse();
			final MovementDataProductSincService movementDataProductSincService = new MovementDataProductSincServiceImpl();
			for (final MovementDataProductSincItemDTO item : itens) 
			{
				movementDataProductSincService.changeSincStateProductMovementsPendingSinc(item, connection);				
			}
		}
		catch (final Exception e) 
		{
			throw e;
		}
		finally
		{
			if(connection!=null)
			{
				connection.close();
			}
		}
	}
	
	private void saveJob(final JobDTO job) throws Exception
	{		
		Connection connection = null;
		try
		{
			connection = FactoryConnection.getConnectionPortal();
			final JobService jobService = new JobServiceImpl();
			jobService.save(job, connection);
		}
		catch (final Exception e) 
		{
			throw e;
		}
		finally
		{
			if(connection!=null)
			{
				connection.close();
			}
		}
	}
}
/*
 * Date create 02/09/2023.
 */
package com.manager.systems.web.jobs.dao;

import com.manager.systems.web.jobs.dto.RomaneioFilterDTO;

public interface RomaneioDao {
	void generateNextWeekMovement(RomaneioFilterDTO filter) throws Exception;
}
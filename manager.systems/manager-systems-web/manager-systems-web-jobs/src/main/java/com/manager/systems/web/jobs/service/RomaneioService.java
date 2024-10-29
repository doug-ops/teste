/*
 * Date create 02/09/2023.
 */
package com.manager.systems.web.jobs.service;

import com.manager.systems.web.jobs.dto.RomaneioFilterDTO;

public interface RomaneioService {
	void generateNextWeekMovement(RomaneioFilterDTO filter) throws Exception;
}

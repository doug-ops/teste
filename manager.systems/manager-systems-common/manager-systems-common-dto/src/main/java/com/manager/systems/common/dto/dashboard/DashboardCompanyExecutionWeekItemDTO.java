/*
 * Create Date 14/03/2022 
 */
package com.manager.systems.common.dto.dashboard;

import java.io.Serializable;
import java.util.Date;

import com.manager.systems.common.utils.StringUtils;


public class DashboardCompanyExecutionWeekItemDTO implements Serializable {
	private static final long serialVersionUID = 4998338035847455580L;


	private int countMovement;
	private int countExecuted;
	private int countNotExecuted;
	private int weekYear;
	private String weekYearString;
	private Date initialDateWeek;
	private Date finalDateWeek;
	private String initialDateWeekString;
	private String finalDateWeekString;
	private int countNegative;
	private int countLowMovement;
	private int countOthers;
	private int countNotInformed;
	private int countDisable;
	private int countCompanyNotExpiry;
	private boolean selectedMovement;
	private int countDisableExecuted;
	private int countNegativeExecuted;
	
	public DashboardCompanyExecutionWeekItemDTO() {
		super();
	}

	public final int getCountMovement() {
		return this.countMovement;
	}

	public final void setCountMovement(final int countMovement) {
		this.countMovement = countMovement;
	}

	public final int getCountExecuted() {
		return this.countExecuted;
	}

	public final void setCountExecuted(final int countExecuted) {
		this.countExecuted = countExecuted;
	}

	public final int getCountNotExecuted() {
		return this.countNotExecuted;
	}

	public final void setCountNotExecuted(final int countNotExecuted) {
		this.countNotExecuted = countNotExecuted;
	}
	
	public final int getWeekYear() {
		return this.weekYear;
	}

	public final void setWeekYear(final int weekYear) {
		String barra = "/";
		this.weekYear = weekYear;
		this.weekYearString = String.valueOf(this.weekYear);
		this.weekYearString = weekYearString.substring(0, 4) + barra + weekYearString.substring(4);
	}
	public final String getWeekYearString() {
		return this.weekYearString;
	}

	public final void setWeekYearString(final String weekYearString) {
		this.weekYearString = weekYearString;
	}
	

	public final Date getInitialDateWeek() {
		return this.initialDateWeek;
	}

	public final void setInitialDateWeek(final Date initialDateWeek) {
		this.initialDateWeek = initialDateWeek;
		this.initialDateWeekString = StringUtils.formatDate(this.initialDateWeek, StringUtils.DATE_PATTERN_DD_MM_YYYY);
	}

	public final Date getFinalDateWeek() {
		return this.finalDateWeek;
	}

	public final void setFinalDateWeek(final Date finalDateWeek) {
		this.finalDateWeek = finalDateWeek;
		this.finalDateWeekString = StringUtils.formatDate(this.finalDateWeek, StringUtils.DATE_PATTERN_DD_MM_YYYY);
	}
	
	public final String getInitialDateWeekString() {
		return this.initialDateWeekString;
	}

	public final void setInitialDateWeekString(final String initialDateWeekString) {
		this.initialDateWeekString = initialDateWeekString;
	}
	
	public final String getFinalDateWeekString() {
		return this.finalDateWeekString;
	}

	public final void setFinalDateWeekString(final String finalDateWeekString) {
		this.finalDateWeekString = finalDateWeekString;
	}
	

	public final int getCountNegative() {
		return this.countNegative;
	}

	public final void setCountNegative(final int countNegative) {
		this.countNegative = countNegative;
	}

	public final int getCountLowMovement() {
		return this.countLowMovement;
	}

	public final void setCountLowMovement(final int countLowMovement) {
		this.countLowMovement = countLowMovement;
	}

	public final int getCountOthers() {
		return this.countOthers;
	}

	public final void setCountOthers(final int countOthers) {
		this.countOthers = countOthers;
	}

	public final int getCountNotInformed() {
		return this.countNotInformed;
	}

	public final void setCountNotInformed(final int countNotInformed) {
		this.countNotInformed = countNotInformed;
	}
	
	public final boolean isSelectedMovement() {
		return this.selectedMovement;
	}

	public final void setSelectedMovement(final boolean selectedMovement) {
		this.selectedMovement = selectedMovement;
	}

	public final int getCountDisable() {
		return this.countDisable;
	}

	public void setCountDisable(final int countDisable) {
		this.countDisable = countDisable;
	}

	public final int getCountCompanyNotExpiry() {
		return this.countCompanyNotExpiry;
	}

	public final void setCountCompanyNotExpiry(final int countCompanyNotExpiry) {
		this.countCompanyNotExpiry = countCompanyNotExpiry;
	}
	
	public final int getCountDisableExecuted() {
		return this.countDisableExecuted;
	}

	public void setCountDisableExecuted(final int countDisableExecuted) {
		this.countDisableExecuted = countDisableExecuted;
	}
	
	public final int getCountNegativeExecuted() {
		return this.countNegativeExecuted;
	}

	public final void setCountNegativeExecuted(final int countNegativeExecuted) {
		this.countNegativeExecuted = countNegativeExecuted;
	}
}
package com.manager.systems.web.financial.cash.flow.grouping.dto;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.manager.systems.common.utils.ConstantDataManager;
import com.manager.systems.common.utils.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CashFlowFinancialGroupingFilterDTO implements Serializable {
	
	private static final long serialVersionUID = -7671887524770557915L;
	
	private int groupingType; //0 - Day - 1 - Week, 2 - Month
	private int subGroupingType; //0 - Total, 1 - Operator, - 2 - Company
	private int reportType; //0 - Previsto e Realizado, 1 - Previsto, 2 - Realizado
	private LocalDateTime dateFrom;
	private LocalDateTime dateTo;
	private String usersChildrenParent;
	private long companyId;
	private String bankAccountIds;
	private boolean analitic;
	private List<Integer> typeDocuments;
	
	public final Map<Long, String> getKeys() {
		
		final WeekFields weekFields = WeekFields.of(DayOfWeek.SUNDAY, 1); 

	    // apply weekOfWeekBasedYear() 
	    final TemporalField weekOfWeekBasedYear = weekFields.weekOfWeekBasedYear(); 
		
		final List<LocalDate> days =  StringUtils.getDatesBetweenUsingJava8(this.dateFrom.toLocalDate(), this.dateTo.toLocalDate().plusDays(1));
		final List<String> weeks = days.parallelStream().map(x -> x.getDayOfWeek().getValue() == 7 ? (String.valueOf(x.plusDays(-1).getYear()) + StringUtils.padLeft(String.valueOf(x.plusDays(-1).get(weekOfWeekBasedYear)), 2, "0")) : (String.valueOf(x.getYear()) + StringUtils.padLeft(String.valueOf(x.get(weekOfWeekBasedYear)), 2, "0")) ).distinct().collect(Collectors.toList()); 
		final List<String> months = days.parallelStream().map(x -> String.valueOf(x.getYear()) +  StringUtils.padLeft(String.valueOf(x.getMonthValue()), 2, ConstantDataManager.ZERO_STRING)).distinct().collect(Collectors.toList()); 	    
		
		Map<Long, String> result = null;
		if(this.groupingType == 0) {
			result = days.parallelStream().collect(Collectors.toMap(x -> Long.valueOf(StringUtils.formatDate(x, StringUtils.DATE_PATTERN_YYYYMMDD)), x -> StringUtils.formatDate(x, StringUtils.DATE_PATTERN_DD_MM_YYYY)));
		} else if(this.groupingType == 1) {
			result = weeks.parallelStream().collect(Collectors.toMap(x -> Long.valueOf(x), x -> this.formatWeekYear(x)));
		} else if(this.groupingType == 2) {
			result = months.parallelStream().collect(Collectors.toMap(x -> Long.valueOf(x), x -> this.formatMonthYear(String.valueOf(x))));
		}
				
		return new TreeMap<>(result);
	}
	
	private String formatWeekYear(final String weekYear) {
		return weekYear.substring(4, 6) + ConstantDataManager.BARRA + weekYear.substring(0, 4);
	}
	
	private String formatMonthYear(final String monthYear) {
		String monthLabel = "";
		switch (monthYear.substring(4, 6)) {
		case "01":
			monthLabel = "Jan";
			break;
		case "02":
			monthLabel = "Fev";
			break;
		case "03":
			monthLabel = "Mar";
			break;
		case "04":
			monthLabel = "Abr";
			break;
		case "05":
			monthLabel = "Mai";
			break;
		case "06":
			monthLabel = "Jun";
			break;
		case "07":
			monthLabel = "Jul";
			break;
		case "08":
			monthLabel = "Ago";
			break;
		case "09":
			monthLabel = "Set";
			break;
		case "10":
			monthLabel = "Out";
			break;
		case "11":
			monthLabel = "Nov";
			break;
		case "12":
			monthLabel = "Dez";
			break;
		default:
			break;
		}
		return monthLabel + ConstantDataManager.BARRA + monthYear.substring(0, 4);
	}
}
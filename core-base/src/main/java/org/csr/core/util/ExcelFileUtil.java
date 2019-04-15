package org.csr.core.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.csr.core.exception.Exceptions;

public class ExcelFileUtil {
	public static ArrayList<ArrayList<String>> parseSheet(Sheet sheet) {
		if (ObjUtil.isEmpty(sheet)) {
			return null;
		}
		ArrayList<ArrayList<String>> Row = new ArrayList<ArrayList<String>>();

		int maxLeng = 1;
		// 循环行Row
		for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {

			Row row = sheet.getRow(rowNum);
			if (row == null) {
				continue;
			}
			int cellNum = row.getLastCellNum();
			if (rowNum == 1) {
				maxLeng = row.getLastCellNum();
			}
			if (cellNum > maxLeng) {
				cellNum = maxLeng;
			}
			// 循环列Cell
			ArrayList<String> arrCell = new ArrayList<String>();
			for (int cellNumIndex = 0; cellNumIndex < cellNum; cellNumIndex++) {
				Cell cell = row.getCell(cellNumIndex);
				if (cell == null) {
					arrCell.add("");
					continue;
				}
				if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
					arrCell.add(String.valueOf(cell.getBooleanCellValue()));
				} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					arrCell.add(String.valueOf(cell.getNumericCellValue()));
				} else {
					arrCell.add(String.valueOf(cell.getStringCellValue()));
				}
			}
			if (rowNum > 1 && row.getLastCellNum() < maxLeng) {
				for (int b = 0; b < maxLeng - row.getLastCellNum(); b++) {
					arrCell.add("");
				}
			}
			Row.add(arrCell);
		}
		return Row;
	}
	/**
	 * 获取对应的Excel的文件标题
	 * 
	 * @author caijin
	 * @param datas
	 * @param columnName
	 * @return
	 * @since JDK 1.7
	 */
	public static List<Integer> getExcelIndex(ArrayList<ArrayList<String>> datas, String columnName) {
		if (ObjUtil.isEmpty(datas) || datas.size() < 1) {
			Exceptions.service("", "您的文件，标题["+columnName+"]不存在");
		}
		List<Integer> indexs = new ArrayList<Integer>();
		ArrayList<String> titleData = datas.get(1);
		for (int i = 0; i < titleData.size(); i++) {
			if (columnName.equals(titleData.get(i))) {
				indexs.add(i);
			}
		}
		if (indexs.size() <= 0) {
			Exceptions.service("", columnName + "列表，不存在");
		}
		return indexs;
	}
}

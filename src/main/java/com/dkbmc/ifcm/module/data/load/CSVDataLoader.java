package com.dkbmc.ifcm.module.data.load;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dkbmc.ifcm.module.core.ModuleConst;
import com.dkbmc.ifcm.module.data.convert.DataConverter;

public class CSVDataLoader implements IDataLoader {
	private String header;
	private ArrayList<String> dataEntries;
	private DataConverter converter;
	private List<Object> conversionEntries;

	public CSVDataLoader(String header_entry, ArrayList<String> data_entries, DataConverter data_converter) {
		header = header_entry;
		dataEntries = data_entries;
		converter = data_converter;
	}

	@Override
	public List<Object> getConversionEntries(){
		return conversionEntries;
	}

	@Override
	public void loadData() throws IllegalArgumentException, SQLException, IOException {
		// TODO Auto-generated method stub
		conversionEntries = new ArrayList<>();

		String[] headerItems = header.split(ModuleConst.MODULE_COMMA_REGEX);

		for (String dataLine : dataEntries) {
			String[] lineItems = parseData(dataLine);
			if(headerItems.length == lineItems.length) {
				Map<String, String> fieldDataMap = getFieldDataMap(headerItems, lineItems);
				Map<String, Object> conversionFieldDataMap = convertData(fieldDataMap);
				if(conversionFieldDataMap != null) conversionEntries.add(conversionFieldDataMap);
			} else {
				// log 기록 필요.
				continue;
			}
		}
		// memory release
		dataEntries.clear();
	}

	// csv parsing
	private String[] parseData(String data_line) {
		String[] lineItmes = data_line.toString().split(LoaderConst.CSV_DATA_REGEX, LoaderConst.CSV_SPLIT_LIMIT);
		return lineItmes;
	}

	@Override
	public Map<String, Object> convertData(Map<String, String> field_data_map) throws IllegalArgumentException, SQLException, IOException {
		// TODO Auto-generated method stub
		return converter.convertRecord(field_data_map);
	}

	// mapping item by header
	private Map<String, String> getFieldDataMap(String[] header_items, String[] line_items) {
		Map<String, String> fieldDataMap = new HashMap<>();
		for(int index=ModuleConst.MODULE_DIGIT_0; index < header_items.length; index++) {
			fieldDataMap.put(header_items[index].toString(), line_items[index].toString());
		}
		return fieldDataMap;
	}

	@Override
	public void parseData() {
		// TODO Auto-generated method stub
	}


}

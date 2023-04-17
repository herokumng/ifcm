package com.dkbmc.ifcm.module.data.load;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IDataLoader {
	public abstract void loadData() throws IllegalArgumentException, SQLException, IOException;
	public abstract void parseData();
	public abstract List<Object> getConversionEntries();
	public abstract Map<String, Object> convertData(Map<String, String> field_data_map) throws IllegalArgumentException, SQLException, IOException;
}

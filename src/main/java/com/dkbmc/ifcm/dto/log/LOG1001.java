
package com.dkbmc.ifcm.dto.log;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LOG1001 {
	private String id;
	private String name;
	private String path;

	@Override
	public String toString() {
		return String.format("%s\t%s\t%s",
				getId(), getName(), getPath());
	}
}
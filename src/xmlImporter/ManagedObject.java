package xmlImporter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ManagedObject extends HashMap{
	private Map<String, String> parameters;
	private String moName;

	public ManagedObject(){
	}

	public ManagedObject(String moName, Map<String, String> parameters){
		this.moName = moName;
		this.parameters = parameters;

	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public String getMoName() {
		return moName;
	}

	public void setMoName(String paramName) {
		this.moName = paramName;
	}

}

package base;

import java.io.File;
import java.io.IOException;

import com.jayway.jsonpath.JsonPath;

public class JSONReader {
	
	public File jsonFile;
	public String fileName;
	
	public JSONReader(String fileName) {
		// TODO Auto-generated constructor stub
		this.fileName=fileName;
	}

	public String getLocator(String locator) throws IOException {
		jsonFile=new File(fileName);
		return JsonPath.read(jsonFile, "$."+locator);
	}
}

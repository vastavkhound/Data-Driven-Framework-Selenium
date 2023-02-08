package base;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class XMLReader {

	public Document document;
	public String fileName;

	public XMLReader(String fileName) {
		this.fileName = fileName;
	}

	public String getLocator(String locator) throws DocumentException {
		SAXReader saxReader = new SAXReader();
		document = saxReader.read(fileName);
		return document.selectSingleNode("//" + locator.replace(".", "/")).getText();
	}

}

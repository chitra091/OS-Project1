package utdallas.cs5348.project1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BatchParser {
	
	public static Batch buildBatch(File batchFile) throws SAXException, IOException, ParserConfigurationException, ProcessException {

		System.out.println("Parsing the input file: " + batchFile.getName());
		
		FileInputStream fis = new FileInputStream(batchFile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fis);

		Element pnode = doc.getDocumentElement();
		NodeList nodes = pnode.getChildNodes();
		
		Batch batch = new Batch();
		
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			Node node = nodes.item(idx);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element elem = (Element) node;
				batch.addCommand(parseCommand(elem, batch));
			}
		}
		
		return batch;
	}
	
	private static Command parseCommand(Element elem, Batch batch) throws ProcessException {
		
		String cmdName = elem.getNodeName();
		Command cmd = null;
		
		if (cmdName == null) {
			throw new ProcessException("Invalid XML element. Does not list a command.");
		} else if ("wd".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing wd");
			cmd = new WDCommand(batch);
			cmd.parse(elem);
		} else if ("file".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing file");
			cmd = new FileCommand(batch);
			cmd.parse(elem);
		} else if ("cmd".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing cmd");
			cmd = new CmdCommand(batch);
			cmd.parse(elem);
		} else if ("pipe".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing pipe");
			cmd  = new PipeCommand(batch);
			cmd.parse(elem);
		} else {
			throw new ProcessException("Invalid XML element. " + cmdName +" is not a registered command.");
		}
		
		return cmd;
	}
}

package com.sand.qa.dataprovider;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlDataProvider extends DataProviderImpl {

	private static final String CASENODETAG = "case";

	private static final String ROWNODETAG = "row";

	private static final String DATANODETAG = "data";

	private static final String TYPETAG = "type";

	private static final String VALUETAG = "value";

	private static final String SPILTTAG = "spilt";

	@Override
	protected void generateData(String caseName, String dataFile) {
		NodeList caseList = getNodeList(dataFile);
		if (caseList.equals(null) || caseList.getLength() == 0) {
			return;
		}
		for (int i = 0; i < caseList.getLength(); i++) {
			Node caseNode = caseList.item(i);
			if (caseNode.getNodeType() == Node.ELEMENT_NODE
					&& caseNode.getNodeName().equals(CASENODETAG)
					&& caseNode.getAttributes().getNamedItem("name")
							.getNodeValue().equals(caseName)) {
				int ii = 1;
				for (Node n = caseNode.getFirstChild(); n != null; n = n
						.getNextSibling()) {
					if (n.getNodeType() == Node.ELEMENT_NODE
							&& n.getNodeName().equals(ROWNODETAG)) {
						if (ii >= start && ii < max) { 
							List<Object> rowdatas = new ArrayList<Object>();
							for (Node m = n.getFirstChild(); m != null; m = m
									.getNextSibling()) {
								if (m.getNodeType() == Node.ELEMENT_NODE
										&& m.getNodeName().equals(DATANODETAG)) {
									rowdatas.add(getRowDataByType(m));
								}
							}
							if (rowdatas.size() > 0)
								data.add(rowdatas.toArray());
							rowdatas.clear();
						}
						ii++;
					}
				}
				break;
			}
		}
	}

	private Object getRowDataByType(Node node) {
		Node type = node.getAttributes().getNamedItem(TYPETAG);
		if (type == null) {
			return node.getAttributes().getNamedItem("value").getNodeValue()
					.toString();
		}
		if (type.getNodeValue().equalsIgnoreCase("int")) {
			return Integer.valueOf(node.getAttributes().getNamedItem(VALUETAG)
					.getNodeValue().toString());
		}
		if (type.getNodeValue().equalsIgnoreCase("double")) {
			return Double.valueOf(node.getAttributes().getNamedItem(VALUETAG)
					.getNodeValue().toString());
		}
		if (type.getNodeValue().equalsIgnoreCase("array")) {

			String spiltString = ",";
			Node spilt = node.getAttributes().getNamedItem(SPILTTAG);
			if (spilt != null)
				spiltString = spilt.getNodeValue().toString();
			return node.getAttributes().getNamedItem(VALUETAG).getNodeValue()
					.toString().split(spiltString);

			/*
			 * MyUser u = new MyUser(); u.setId(1); u.setName("aa"); return u;
			 */
		}
		return node.getAttributes().getNamedItem(VALUETAG).getNodeValue();
	}

	private NodeList getNodeList(String dataFile) {
		DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dombuilder = domfac.newDocumentBuilder();
			InputStream is = new FileInputStream(defaultPathXml+ dataFile);
			Document doc = dombuilder.parse(is);
			Element cases = doc.getDocumentElement();
			return cases.getChildNodes();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}

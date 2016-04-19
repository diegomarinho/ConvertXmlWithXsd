
import java.io.File;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamResult;

import org.apache.xerces.xs.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jlibs.xml.sax.XMLDocument;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jlibs.xml.xsd.XSInstance;
import jlibs.xml.xsd.XSParser;

public class Test {

	public static void main(String[] pArgs) {

		String targetNamespace = null;
		Element rootElem = null;
		Document doc = null;

		try {

			String filename = "nfse_v201.xsd";
			// instance.

			doc = loadXsdDocument(filename);

			// Find the docs root element and use it to find the targetNamespace
			rootElem = doc.getDocumentElement();

			if (rootElem != null && rootElem.getNodeName().equals("xsd:schema")) {
				targetNamespace = rootElem.getAttribute("targetNamespace");
			}

			// Parse the file into an XSModel object
			XSModel xsModel = new XSParser().parse(filename);

			// Define defaults for the XML generation
			XSInstance instance = new XSInstance();
			instance.minimumElementsGenerated = 1;
			instance.maximumElementsGenerated = 1;
			instance.generateDefaultAttributes = Boolean.TRUE;
			instance.generateOptionalAttributes = Boolean.TRUE;
			instance.maximumRecursionDepth = 0;
			instance.generateAllChoices = Boolean.TRUE;
			instance.showContentModel = Boolean.TRUE;
			instance.generateOptionalElements = Boolean.TRUE;

			// Build the sample xml doc Replace first param to XMLDoc with a file input stream to write to file
			QName rootElement = new QName(targetNamespace, "EnviarLoteRpsSincronoEnvio");
			XMLDocument sampleXml = new XMLDocument(new StreamResult(System.out), Boolean.TRUE, 4, null);
			instance.generate(xsModel, rootElement, sampleXml);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Document loadXsdDocument(String inputName) {
		
		DocumentBuilder builder = null;
		File inputFile = null;
		final String filename = inputName;
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(Boolean.FALSE);
		factory.setIgnoringElementContentWhitespace(Boolean.TRUE);
		factory.setIgnoringComments(Boolean.TRUE);
		Document doc = null;

		try {
			builder = factory.newDocumentBuilder();
			inputFile = new File(filename);
			doc = builder.parse(inputFile);
		} catch (final Exception e) {
			e.printStackTrace();
			// throw new ContentLoadException(msg);
		}

		return doc;
	}
}
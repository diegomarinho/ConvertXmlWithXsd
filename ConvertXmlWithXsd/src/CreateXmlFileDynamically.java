import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CreateXmlFileDynamically {
	
	public static void main(String args[]) {
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = docFactory.newDocumentBuilder();
			Document document = builder.newDocument();
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter the number of Element for XML files: - \n");
			int num = Integer.parseInt(br.readLine());
			System.out.println("Enter the root Element: \n");
			String rootElement = br.readLine();
			Element element = document.createElement(rootElement);
			document.appendChild(element);
			
			for (int i = 1; i <= num; i++) {
				System.out.println("Enter the Element:- \n");
				String strElement = br.readLine();
				System.out.println("Enter the Data: - \n");
				String strData = br.readLine();
				Element childElement = document.createElement(strElement);
				childElement.appendChild(document.createTextNode(strData));
				element.appendChild(childElement);
			}
			
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(System.out);
			transformer.transform(domSource, streamResult);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
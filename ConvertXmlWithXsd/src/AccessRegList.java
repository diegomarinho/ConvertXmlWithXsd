/* AccessRegList: A command line application to manipulate the contents
   of a course registration list represented as an XML document.

   Version: A REDUCED DEMO VERSION

   FILES: 
	- name of the registration list file is given as a command line argument
	- reglist.dtd: DTD for the registration list

   P. Kilpeläinen, University of Kuopio, 2003 - 2005

 */

import java.io.*;

// JAXP packages:
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.w3c.dom.*;

public class AccessRegList {

	static final String appName = "AccessRegList";
	static final String version = "Reduced DEMO version 0.3";

	static BufferedReader terminalReader;

	/* AUXILIARY METHODS: */

	private static void usage(String appName) {
		System.err.println("Usage: " + appName + " [options] <filename>");
		System.err.println("       -v = validation");
		System.exit(1);
	}

	private static void showCommands() {

		System.out.println("?: \t show commands (this message) ");
		System.out.println("a: \t add records ");
		System.out.println("d: \t delete records ");
		System.out.println("e: \t edit exercise data ");
		System.out.println("f: \t find student ");
		System.out.println("h: \t show commands (this message) ");
		System.out.println("l: \t list records ");
		System.out.println("s: \t save in file ");
		System.out.println("q: \t quit ");
		System.out.println("u: \t update ");
		System.out.println(" ");
	} // showCommands()

	private static String strContent(Node node) throws Exception
	// Returns the catenation of string values
	// for Text or CDATASection children of 'node'
	{
		StringBuffer strBuf = new StringBuffer();
		NodeList childNodes = node.getChildNodes();
		Node child;
		for (int i = 0; i < childNodes.getLength(); i++) {
			child = childNodes.item(i);
			if (child.getNodeType() == Node.TEXT_NODE
					|| child.getNodeType() == Node.CDATA_SECTION_NODE) {
				strBuf.append(child.getNodeValue());
			}
		}
		return strBuf.toString();
	} // strContent

	/* COMMAND IMPLEMENTATION METHODS: */

	private static Element newStudent(Document doc, String ID, String fName,
			String lName, String branchAndYear, String email, String group)
	// Create and return a new 'student' element
	// with the given values for its 'id' attribute and its sub-elements
	{
		Element newStudent = doc.createElement("student");
		newStudent.setAttribute("id", ID);

		Element newName = doc.createElement("name");

		Element newGiven = doc.createElement("given");
		newGiven.appendChild(doc.createTextNode(fName));
		Element newFamily = doc.createElement("family");
		newFamily.appendChild(doc.createTextNode(lName));
		newName.appendChild(newGiven);
		newName.appendChild(newFamily);

		newStudent.appendChild(newName);

		Element newBranch = doc.createElement("branchAndYear");
		newBranch.appendChild(doc.createTextNode(branchAndYear));
		newStudent.appendChild(newBranch);

		Element newEmail = doc.createElement("email");
		newEmail.appendChild(doc.createTextNode(email));
		newStudent.appendChild(newEmail);

		Element newGroup = doc.createElement("group");
		newGroup.appendChild(doc.createTextNode(group));
		newStudent.appendChild(newGroup);

		return newStudent;

	} // newStudent

	private static void addRecords(Document doc) throws Exception {

		System.out.println("Starting to add records");

		Element rootElem = doc.getDocumentElement();
		String lastID = rootElem.getAttribute("lastID");
		String courseID = rootElem.getAttribute("courseID");
		int lastIDnum = java.lang.Integer.parseInt(lastID);

		String ID, firstName, lastName, branchAndYear, email, group;

		System.out.print("First name (or <return> to finish): ");
		firstName = terminalReader.readLine().trim();

		while (firstName.length() > 0) {

			ID = courseID + "_" + new Integer(++lastIDnum).toString();

			System.out.print("Last name: ");
			lastName = terminalReader.readLine().trim();

			System.out.print("Branch&year: ");
			branchAndYear = terminalReader.readLine().trim();

			System.out.print("email: ");
			email = terminalReader.readLine().trim();

			System.out.print("group: ");
			group = terminalReader.readLine().trim();

			// Create and append a new student element:
			Element newStudent = newStudent(doc, ID, firstName, lastName,
					branchAndYear, email, group);
			rootElem.appendChild(newStudent);

			System.out.print("First name (or <return> to finish): ");
			firstName = terminalReader.readLine().trim();

		} // while firstName.length() > 0

		String newLastID = java.lang.Integer.toString(lastIDnum);
		rootElem.setAttribute("lastID", newLastID);
		System.out.println("Finished adding records");
	}

	private static void deleteRecords(Document doc) throws Exception {

		System.out.println("Starting to delete records");

		Element rootElem = doc.getDocumentElement();
		String lastID = rootElem.getAttribute("lastID");
		String courseID = rootElem.getAttribute("courseID");
		String ID, IDnum;

		System.out.print("Give ID (1 .. " + lastID
				+ ") or <return> to finish): ");
		IDnum = terminalReader.readLine().trim();

		while (IDnum.length() > 0) {

			ID = courseID + "_" + IDnum;

			/* Insert the implementation of deletion here: */

			System.out.println("Deleting student " + ID);
			System.out.println("Deletion disabled in " + version);

			System.out.print("Give ID (1 .. " + lastID
					+ ") or <return> to finish): ");
			IDnum = terminalReader.readLine().trim();
		} // while IDnum.length() > 0

		System.out.println("Finished deleting records");

	} // deleteRecords

	private static void editExercs(Document doc) throws Exception {

	} // editExercs

	private static void findStudent(Document doc) throws Exception {

		System.out.print("Give family name (or <return>): ");

		String fName = terminalReader.readLine().trim();

		if (fName.length() > 0) {

			/* Insert the implementation of look-up by family name here: */

			System.out.println("Not implemented in " + version);

		}
		; // if (fName.getLength() > 0)

	} // findStudent

	private static void showStudent(Element currStudent) throws Exception {

		// Collect relevand sub-elements:
		Node given = currStudent.getFirstChild().getFirstChild();
		Node family = given.getNextSibling();
		Node bAndY = currStudent.getFirstChild().getNextSibling();
		Node email = bAndY.getNextSibling();
		Node group = email.getNextSibling();

		// System.out.print(currStudent.getAttribute("id").substring(3));
		System.out.print(currStudent.getAttribute("id"));
		System.out.print(": " + strContent(given));
		// or simply given.getFirstChild().getNodeValue()
		System.out.print(" " + strContent(family));
		System.out.print(", " + strContent(bAndY));
		System.out.print(", " + strContent(email));
		System.out.println(", " + strContent(group));

	} // showStudent

	private static void listRecords(Document doc) throws Exception {
		System.out.println("listing records:");
		NodeList students = doc.getElementsByTagName("student");
		for (int i = 0; i < students.getLength(); i++) {
			showStudent((Element) students.item(i));
		}
	}

	private static void updateField(Document doc, Element student,
			String fieldName) throws Exception
	/*
	 * An auxiliary method of 'update'. Reads a new value from 'terminalReader',
	 * and if it is non-empty, uses it to update the value of sub-element
	 * 'fieldName' of student Element 'student'
	 */
	{

	} // updateField

	private static void update(Document doc) throws Exception {

		Element rootElem = doc.getDocumentElement();
		String lastID = rootElem.getAttribute("lastID");
		String courseID = rootElem.getAttribute("courseID");

		String ID;

		System.out.print("Give student number (1 .. " + lastID + "): ");

		ID = courseID + "_" + terminalReader.readLine().trim();

		/* Insert implementation below: */

		System.out.println("Updating student " + ID);
		System.out.println("Updates disabled in " + version);

	} // update

	private static void saveInFile(Document doc) throws Exception {

		System.out.println("Saving not supported by " + version);
	}

	public static void main(String args[]) throws Exception {

		String filename = null;
		// Parsing flags:
		boolean validation = false;
		boolean ignoreWhitespace = true;
		boolean ignoreComments = true;
		boolean putCDATAIntoText = true;
		boolean expandEntities = true;

		terminalReader = new BufferedReader(new InputStreamReader(System.in,
				System.getProperty("file.encoding")));

		System.out.println(appName + " (" + version + ")");

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-v")) {
				validation = true;
			} else {
				filename = args[i];

				// Must be last arg
				if (i != args.length - 1) {
					usage(appName);
				}
			}
		} // for
		if (filename == null) {
			usage(appName);
		}

		// Step 1: create a DocumentBuilderFactory:
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		// Optional: set various configuration options:
		dbf.setValidating(validation);
		dbf.setIgnoringComments(ignoreComments);
		dbf.setIgnoringElementContentWhitespace(ignoreWhitespace);
		dbf.setCoalescing(putCDATAIntoText);
		dbf.setExpandEntityReferences(expandEntities);

		// Step 2: create a DocumentBuilder using the DocumentBuilderFactory:
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException pce) {
			System.err.println(pce);
			System.exit(1);
		}

		// Set an ErrorHandler before parsing:
		OutputStreamWriter errorWriter = new OutputStreamWriter(System.err);
		db.setErrorHandler(new MyErrorHandler(
				new PrintWriter(errorWriter, true)));

		// Step 3: parse the input file
		Document doc = null;
		try {
			doc = db.parse(new File(filename));
		} catch (SAXException se) {
			System.err.println(se.getMessage());
			System.exit(1);
		} catch (IOException ioe) {
			System.err.println(ioe);
			System.exit(1);
		}

		System.err.println("Document loaded succesfully");

		String cmd; // current command string
		boolean cont = true;

		try { // to read command characters from terminal

			/* MAIN COMMAND READING LOOP: */
			while (cont) {
				System.out.print("> ");

				cmd = terminalReader.readLine();
				if (cmd == null)
					cont = false;
				else if (cmd.length() > 0) {
					if (cmd.equals("a")) {
						addRecords(doc);

					} else if (cmd.equals("d")) {
						deleteRecords(doc);

					} else if (cmd.equals("e")) {
						editExercs(doc);

					} else if (cmd.equals("f")) {
						findStudent(doc);

					} else if (cmd.equals("l")) {
						listRecords(doc);

					} else if (cmd.equals("q")) {
						cont = false;

					} else if (cmd.equals("s")) {
						saveInFile(doc);

					} else if (cmd.equals("u")) {
						update(doc);

					} else
						showCommands();

				}
				; // if (cmd.length() > 0)
			} // while (cont)
		} // of try (to read commands)
		catch (Exception e) {
			e.printStackTrace();
		}
	} // main() method

	// Error handler to report errors and warnings:
	private static class MyErrorHandler implements ErrorHandler {
		/** Error handler output goes here */
		private PrintWriter out;

		MyErrorHandler(PrintWriter out) {
			this.out = out;
		}

		/**
		 * Returns a string describing parse exception details
		 */
		private String getParseExceptionInfo(SAXParseException spe) {
			String systemId = spe.getSystemId();
			if (systemId == null) {
				systemId = "null";
			}
			String info = "URI=" + systemId + " Line=" + spe.getLineNumber()
					+ ": " + spe.getMessage();
			return info;
		}

		// Standard SAX ErrorHandler methods:
		// See SAX documentation for more info.

		public void warning(SAXParseException spe) throws SAXException {
			out.println("Warning: " + getParseExceptionInfo(spe));
		}

		public void error(SAXParseException spe) throws SAXException {
			String message = "Error: " + getParseExceptionInfo(spe);
			throw new SAXException(message);
		}

		public void fatalError(SAXParseException spe) throws SAXException {
			String message = "Fatal Error: " + getParseExceptionInfo(spe);
			throw new SAXException(message);
		}
	} // class MyErrorHandler

} // public class AccessRegList
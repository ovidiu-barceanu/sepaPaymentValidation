package com.example.utils;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.*;
import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

public class XmlParsing {

    IbanValidator ibanValidator = new IbanValidator();
    String xmlFilePath = "src/test/resources/xml/sample.xml";
    private Document document;

    // Constructor to initialize the XML Document
    public XmlParsing() {
        this.document = parseXmlFile(xmlFilePath);
    }

    // Method to parse the XML file
    private Document parseXmlFile(String xmlFilePath) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new File(xmlFilePath));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Utility method to get the value of a child element from a parent element
    private String getElementValue(String parentTag, String childTag) {
        NodeList parentList = document.getElementsByTagName(parentTag);
        if (parentList.getLength() > 0) {
            Element parentElement = (Element) parentList.item(0);
            NodeList childList = parentElement.getElementsByTagName(childTag);
            if (childList.getLength() > 0) {
                return childList.item(0).getTextContent();
            }
        }
        return null;  // Return null if not found
    }

    public double getCtrlSum(){
        // Get CtrlSum from GrpHdr
        String grpHdrCtrlSumStr = getElementValue("GrpHdr", "CtrlSum");
        if (grpHdrCtrlSumStr == null) {
            throw new AssertionError("CtrlSum in GrpHdr is null or missing.");
        }

        // Get CtrlSum from PmtInf
        String pmtInfCtrlSumStr = getElementValue("PmtInf", "CtrlSum");
        if (pmtInfCtrlSumStr == null) {
            throw new AssertionError("CtrlSum in PmtInf is null or missing.");
        }

        // Parse to double and compare
        double grpHdrCtrlSum = Double.parseDouble(grpHdrCtrlSumStr);
        double pmtInfCtrlSum = Double.parseDouble(pmtInfCtrlSumStr);

        // Assert that both control sums are equal
        assertThat("Control sums from GrpHdr and PmtInf should be equal",
                grpHdrCtrlSum, equalTo(pmtInfCtrlSum));

        return pmtInfCtrlSum;
    }

    public double sumUpAllAmountsFromCdtTrfTxInf(){
        // Sum all amounts from CdtTrfTxInf
        NodeList cdtTrfTxInfList = document.getElementsByTagName("CdtTrfTxInf");
        double totalAmount = 0.0;

        for (int i = 0; i < cdtTrfTxInfList.getLength(); i++) {
            // Access each CdtTrfTxInf node
            Element cdtTrfTxInfElement = (Element) cdtTrfTxInfList.item(i);

            // Get InstdAmt element safely
            NodeList instdAmtList = cdtTrfTxInfElement.getElementsByTagName("InstdAmt");
            if (instdAmtList.getLength() > 0) {
                String amountStr = instdAmtList.item(0).getTextContent();
                totalAmount += Double.parseDouble(amountStr);
            }
        }
        return totalAmount;
    }

    public void assertCtrlSumEqualsAllTransactions() {
            assertThat("Debtor amount is equal with all transactions",
                    sumUpAllAmountsFromCdtTrfTxInf(),
                    equalTo(getCtrlSum()));
    }

    // Method to get transaction date and assert it's not in the future
    public void assertTransactionDateNotInFuture() {
        // Get the transaction date from <ReqdExctnDt>
        String transactionDateStr = getElementValue("PmtInf", "ReqdExctnDt");
        if (transactionDateStr == null) {
            throw new AssertionError("Transaction date (ReqdExctnDt) is missing.");
        }

        // Parse the date string to a LocalDate
        LocalDate transactionDate = LocalDate.parse(transactionDateStr, DateTimeFormatter.ISO_LOCAL_DATE);

        // Get today's date
        LocalDate today = LocalDate.now();

        // Use assertThat to check that the transaction date is not in the future
        assertThat("Transaction date should not be in the future",
                transactionDate.isBefore(today) || transactionDate.isEqual(today),
                equalTo(true));
    }


    // Method to validate all IBANs in the XML
    public void validateAllIbans() {
        if (document == null) {
            throw new IllegalArgumentException("The XML document is not parsed correctly.");
        }

        NodeList ibanList = document.getElementsByTagName("IBAN");
        boolean allIbansValid = true; // Flag to track IBAN validity

        for (int i = 0; i < ibanList.getLength(); i++) {
            String iban = ibanList.item(i).getTextContent();
            boolean isValid = ibanValidator.isValidIban(iban);

            // Use Hamcrest to assert that the IBAN is valid
            assertThat("IBAN " + iban + " should be valid", isValid, is(true));

            if (!isValid) {
                allIbansValid = false; // At least one IBAN is invalid
            }
        }

        // Optionally, assert that all IBANs are valid at once
        assertThat("All IBANs should be valid", allIbansValid, is(true));
    }

    public static boolean checkThatDebtorAmountHasAtLeastTwoDigits(double number) {
        // Get the absolute value and convert to string to split
        String[] parts = String.valueOf(Math.abs(number)).split("\\.");

        // Check the part before the decimal (index 0) and ensure it's at least 2 characters long
        assertThat(" Number has at least 2 digits",  parts[0].length() >= 2);
        return parts[0].length() >= 2;
    }

    public static boolean checkThatDebtorAmountHasAtLeastTwoDigitsAfterDecimal(double number) {
        // Get the absolute value and convert to string to split
        String[] parts = String.valueOf(Math.abs(number)).split("\\.");

        // If there's no decimal part or it's too short, return false
        assertThat("Decimal number has at least 2 digits",  parts[1].length() >= 2);
        return parts.length > 1 && parts[1].length() >= 2;
    }

    // Method to validate XML against the XSD schema
    public void validateXmlAgainstSchema(String xsdFilePath) {
        // Initialize SchemaFactory
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            // Load the schema
            Schema schema = schemaFactory.newSchema(new File(xsdFilePath));

            // Create a Validator instance
            Validator validator = schema.newValidator();

            // Validate the XML document against the schema
            validator.validate(new StreamSource(new File(xmlFilePath)));

            // If no exceptions were thrown, the XML is valid
            assertThat("XML is valid against the provided schema.", true, is(true));
        } catch (SAXException e) {
            // Handle validation error
            throw new AssertionError("XML validation failed: " + e.getMessage());
        } catch (IOException e) {
            // Handle IO errors
            throw new AssertionError("Error reading the XML or XSD file: " + e.getMessage());
        }
    }

    public void validateThatXmlCanBeProcessed() {
        XmlParsing xmlParsing = new XmlParsing();
        xmlParsing.validateXmlAgainstSchema("src/test/resources/schemas/pain.001.001.11.xsd");
    }

}

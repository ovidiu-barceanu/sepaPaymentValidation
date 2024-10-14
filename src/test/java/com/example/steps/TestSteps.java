package com.example.steps;
//import com.example.utils.IbanValidator;
//import com.example.utils.SchemaValidator;
//import com.example.utils.XmlParser;
import com.example.utils.XmlParsing;
import io.cucumber.java.en.*;


public class TestSteps {

    XmlParsing xmlParsing = new XmlParsing();
    private boolean isSchemaValid;

    @When("The control sum is not null")
    public void controlSumNotNull(){
        xmlParsing.getCtrlSum();
    }

    @Then("I check that the debtor total amount has at least 2 digits")
    public void checkWholeNumber(){
//        Double total = xmlParsing.getCtrlSum();
        XmlParsing.checkThatDebtorAmountHasAtLeastTwoDigits(xmlParsing.getCtrlSum());
    }

    @Then("I check that the debtor total amount has at least 2 decimal digits")
    public void checkDecimalPart(){
//        Double total = xmlParsing.assertCtrlSumEqualsAllTransactions();
        XmlParsing.checkThatDebtorAmountHasAtLeastTwoDigitsAfterDecimal(xmlParsing.getCtrlSum());
    }

    @Then("I check that the debtor amount is equal with the sum of all credits")
    public void debtorAmountEqualToSumOfAllCredits(){
        xmlParsing.assertCtrlSumEqualsAllTransactions();
    }

    @Then("I check that the transaction date is not in the future")
    public void checkTransactionDateNotInFuture(){
        xmlParsing.assertTransactionDateNotInFuture();
    }

    @Then("I check that all IBANs are valid")
    public void checkIbansAreValid(){
        xmlParsing.validateAllIbans();
    }

    @When("I validate the XML against the ISO 20022 schema")
    public void i_validate_the_XML_against_the_ISO_20022_schema() {
        xmlParsing.validateThatXmlCanBeProcessed();
    }

}

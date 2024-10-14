Feature: XML Validation for ISO 20022 Payment Initiation

  Scenario:random test
   When The control sum is not null

  Scenario: Debtor total amount has at least 2 digits
    Given The control sum is not null
    When I check that the debtor total amount has at least 2 digits

  Scenario: Debtor total amount has at least 2 decimal digits
    Given The control sum is not null
    When I check that the debtor total amount has at least 2 decimal digits

  Scenario: Debtor amount is equal to the sum of all credits
    Given The control sum is not null
    Then I check that the debtor amount is equal with the sum of all credits


  Scenario: Transaction date is not in the future
    Given The control sum is not null
    Then I check that the transaction date is not in the future

  Scenario: IBANs are valid
    Given The control sum is not null
    Then I check that all IBANs are valid

  Scenario: XML can be processed without invalid data
    Given The control sum is not null
    When I validate the XML against the ISO 20022 schema
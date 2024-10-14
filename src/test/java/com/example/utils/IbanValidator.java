package com.example.utils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class IbanValidator {

    // IBAN validation method
    public boolean isValidIban(String iban) {
        // Define IBAN lengths by country (you can extend this map)
        Map<String, Integer> ibanLengths = new HashMap<>();
        ibanLengths.put("DE", 22); // Germany
        ibanLengths.put("DK", 18); // France
        ibanLengths.put("CZ", 24); // Czech Republic

        // Check length
        String countryCode = iban.substring(0, 2);
        if (!ibanLengths.containsKey(countryCode) || iban.length() != ibanLengths.get(countryCode)) {
            return false;  // Invalid length
        }

        // Check format (only alphanumeric)
        if (!iban.matches("[A-Za-z0-9]+")) {
            return false;  // Invalid format
        }

        // Rearranging and checksum calculation
        String rearrangedIban = iban.substring(4) + iban.substring(0, 4).toUpperCase(); // Rearrange
        StringBuilder numericIban = new StringBuilder();

        // Convert letters to numbers
        for (char c : rearrangedIban.toCharArray()) {
            if (Character.isDigit(c)) {
                numericIban.append(c);
            } else {
                numericIban.append((int) c - 55); // A=10, B=11, ..., Z=35
            }
        }

//        // Check if the number is divisible by 97

        // Use BigInteger to check if the number is divisible by 97
        BigInteger ibanNumber = new BigInteger(numericIban.toString());
        return ibanNumber.mod(BigInteger.valueOf(97)).equals(BigInteger.ONE);
    }
}

package com.pfc.service;

import com.pfc.service.domain.IbanValidation;

import java.util.Map;

public class IbanValidatorService {

    private final Map<String, Integer> countryIban;

    public IbanValidatorService(Map<String, Integer> countryIban) {
        this.countryIban = countryIban;
    }

    public IbanValidation isValid(String iban) {
        if (iban == null || iban.isEmpty() || iban.isBlank()) {
            return validationErrorResponse("Empty or blank IBAN is not allowed");
        }

        final String ibanWithoutSpace = iban.replaceAll("\\s+", "");

        if (ibanWithoutSpace.length() < 5) {
            return validationErrorResponse("IBAN is too short");
        }

        final String countryCode = ibanWithoutSpace.substring(0, 2);

        if (!countryIban.containsKey(countryCode)) {
            return validationErrorResponse("Country is not supported");
        }

        final int ibanLength = countryIban.get(countryCode);

        if (ibanWithoutSpace.length() != ibanLength) {
            return validationErrorResponse("IBAN is not of the expected length for the given country");
        }

        return calculateIbanMod(ibanWithoutSpace) == 1
                ? validationSuccessResponse(true, "IBAN is valid")
                : validationSuccessResponse(false, "IBAN is not valid");
    }

    private int calculateIbanMod(String iban) {
        final String ibanFirstPart = iban.substring(0, 4);
        final String ibanRearranged = iban.substring(4) + ibanFirstPart;

        StringBuilder builder = new StringBuilder(9);
        for (int index = 0; index < ibanRearranged.length(); index++) {
            if (builder.length() / 9 == 1) {
                long mod = Long.parseLong(builder.toString()) % 97;
                builder = new StringBuilder(9);
                builder.append(mod);
            }
            builder.append(convertLetterToNumber(ibanRearranged.charAt(index)));
        }
        return Integer.parseInt(builder.toString()) % 97;
    }

    private String convertLetterToNumber(char letter) {
        char upperLetter = Character.toUpperCase(letter);
        if (upperLetter >= 'A' && upperLetter <= 'Z') {
            return String.valueOf(upperLetter - 55);
        }
        return String.valueOf(letter);
    }

    private IbanValidation validationErrorResponse(String message) {
        return new IbanValidation(false, true, message);
    }

    private IbanValidation validationSuccessResponse(boolean valid, String message) {
        return new IbanValidation(valid, false, message);
    }
}

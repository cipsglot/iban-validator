package com.pfc.service;

import com.pfc.service.domain.IbanValidation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class IbanValidatorServiceTest {

    @Test
    @DisplayName("Should return not valid if IBAN is null, empty, or blank")
    void notValidNullEmptyBlank() {
        IbanValidatorService service = new IbanValidatorService(Collections.emptyMap());

        IbanValidation nullValidation = service.isValid(null);
        assertFalse(nullValidation.isValid());
        assertTrue(nullValidation.isValidationError());
        assertEquals("Empty or blank IBAN is not allowed", nullValidation.getMessage());

        IbanValidation emptyValidation = service.isValid("");
        assertFalse(emptyValidation.isValid());
        assertTrue(emptyValidation.isValidationError());
        assertEquals("Empty or blank IBAN is not allowed", emptyValidation.getMessage());

        IbanValidation blankValidation = service.isValid(" ");
        assertFalse(blankValidation.isValid());
        assertTrue(blankValidation.isValidationError());
        assertEquals("Empty or blank IBAN is not allowed", blankValidation.getMessage());
    }

    @Test
    @DisplayName("Should return not valid if IBAN is too short")
    void notValidTooShort() {
        IbanValidatorService service = new IbanValidatorService(Collections.emptyMap());

        IbanValidation shortValidationSpaces = service.isValid("a f h");
        assertFalse(shortValidationSpaces.isValid());
        assertTrue(shortValidationSpaces.isValidationError());
        assertEquals("IBAN is too short", shortValidationSpaces.getMessage());

        IbanValidation shortValidation = service.isValid("afg");
        assertFalse(shortValidation.isValid());
        assertTrue(shortValidation.isValidationError());
        assertEquals("IBAN is too short", shortValidation.getMessage());
    }

    @Test
    @DisplayName("Should return not valid if IBAN's country is not supported")
    void notValidCountryNotSupported() {
        IbanValidatorService service = new IbanValidatorService(Map.of("AS", 21, "SE", 24));

        IbanValidation countryValidation1 = service.isValid("IT123413523");
        assertFalse(countryValidation1.isValid());
        assertTrue(countryValidation1.isValidationError());
        assertEquals("Country is not supported", countryValidation1.getMessage());

        IbanValidation countryValidation2 = service.isValid("DE1234235");
        assertFalse(countryValidation2.isValid());
        assertTrue(countryValidation2.isValidationError());
        assertEquals("Country is not supported", countryValidation2.getMessage());
    }

    @Test
    @DisplayName("Should return not valid if IBAN's length is not as defined per country")
    void notValidCountryLength() {
        IbanValidatorService service = new IbanValidatorService(Map.of("AS", 21, "SE", 24));

        IbanValidation lengthValidation1 = service.isValid("SE123413523");
        assertFalse(lengthValidation1.isValid());
        assertTrue(lengthValidation1.isValidationError());
        assertEquals("IBAN is not of the expected length for the given country", lengthValidation1.getMessage());

        IbanValidation lengthValidation2 = service.isValid("AS1234235");
        assertFalse(lengthValidation2.isValid());
        assertTrue(lengthValidation2.isValidationError());
        assertEquals("IBAN is not of the expected length for the given country", lengthValidation2.getMessage());
    }

    @Test
    @DisplayName("Should return not valid if the IBAN is not valid")
    void notValidIban() {
        IbanValidatorService service = new IbanValidatorService(Map.of("IT", 27, "SE", 24));

        IbanValidation notValid1 = service.isValid("SE8314976738589148951498");
        assertFalse(notValid1.isValid());
        assertFalse(notValid1.isValidationError());
        assertEquals("IBAN is not valid", notValid1.getMessage());

        IbanValidation notValid2 = service.isValid("IT47Q0300203280327722877582");
        assertFalse(notValid2.isValid());
        assertFalse(notValid2.isValidationError());
        assertEquals("IBAN is not valid", notValid2.getMessage());
    }

    @Test
    @DisplayName("Should return valid if the IBAN is valid")
    void validIban() {
        IbanValidatorService service = new IbanValidatorService(Map.of("IT", 27, "SE", 24));

        IbanValidation valid1 = service.isValid("SE1827417766875674442214");
        assertTrue(valid1.isValid());
        assertFalse(valid1.isValidationError());
        assertEquals("IBAN is valid", valid1.getMessage());

        IbanValidation valid2 = service.isValid("IT47Q0300203280327722877482");
        assertTrue(valid2.isValid());
        assertFalse(valid2.isValidationError());
        assertEquals("IBAN is valid", valid2.getMessage());
    }

}
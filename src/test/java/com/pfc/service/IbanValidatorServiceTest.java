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
        assertEquals("Empty or blank IBAN is not allowed", nullValidation.getMessage());

        IbanValidation emptyValidation = service.isValid("");
        assertFalse(emptyValidation.isValid());
        assertEquals("Empty or blank IBAN is not allowed", emptyValidation.getMessage());

        IbanValidation blankValidation = service.isValid(" ");
        assertFalse(blankValidation.isValid());
        assertEquals("Empty or blank IBAN is not allowed", blankValidation.getMessage());
    }

    @Test
    @DisplayName("Should return not valid if IBAN is too short")
    void notValidTooShort() {
        IbanValidatorService service = new IbanValidatorService(Collections.emptyMap());

        IbanValidation nullValidation = service.isValid("a f h");
        assertFalse(nullValidation.isValid());
        assertEquals("IBAN is too short", nullValidation.getMessage());

        IbanValidation emptyValidation = service.isValid("afg");
        assertFalse(emptyValidation.isValid());
        assertEquals("IBAN is too short", emptyValidation.getMessage());
    }

    @Test
    @DisplayName("Should return not valid if IBAN's country is not supported")
    void notValidCountryNotSupported() {
        IbanValidatorService service = new IbanValidatorService(Map.of("AS", 21, "SE", 24));

        IbanValidation nullValidation = service.isValid("IT123413523");
        assertFalse(nullValidation.isValid());
        assertEquals("Country is not supported", nullValidation.getMessage());

        IbanValidation emptyValidation = service.isValid("DE1234235");
        assertFalse(emptyValidation.isValid());
        assertEquals("Country is not supported", emptyValidation.getMessage());
    }

    @Test
    @DisplayName("Should return not valid if IBAN's length is not as defined per country")
    void notValidCountryLength() {
        IbanValidatorService service = new IbanValidatorService(Map.of("AS", 21, "SE", 24));

        IbanValidation nullValidation = service.isValid("SE123413523");
        assertFalse(nullValidation.isValid());
        assertEquals("IBAN is not of the expected length for the given country", nullValidation.getMessage());

        IbanValidation emptyValidation = service.isValid("AS1234235");
        assertFalse(emptyValidation.isValid());
        assertEquals("IBAN is not of the expected length for the given country", emptyValidation.getMessage());
    }

    @Test
    @DisplayName("Should return not valid if the IBAN is not valid")
    void notValidIban() {
        IbanValidatorService service = new IbanValidatorService(Map.of("IT", 27, "SE", 24));

        IbanValidation nullValidation = service.isValid("SE8314976738589148951498");
        assertFalse(nullValidation.isValid());
        assertEquals("IBAN is not valid", nullValidation.getMessage());

        IbanValidation emptyValidation = service.isValid("IT47Q0300203280327722877582");
        assertFalse(emptyValidation.isValid());
        assertEquals("IBAN is not valid", emptyValidation.getMessage());
    }

    @Test
    @DisplayName("Should return valid if the IBAN is valid")
    void validIban() {
        IbanValidatorService service = new IbanValidatorService(Map.of("IT", 27, "SE", 24));

        IbanValidation nullValidation = service.isValid("SE1827417766875674442214");
        assertTrue(nullValidation.isValid());
        assertEquals("IBAN is valid", nullValidation.getMessage());

        IbanValidation emptyValidation = service.isValid("IT47Q0300203280327722877482");
        assertTrue(emptyValidation.isValid());
        assertEquals("IBAN is valid", emptyValidation.getMessage());
    }

}
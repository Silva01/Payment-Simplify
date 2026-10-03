package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.user;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.WalletTypeEnum;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidationWithAllFieldsValid() {
        UserRequest request = new UserRequest("João Silva", "12345678900", "joao@example.com", "password123", WalletTypeEnum.COMUM);
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Expected no violations for valid UserRequest");
    }

    @Test
    void shouldFailValidationWhenNameIsNull() {
        UserRequest request = new UserRequest(null, "12345678900", "joao@example.com", "password123", WalletTypeEnum.COMUM);
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Name is required", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenNameIsEmpty() {
        UserRequest request = new UserRequest("", "12345678900", "joao@example.com", "password123", WalletTypeEnum.COMUM);
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Name is required", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenNameIsBlank() {
        UserRequest request = new UserRequest("   ", "12345678900", "joao@example.com", "password123", WalletTypeEnum.COMUM);
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Name is required", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenCpfIsNull() {
        UserRequest request = new UserRequest("João Silva", null, "joao@example.com", "password123", WalletTypeEnum.COMUM);
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("CPF is required", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenCpfIsEmpty() {
        UserRequest request = new UserRequest("João Silva", "", "joao@example.com", "password123", WalletTypeEnum.COMUM);
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("CPF is required", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenCpfIsBlank() {
        UserRequest request = new UserRequest("João Silva", "   ", "joao@example.com", "password123", WalletTypeEnum.COMUM);
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("CPF is required", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenEmailIsNull() {
        UserRequest request = new UserRequest("João Silva", "12345678900", null, "password123", WalletTypeEnum.COMUM);
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Email is required", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenEmailIsEmpty() {
        UserRequest request = new UserRequest("João Silva", "12345678900", "", "password123", WalletTypeEnum.COMUM);
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Email is required", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenEmailIsBlank() {
        UserRequest request = new UserRequest("João Silva", "12345678900", "   ", "password123", WalletTypeEnum.COMUM);
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Email is required", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenPasswordIsNull() {
        UserRequest request = new UserRequest("João Silva", "12345678900", "joao@example.com", null, WalletTypeEnum.COMUM);
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Password is required", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenPasswordIsEmpty() {
        UserRequest request = new UserRequest("João Silva", "12345678900", "joao@example.com", "", WalletTypeEnum.COMUM);
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Password is required", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenPasswordIsBlank() {
        UserRequest request = new UserRequest("João Silva", "12345678900", "joao@example.com", "   ", WalletTypeEnum.COMUM);
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Password is required", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenTypeIsNull() {
        UserRequest request = new UserRequest("João Silva", "12345678900", "joao@example.com", "password123", null);
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Type is required", violations.iterator().next().getMessage());
    }
}

package validator;

import com.tenniscorp.validators.TennisGameValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

class TennisGameValidatorTest {

    private final TennisGameValidator validator = new TennisGameValidator();


    private String generateSequence(char c, int length) {
        char[] chars = new char[length];
        Arrays.fill(chars, c);
        return new String(chars);
        // en Java 11+ tu pourrais faire : return String.valueOf(c).repeat(length);
    }


    @Test
    @DisplayName("Dois accepter les chaines de caractères contenant uniquement A et B")
    void validate_shouldAcceptSequenceWithAandB() {
        String input = "ABABABBA";

        assertThatNoException()
                .isThrownBy(() -> validator.validate(input));
    }

    @Test
    @DisplayName("Accepte les séquence ayant au plus 500 caractères A et B")
    void validate_shouldAcceptMaxLengthSequence() {
        String input = generateSequence('A', 500);

        assertThatNoException()
                .isThrownBy(() -> validator.validate(input));
    }

    @Test
    void validate_shouldThrowIllegalArgumentException_whenInputIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> validator.validate(null))
                .withMessageContaining("ne peut pas être vide");
    }

    @Test
    void validate_shouldThrowIllegalArgumentException_whenInputIsEmpty() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> validator.validate(""))
                .withMessageContaining("ne peut pas être vide");
    }

    @Test
    void validate_shouldThrowIllegalArgumentException_whenContainsInvalidCharacter() {
        String input = "ABXCAB";

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> validator.validate(input))
                .withMessageContaining("Format Invalide");
    }

    @Test
    void validate_shouldThrowIllegalArgumentException_whenContainsLowercaseLetters() {
        String input = "AbAB";

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> validator.validate(input))
                .withMessageContaining("Format Invalide");
    }

    @Test
    void validate_shouldThrowSecurityException_whenInputTooLong() {
        String input = generateSequence('A', 501); // > 500

        assertThatExceptionOfType(SecurityException.class)
                .isThrownBy(() -> validator.validate(input))
                .withMessageContaining("Overflow");
    }
}


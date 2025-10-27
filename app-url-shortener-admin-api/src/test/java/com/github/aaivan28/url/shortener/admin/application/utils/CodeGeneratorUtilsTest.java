package com.github.aaivan28.url.shortener.admin.application.utils;

import com.github.aaivan28.url.shortener.admin.application.exception.CodeGenerationException;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CodeGeneratorUtilsTest {

    @Test
    void generateUniqueCode_should_return_code() {
        // Given
        Predicate<String> nonShouldRetry = code -> false;

        // When
        final String actual = CodeGeneratorUtils.generateUniqueCode(nonShouldRetry);

        // Then
        BDDAssertions.then(actual)
                .isNotNull()
                .hasSize(10);
    }

    @Test
    void generateUniqueCode_should_throw_exception_when_all_attempts_are_consumed() {
        // Given
        Predicate<String> shouldRetry = code -> true;

        // When
        final RuntimeException actual = assertThrows(CodeGenerationException.class, () -> CodeGeneratorUtils.generateUniqueCode(shouldRetry));

        // Then
        BDDAssertions.then(actual)
                .isInstanceOf(CodeGenerationException.class)
                .hasMessage("Failed to generate unique code after 10 attempts.");

    }

}
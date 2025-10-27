package com.github.aaivan28.url.shortener.admin.application.utils;

import com.github.aaivan28.url.shortener.admin.application.exception.CodeGenerationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CodeGeneratorUtils {

    private static final int CODE_LENGTH = 10;
    private static final int DISTINCT_RETRY_LIMIT = 10;

    public static String generateUniqueCode(final @NonNull Predicate<String> shouldRetry) {
        return generateWithRetry(CodeGeneratorUtils::generateCode, shouldRetry, 0);
    }

    private static String generateCode() {
        final UUID solutionToken = new UUID(UUID.randomUUID().getMostSignificantBits(), UUID.randomUUID().getLeastSignificantBits());
        return StringUtils.right(solutionToken.toString().replace("-", StringUtils.EMPTY), CODE_LENGTH);
    }

    private static <T> T generateWithRetry(final Supplier<T> supplier, Predicate<T> shouldRetry, int currentTry) {
        log.debug("Generating code with retry: {}", currentTry);
        if (currentTry > DISTINCT_RETRY_LIMIT) {
            throw new CodeGenerationException(DISTINCT_RETRY_LIMIT);
        }
        final T tokens = supplier.get();
        return shouldRetry.test(tokens) ? generateWithRetry(supplier, shouldRetry, currentTry + 1) : tokens;
    }
}

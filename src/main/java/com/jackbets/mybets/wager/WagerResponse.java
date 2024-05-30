package com.jackbets.mybets.wager;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

import com.jackbets.mybets.category.Category;
import com.jackbets.mybets.status.Status;

public record WagerResponse(
    String betTitle,
    Long id,
    BigDecimal units,
    Status status,
    int odds,
    Instant timePlaced,
    BigDecimal toWin,
    Category category) {

        public WagerResponse {
            Objects.requireNonNull(betTitle);
            Objects.requireNonNull(id);
            Objects.requireNonNull(units);
            Objects.requireNonNull(status);
            Objects.requireNonNull(odds);
            Objects.requireNonNull(timePlaced);
            Objects.requireNonNull(toWin);
            Objects.requireNonNull(category);
        }
    
}

package com.jackbets.mybets.wager;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

import com.jackbets.mybets.category.Category;
import com.jackbets.mybets.status.Status;

import lombok.Builder;

@Builder
public record WagerResponse(
    Long id,
    String betTitle,
    BigDecimal units,
    int odds,
    Status status,
    Instant timePlaced,
    BigDecimal toWin,
    Category category) {

        public WagerResponse {
            Objects.requireNonNull(id);
            Objects.requireNonNull(betTitle);
            Objects.requireNonNull(units);
            Objects.requireNonNull(odds);
            Objects.requireNonNull(status);
            Objects.requireNonNull(timePlaced);
            Objects.requireNonNull(toWin);
            Objects.requireNonNull(category);
        }
    
}

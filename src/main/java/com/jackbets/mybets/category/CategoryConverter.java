package com.jackbets.mybets.category;

import java.util.stream.Stream;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CategoryConverter implements AttributeConverter<Category, String>{

    @Override
    public String convertToDatabaseColumn(Category category) {
        if (category == null) {
            return null;
        }
        return category.getSport();
    }

    @Override
    public Category convertToEntityAttribute(String sport) {
        if (sport == null) {
            return null;
        }

        return Stream.of(Category.values())
        .filter(cat -> cat.getSport().equals(sport))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
    }

}

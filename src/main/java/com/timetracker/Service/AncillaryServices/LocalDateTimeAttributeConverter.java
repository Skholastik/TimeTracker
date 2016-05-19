package com.timetracker.Service.AncillaryServices;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(ZonedDateTime locDateTime) {
        return (locDateTime == null ? null : Timestamp.valueOf(LocalDateTime.from(locDateTime)));
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
        return (sqlTimestamp == null ? null :
                ZonedDateTime.of(sqlTimestamp.toLocalDateTime(), ZoneId.systemDefault()));
    }
}
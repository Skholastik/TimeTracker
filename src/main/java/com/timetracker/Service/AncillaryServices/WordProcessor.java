package com.timetracker.Service.AncillaryServices;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Класс для обработки слов и предложений
 */

public class WordProcessor {

    /**
     * Используется для обработки и подготовки слова для БД.
     * Убирает лишние пробелы между словами,
     * делает только первую букву заглавной
     */

    public static String prepareWordToDB(String word) {
        String formatted = word.trim().toLowerCase().replaceAll("[\\s]{2,}", " ");
        return formatted.substring(0, 1).toUpperCase() + formatted.substring(1);
    }

    /**
     * Проверяет ссылку на наличие строки и строку на отсутвие в ней одних
     * пробелов,а так же пустых мест.Выбрасывает {@link NullPointerException}
     * если ссылка на объект отсутствует, либо же имеются пустые места или
     * пробелы.
     */
    public static String requireNonNullAndEmpty(String word, String message) {
        if (StringUtils.isBlank(word))
            throw new NullPointerException(message);
        return word;
    }

}

package ru.otus.spring.service;

import java.util.Locale;

public interface LocaleService {
    String changeLocale(String localeTag);
    Locale getCurrentLocale();
}

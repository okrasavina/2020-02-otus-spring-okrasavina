package ru.otus.spring.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class LocaleServiceImpl implements LocaleService {
    private final MessageSource messageSource;
    @Getter
    @Setter(value = AccessLevel.PRIVATE)
    private Locale currentLocale = Locale.getDefault();

    @Override
    public String changeLocale(String localeTag) {
        setCurrentLocale(Locale.forLanguageTag(localeTag));
        return messageSource.getMessage("locale.change", null, currentLocale);
    }
}

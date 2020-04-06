package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.spring.config.LocaleConfig;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class LocaleServiceImpl implements LocaleService {
    private final LocaleConfig localeConfig;
    private final MessageSource messageSource;

    @Override
    public String changeLocale(String localeTag) {
        localeConfig.setCurrentLocale(Locale.forLanguageTag(localeTag));
        return messageSource.getMessage("locale.change", null, localeConfig.getCurrentLocale());
    }
}

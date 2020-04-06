package ru.otus.spring.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Data
public class LocaleConfig {

    private Locale currentLocale;

    public LocaleConfig() {
        currentLocale = Locale.getDefault();
    }
}

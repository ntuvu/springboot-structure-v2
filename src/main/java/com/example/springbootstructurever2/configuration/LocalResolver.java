package com.example.springbootstructurever2.configuration;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
@Profile({"dev"})
public class LocalResolver extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {

  @Override
  public Locale resolveLocale(HttpServletRequest request) {
    String languageHeader = request.getHeader("Accept-Language");
    List<Locale> locales = List.of(new Locale("en"), new Locale("fr"));
    return StringUtils.hasLength(languageHeader)
        ? Locale.lookup(Locale.LanguageRange.parse(languageHeader), locales)
        : Locale.getDefault();
  }

  @Bean
  public ResourceBundleMessageSource bundleMessageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages");
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setCacheSeconds(3600);

    return messageSource;
  }
}

package qsol.qsoljecheonweb.config;

//import com.qsol.qsolgwmanagerweb.menu.interceptor.MenuIndexInterceptor;
import qsol.qsoljecheonweb.manager.interceptor.ManagerLoginInterceptor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class BeanConfig {


    @Bean
    public MessageSource messageSource() {
    	final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
    
    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
    
    @Bean 
    public LocaleResolver localeResolver() {
    	SessionLocaleResolver resolver = new SessionLocaleResolver();
    	resolver.setDefaultLocale(Locale.KOREAN);
    	resolver.setLocaleAttributeName("lang");
    	return resolver;
    }
    
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
    	LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }
    

    /*@Bean
    public MenuIndexInterceptor menuIndexInterceptor() {
    	MenuIndexInterceptor menuIndexInterceptor = new MenuIndexInterceptor();
    	return menuIndexInterceptor;
    }*/
    
    
    @Bean
    public ManagerLoginInterceptor userLoginInterceptor() {
    	ManagerLoginInterceptor userLoginInterceptor = new ManagerLoginInterceptor();
    	return userLoginInterceptor;
    }
}

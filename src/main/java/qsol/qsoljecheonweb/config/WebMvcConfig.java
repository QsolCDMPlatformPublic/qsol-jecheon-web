package qsol.qsoljecheonweb.config;

//import com.qsol.qsolgwmanagerweb.menu.interceptor.MenuIndexInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.resource.PathResourceResolver;
import qsol.qsoljecheonweb.manager.interceptor.ManagerLoginInterceptor;

@Configuration
//@EnableWebMvc
@ComponentScan
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired
	private LocaleChangeInterceptor localeChangeInterceptor;
	
/*
	@Autowired
	private MenuIndexInterceptor menuIndexInterceptor;
*/

	@Autowired
	private ManagerLoginInterceptor managerLoginInterceptor;
	
//	@Autowired
//	public void setLocaleChangeInterceptor(LocaleChangeInterceptor localeChangeInterceptor) {
//		this.localeChangeInterceptor = localeChangeInterceptor;
//	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/static/**").addResourceLocations("/resources");
		
		registry
           .addResourceHandler("/js/**")
           .addResourceLocations("/js/")
           .setCachePeriod(3600)
           .resourceChain(true)
//           .addResolver(new GzipResourceResolver())
           .addResolver(new PathResourceResolver());
	}
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor).addPathPatterns("/**");
		/*registry.addInterceptor(menuIndexInterceptor).addPathPatterns("/view/**");*/
		registry.addInterceptor(managerLoginInterceptor)
				.excludePathPatterns("/view/mobileWeb/security/Login", "/api/security/login", "/security/not_registered_device", "/api/security/loginSaveData")
				.addPathPatterns("/view/**", "/api/**", "/");
	}

	/*
	@Bean
	public SpringTemplateEngine templateEngine() {
	    SpringTemplateEngine templateEngine = new SpringTemplateEngine();    
	    templateEngine.addTemplateResolver(new UrlTemplateResolver());
	    return templateEngine;
	}
    */
    


}

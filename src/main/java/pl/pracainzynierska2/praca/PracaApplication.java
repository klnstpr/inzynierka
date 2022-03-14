package pl.pracainzynierska2.praca;


import com.vaadin.flow.server.VaadinServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//import pl.pracainzynierska2.praca.ViewController.DefaultView;
@EnableJpaRepositories

@SpringBootApplication(scanBasePackages={"pl.pracainzynierska2.praca"})
public class PracaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PracaApplication.class, args);
	}

	//zmiana mapowania url Vaadin - domyślnie jest to "/" i koliduje z mapowaniem strony startowej Thymeleaf
	@Bean
	public ServletRegistrationBean frontendServletBean() {
		ServletRegistrationBean bean = new ServletRegistrationBean<>(new VaadinServlet() {
			@Override
			protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
				if (!serveStaticOrWebJarRequest(req, resp)) {
					resp.sendError(404);
				}
			}
		}, "/frontend/*");
		bean.setLoadOnStartup(1);
		return bean;
	}


}
package com.dkbmc.ifcm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig{

	@Bean
	public Docket swaggerAPI() {
		// Docket : swagger Bean
		return new Docket(DocumentationType.OAS_30).useDefaultResponseMessages(true) // 기본 응답 메시지 표시 여부
				.select().apis(RequestHandlerSelectors.basePackage("com.dkbmc.ifcm")) // swagger에서 탐색하는 Package 명
				.paths(PathSelectors.any()).build().apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Interface Common Module Rest API's").description("IFCM Rest API's").version("1.0").build();
	}

}
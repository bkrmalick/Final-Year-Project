package com.bkrmalick.covidtracker.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

/**
 * This config allows a user to directly query the backend
 * (which they might do by querying a page directly as backend and frontend are hosted on the same server/port)
 * and not get a 404 error, it instead redirects them to index.html file for loading
 * appropriate JS files for the client-side routing
 *
 * Credits: https://stackoverflow.com/a/52672246/12567585
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer
{

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry)
	{

		registry.addResourceHandler("/**/*")
				.addResourceLocations("classpath:/static/")
				.resourceChain(true)
				.addResolver(new PathResourceResolver()
				{
					@Override
					protected Resource getResource(String resourcePath, Resource location) throws IOException
					{
						Resource requestedResource = location.createRelative(resourcePath);
						return requestedResource.exists() && requestedResource.isReadable() ? requestedResource : new ClassPathResource("/static/index.html");
					}
				});
	}
}
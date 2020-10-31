package com.bkrmalick.covidtracker.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class CachingConfig
{
	private static final Logger logger = LoggerFactory.getLogger(CachingConfig.class);
	private static final String  questionsCacheName= "formQuestions";
	private static final int cacheRefreshDelay= 24*60*60*1000; //24 hours

	@CacheEvict(allEntries = true, value = {questionsCacheName})
	@Scheduled(fixedDelay = cacheRefreshDelay, initialDelay = cacheRefreshDelay)
	public void reportCacheEvict()
	{
		logger.info("Questions cache flushed according to schedule");
	}
}
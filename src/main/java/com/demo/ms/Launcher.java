package com.demo.ms;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.demo.handler.RoomBookingHandler;

@Configuration
@ComponentScan(basePackages = "com.demo")
@PropertySource("classpath:application.properties")
public class Launcher extends TimerTask {
	private static final Logger LOG = LoggerFactory.getLogger(Launcher.class);


	
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	public static void main(String[] args) {

		Timer timer = new Timer();
		timer.schedule(new Launcher(), 0, 1000 * 60);
		
		//ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		//executor.scheduleAtFixedRate(new App(), 0, 60, TimeUnit.SECONDS);
	}


	@Override
	public void run() {
		LOG.info("*****************************************************");
		LOG.info("***************  Starting Process ... ***************");
		LOG.info("*****************************************************");
		ApplicationContext context = new AnnotationConfigApplicationContext(Launcher.class);
		RoomBookingHandler roomBookingHandler = context.getBean(RoomBookingHandler.class);
		roomBookingHandler.launchRoomBookingProcess();
		
		LOG.info("*****************************************************");
		LOG.info("***************  Finish Process. ***************");
		LOG.info("*****************************************************");
	
	}
	

	
	
}

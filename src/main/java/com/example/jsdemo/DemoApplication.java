package com.example.jsdemo;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import reactor.core.publisher.Flux;

@SpringBootApplication
@Controller
public class DemoApplication {

	@GetMapping(path = "/", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter stream() {
		SseEmitter emitter = new SseEmitter();
		Flux.interval(Duration.ofSeconds(5)).subscribe(
				value -> {
					try {
						String text = render(value);
						// https://github.com/spring-projects/spring-framework/issues/30965
						emitter.send(text.replace("\n", "\ndata:"), MediaType.TEXT_HTML);
					} catch (Exception e) {
						emitter.completeWithError(e);
					}
				}, emitter::completeWithError, emitter::complete);
		;
		return emitter;
	}

	private String render(Long value) {
		return String.format("""
				<div>
					<p>Value: %d</p>
				</div>""", value);
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
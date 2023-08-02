package com.example.jsdemo;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@SpringBootApplication
@RestController
public class DemoApplication {

	@GetMapping(path = "/", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> stream() {
		return Flux.interval(Duration.ofSeconds(5)).map(
				value -> render(value));
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
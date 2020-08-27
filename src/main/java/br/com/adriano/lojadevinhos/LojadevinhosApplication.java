package br.com.adriano.lojadevinhos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootApplication
public class LojadevinhosApplication {

	public static void main(String[] args) {
		SpringApplication.run(LojadevinhosApplication.class, args);
	}

}

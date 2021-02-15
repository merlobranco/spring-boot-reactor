package com.merlobranco.springboot.reactor.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.merlobranco.springboot.reactor.app.models.Usuario;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {
	
	private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flux<Usuario> nombres = Flux.just("Andres", "Pedro", "Maria", "Diego", "Juan")
				.map(nombre -> new Usuario(nombre.toUpperCase(), null))
				.doOnNext(u -> {
					if (u == null) {
						throw new RuntimeException("Nombres no pueden ser vacíos");
					}
					System.out.println(u.getNombre());
				})
				.map(u -> {
					u.setNombre(u.getNombre().toLowerCase());
					return u;
				});
		
		// Subscribing the observer with a task
		// And handling an error
		nombres.subscribe(u -> log.info(u.toString()), 
				error -> log.error(error.getMessage()),
				new Runnable() {

					@Override
					public void run() {
						log.info("Ha finalizado la ejecución del observable con éxito!");
					}
					
				});
		
	}

}

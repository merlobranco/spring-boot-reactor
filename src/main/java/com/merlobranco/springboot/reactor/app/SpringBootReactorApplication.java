package com.merlobranco.springboot.reactor.app;

import java.util.Arrays;
import java.util.List;

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
		
		List<String> usuariosList = Arrays.asList(new String[] {"Andres Guzman", "Pedro Almodovar", "Maria Malta", "Diego Maradona", "Juan Vilches", "Bruce Lee", "Bruce Willis"});
//		Flux<String> nombres = Flux.just("Andres Guzman", "Pedro Almodovar", "Maria Malta", "Diego Maradona", "Juan Vilches", "Bruce Lee", "Bruce Willis");
		Flux<String> nombres = Flux.fromIterable(usuariosList);
		
		Flux<Usuario> usuarios = nombres.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.filter(u -> u.getNombre().equalsIgnoreCase("Bruce"))
				.doOnNext(u -> {
					if (u == null) {
						throw new RuntimeException("Nombres no pueden ser vacíos");
					}
					System.out.println(u.getNombre().concat(" ").concat(u.getApellido()));
				})
				.map(u -> {
					u.setNombre(u.getNombre().toLowerCase());
					return u;
				});
		
		// Subscribing the observer with a task
		// And handling an error
		usuarios.subscribe(u -> log.info(u.toString()), 
				error -> log.error(error.getMessage()),
				new Runnable() {

					@Override
					public void run() {
						log.info("Ha finalizado la ejecución del observable con éxito!");
					}
					
				});
		
	}

}

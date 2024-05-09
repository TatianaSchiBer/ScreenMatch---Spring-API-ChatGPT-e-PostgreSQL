package br.com.alura.ScreenMatchNoWeb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.alura.ScreenMatchNoWeb.principal.Principal;

@SpringBootApplication
public class ScreenMatchNoWebApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchNoWebApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Principal principal = new Principal();		
		
		principal.exibeMenu();

	}

}

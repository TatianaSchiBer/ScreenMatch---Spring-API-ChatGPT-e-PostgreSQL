package br.com.alura.ScreenMatchNoWeb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.alura.ScreenMatchNoWeb.principal.Principal;
import br.com.alura.ScreenMatchNoWeb.repository.SerieRepository;

@SpringBootApplication
public class ScreenMatchNoWebApplication implements CommandLineRunner{
	
	@Autowired
	private SerieRepository repositorio;

	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchNoWebApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Principal principal = new Principal(repositorio);		
		
		principal.exibeMenu();

	}

}

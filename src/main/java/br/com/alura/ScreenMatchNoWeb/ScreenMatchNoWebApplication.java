package br.com.alura.ScreenMatchNoWeb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.alura.ScreenMatchNoWeb.model.DadosEpisodio;
import br.com.alura.ScreenMatchNoWeb.model.DadosSerie;
import br.com.alura.ScreenMatchNoWeb.model.DadosTemporada;
import br.com.alura.ScreenMatchNoWeb.service.ConsumoApi;
import br.com.alura.ScreenMatchNoWeb.service.ConverteDados;

@SpringBootApplication
public class ScreenMatchNoWebApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchNoWebApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		 var consumoApi = new ConsumoApi();
		 
		    var json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=6585022c");
		    
		    System.out.println(json);
		    
		    ConverteDados conversor	= new ConverteDados();
		    DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		    System.out.println(dados);
		    
		 

		    json = consumoApi.obterDados("https://omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=6585022c");
		    DadosEpisodio dadosEpisodio = conversor.obterDados(json, DadosEpisodio.class);
		    System.out.println(dadosEpisodio);
		    
		    List<DadosTemporada> temporadas = new ArrayList<DadosTemporada>();

		    for(int i=1; i<=dados.totalTemporadas(); i++) {
		    	  json = consumoApi.obterDados("https://omdbapi.com/?t=gilmore+girls&season=" + i + "&apikey=6585022c");
		    	  DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
		    	  temporadas.add(dadosTemporada);
		    }
		    temporadas.forEach(System.out::println);
	}

}

package br.com.alura.ScreenMatchNoWeb.principal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.alura.ScreenMatchNoWeb.model.Categoria;
import br.com.alura.ScreenMatchNoWeb.model.DadosSerie;
import br.com.alura.ScreenMatchNoWeb.model.DadosTemporada;
import br.com.alura.ScreenMatchNoWeb.model.Episodio;
import br.com.alura.ScreenMatchNoWeb.model.Serie;
import br.com.alura.ScreenMatchNoWeb.repository.SerieRepository;
import br.com.alura.ScreenMatchNoWeb.service.ConsumoApi;
import br.com.alura.ScreenMatchNoWeb.service.ConverteDados;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private SerieRepository repositorio;
    private List<Serie> series = new ArrayList<Serie>();
    
    public Principal(SerieRepository repositorio) {
    	this.repositorio = repositorio;
		
	}

	public void exibeMenu() {
        var opcao = -1;
        while(opcao != 0) {
            var menu = """
            		
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar Serie por Titulo
                    5 - Buscar series por ator
                    6 - Top 7 series
                    7 - Buscar series por Categoria
                    8 - Buscar series por quantidade de temporada e avaliacao
                                    
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                    
                case 4:
                	buscarSeriePorTitulo();
                	break;
                case 5: 
                	buscarSeriesPorAtor();
                	break;
                case 6:
                	buscarTop7Series();
                	break;
                case 7:
                	buscarSeriesPorCategoria();
                	break;
                case 8:
                	buscarSeriesPorTemporadaEAvaliacao();
                	break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSeriesPorTemporadaEAvaliacao() {
		System.out.println("Voce deseja buscar series com no maximo quantas temporadas: ");
		var maxTemporadas = leitura.nextInt();
		leitura.nextLine();
		System.out.println("Voce deseja que as series buscadas tenha qual avaliaçao minima: ");
		var avaliacao = leitura.nextDouble();
		List<Serie> seriePorTemporadaEAvaliacao = repositorio.findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(maxTemporadas, avaliacao);
		seriePorTemporadaEAvaliacao.forEach(s ->System.out.println ("Titulo: " + s.getTitulo() + " - total temporadas: " + s.getTotalTemporadas() + " - avaliacao " + s.getAvaliacao()));
	}

	private void buscarSeriesPorCategoria() {
		System.out.println("Deseja buscar series por qual categoria/genero? ");
		var nomeGenero = leitura.nextLine();
		Categoria categoria = Categoria.fromportugues(nomeGenero);
		List<Serie> seriePorCategoria = repositorio.findByGenero(categoria);
		System.out.println("Series da categoria " + nomeGenero);
		seriePorCategoria.forEach(System.out::println);
		
	}

	private void buscarTop7Series() {
		List<Serie> seriesTop = repositorio.findTop7ByOrderByAvaliacaoDesc();
		seriesTop.forEach(s -> System.out.println(s.getTitulo() + " avaliacao: " + s.getAvaliacao()));
		
	}

	private void buscarSeriesPorAtor() {
		System.out.println("Digite o nome do ator a ser consultado: ");
		var nomeAtor = leitura.nextLine();
		System.out.println("Avaliacoes a partir de que valor: ");
		var avaliacao = leitura.nextDouble();
		
		List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
		System.out.println("Series em que o ator " + nomeAtor + " atuou: ");
		seriesEncontradas.forEach(s -> System.out.println(s.getTitulo() + " avaliacao: " + s.getAvaliacao()));
		
	}

	private void buscarSeriePorTitulo() {
    	System.out.println("Escolha um série pelo nome: ");
    	var nomeSerie = leitura.nextLine();
    	Optional<Serie> serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);
    	
    	if(serieBuscada.isPresent()) {
    		System.out.println("Dados da serie:" + serieBuscada.get());
    	}else {
    		System.out.println("Serie nao encontrada!");
    	}
		
	}

	private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
   //     dadosSeries.add(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
    	
    	listarSeriesBuscadas();
    	System.out.println("Escolha uma serie pelo nome: ");
    	var nomeSerie = leitura.nextLine();
    	
    	Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);
    	
    	if(serie.isPresent()) {
     
    		var serieEncontrada = serie.get();
    		
        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
            var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);
        
        List<Episodio> episodios = temporadas.stream()
                	.flatMap(d -> d.episodios().stream()
        					.map(e -> new Episodio(d.numero(), e)))
        			.collect(Collectors.toList());
        serieEncontrada.setEpisodios(episodios);
        repositorio.save(serieEncontrada);
        
    }else {
    	System.out.println("Serie nao encontrada!! ");
    }
    }

    private void listarSeriesBuscadas(){
    	
        series = repositorio.findAll();
        
//        series = dadosSeries.stream()
//                .map(d -> new Serie(d))
//                .collect(Collectors.toList());
               
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
}
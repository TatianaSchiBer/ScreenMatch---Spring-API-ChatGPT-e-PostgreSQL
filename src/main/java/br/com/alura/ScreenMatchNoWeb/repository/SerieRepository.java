package br.com.alura.ScreenMatchNoWeb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.alura.ScreenMatchNoWeb.model.Categoria;
import br.com.alura.ScreenMatchNoWeb.model.Episodio;
import br.com.alura.ScreenMatchNoWeb.model.Serie;

public interface SerieRepository extends JpaRepository<Serie, Long> {
	
	Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

	List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacao);

	List<Serie> findTop7ByOrderByAvaliacaoDesc();
	
	List<Serie> findByGenero(Categoria categoria);

	List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(Integer totalTemporadas, Double avaliacao );
	
	// JPQL
	
	@Query("select s from Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacao >= :avaliacao")
	List<Serie> seriesPorTemporadaEAvaliacao(int totalTemporadas, double avaliacao);

	@Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodio%")
	List<Episodio> episodioPorTrecho(String trechoEpisodio);

	@Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
	List<Episodio> topEpisodiosPorSerie(Serie serie);

	@Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
	List<Episodio> episodiosPorSerieEAno(Serie serie, int anoLancamento);

}

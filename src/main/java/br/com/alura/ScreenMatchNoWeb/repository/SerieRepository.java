package br.com.alura.ScreenMatchNoWeb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.ScreenMatchNoWeb.model.Categoria;
import br.com.alura.ScreenMatchNoWeb.model.Serie;

public interface SerieRepository extends JpaRepository<Serie, Long> {
	
	Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

	List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacao);

	List<Serie> findTop7ByOrderByAvaliacaoDesc();
	
	List<Serie> findByGenero(Categoria categoria);

	List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(Integer totalTemporadas, Double avaliacao );

}

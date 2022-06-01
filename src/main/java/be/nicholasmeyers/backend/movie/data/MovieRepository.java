package be.nicholasmeyers.backend.movie.data;

import be.nicholasmeyers.backend.movie.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MovieRepository extends JpaRepository<Movie, UUID> {
}

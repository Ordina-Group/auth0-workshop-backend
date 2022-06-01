package be.nicholasmeyers.backend.movie.service;

import be.nicholasmeyers.backend.movie.data.MovieRepository;
import be.nicholasmeyers.backend.movie.exception.MovieNotFoundException;
import be.nicholasmeyers.backend.movie.model.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovieService {

    private final Logger logger = LoggerFactory.getLogger(MovieService.class);
    private final MovieRepository repository;

    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }

    public Movie findById(UUID id) {
        Optional<Movie> movie = repository.findById(id);
        if (movie.isEmpty()) {
            logger.error("Movie with id {} not found", id);
            throw new MovieNotFoundException();
        }
        return movie.get();
    }

    public List<Movie> findAll() {
        return repository.findAll();
    }

    public Movie create(Movie movie) {
        return repository.save(movie);

    }
}

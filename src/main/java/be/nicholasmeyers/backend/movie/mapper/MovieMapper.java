package be.nicholasmeyers.backend.movie.mapper;

import be.nicholasmeyers.backend.movie.model.Movie;
import be.nicholasmeyers.backend.movie.resource.MovieWebRequestResource;
import be.nicholasmeyers.backend.movie.resource.MovieWebResponseResource;

import java.util.List;

public interface MovieMapper {
    MovieWebResponseResource modelToResource(final Movie movie);

    List<MovieWebResponseResource> modelToResource(final List<Movie> movies);

    Movie resourceToModel(final MovieWebRequestResource resource);

}

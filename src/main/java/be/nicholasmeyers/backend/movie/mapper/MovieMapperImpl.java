package be.nicholasmeyers.backend.movie.mapper;

import be.nicholasmeyers.backend.movie.model.Movie;
import be.nicholasmeyers.backend.movie.resource.MovieWebRequestResource;
import be.nicholasmeyers.backend.movie.resource.MovieWebResponseResource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MovieMapperImpl implements MovieMapper {

    @Override
    public MovieWebResponseResource modelToResource(final Movie movie) {
        MovieWebResponseResource response = new MovieWebResponseResource();
        response.setId(movie.getId());
        response.setName(movie.getName());
        response.setImageUrl(movie.getImageUrl());
        return response;
    }

    @Override
    public List<MovieWebResponseResource> modelToResource(final List<Movie> movies) {
        List<MovieWebResponseResource> response = new ArrayList<>();
        for (Movie movie : movies) {
            response.add(modelToResource(movie));
        }
        return response;
    }

    @Override
    public Movie resourceToModel(final MovieWebRequestResource resource) {
        Movie movie = new Movie();
        movie.setName(resource.getName());
        movie.setImageUrl(resource.getImageUrl());
        return movie;
    }
}

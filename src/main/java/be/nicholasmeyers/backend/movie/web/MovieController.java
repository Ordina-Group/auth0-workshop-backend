package be.nicholasmeyers.backend.movie.web;

import be.nicholasmeyers.backend.movie.mapper.MovieMapper;
import be.nicholasmeyers.backend.movie.resource.MovieWebRequestResource;
import be.nicholasmeyers.backend.movie.resource.MovieWebResponseResource;
import be.nicholasmeyers.backend.movie.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/movie")
@CrossOrigin("http://localhost:4200")
public class MovieController {

    private final MovieMapper mapper;
    private final MovieService service;

    public MovieController(MovieMapper mapper, MovieService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<MovieWebResponseResource>> getAllMovies() {
        return ResponseEntity.ok(mapper.modelToResource(service.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieWebResponseResource> findById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(mapper.modelToResource(service.findById(id)));
    }

    @PostMapping()
    public ResponseEntity<MovieWebResponseResource> create(@RequestBody MovieWebRequestResource resource) {
        MovieWebResponseResource response = mapper.modelToResource(service.create(mapper.resourceToModel(resource)));
        UriComponents uriComponents = UriComponentsBuilder.newInstance().path("/movie/{id}").buildAndExpand(response.getId());
        return ResponseEntity.created(uriComponents.toUri()).body(response);
    }
}

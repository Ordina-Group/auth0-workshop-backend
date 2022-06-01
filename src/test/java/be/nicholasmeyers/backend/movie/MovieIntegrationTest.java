package be.nicholasmeyers.backend.movie;

import be.nicholasmeyers.backend.movie.resource.MovieWebRequestResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class MovieIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .build();
    }

    @Transactional
    @Test
    public void createTicket() throws Exception {
        MovieWebRequestResource requestResource = new MovieWebRequestResource();
        requestResource.setName("MOVIE_NAME");
        requestResource.setImageUrl("https://media.s-bol.com/qP3QmVkB51D/848x1200.jpg");

        mockMvc.perform(post("/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestResource))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, startsWith("/movie/")))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("MOVIE_NAME"))
                .andExpect(jsonPath("$.imageUrl").value("https://media.s-bol.com/qP3QmVkB51D/848x1200.jpg"));
    }

    @Test
    public void findById() throws Exception {
        mockMvc.perform(get("/movie/820053c6-522d-4382-aea4-938287c31501")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("820053c6-522d-4382-aea4-938287c31501"))
                .andExpect(jsonPath("$.name").value("Kampioen Zijn Blijft Plezant!"))
                .andExpect(jsonPath("$.imageUrl").value("https://media.s-bol.com/qP3QmVkB51D/848x1200.jpg"));
    }

    @Test
    public void findAll() throws Exception {
        mockMvc.perform(get("/movie")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("820053c6-522d-4382-aea4-938287c31501"))
                .andExpect(jsonPath("$[0].name").value("Kampioen Zijn Blijft Plezant!"))
                .andExpect(jsonPath("$[0].imageUrl").value("https://media.s-bol.com/qP3QmVkB51D/848x1200.jpg"));
    }
}

package app;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Application.class)
public class BookControllerRequestTests {

  private MockMvc mockMvc;
  private int bookId;

  @SuppressWarnings("SpringJavaAutowiringInspection")
  @Autowired
  protected WebApplicationContext wac;

  @Autowired
  protected BookRepository repository;

  @Before
  public void setup() {
    this.mockMvc = webAppContextSetup(this.wac).build();
    bookId = this.repository.save(new Book("Pro Spring", new Author("Rob", "Harrop"))).getId();
  }

  @Test
  public void listBooks() throws Exception {
    mockMvc.perform(get("/books"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[(@.length-1)].title").value("Pro Spring"))
        .andExpect(jsonPath("$[(@.length-1)].author.firstName").value("Rob"));
  }

  @Test
  public void getBook() throws Exception {
    mockMvc.perform(get("/books/{id}", bookId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Pro Spring"));
  }

  @Test
  public void getUnknownBook() throws Exception {
    mockMvc.perform(get("/books/{id}", 100))
        .andExpect(status().isNotFound());
  }

  @Test @DirtiesContext
  public void deleteBook() throws Exception {
    mockMvc.perform(delete("/books/{id}", 1))
        .andExpect(status().isNoContent());
    assertNull(this.repository.findById(1));
  }

  @Test @DirtiesContext
  public void createBook() throws Exception {
    Book book = new Book("Pro Struts", new Author("Rob", "Harrop"));
    ObjectMapper mapper = new ObjectMapper();
    String content = mapper.writeValueAsString(book);
    mockMvc.perform(post("/books")
        .content(content)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
    assertNotNull(this.repository.findById(2));
  }

  @Test @DirtiesContext
  public void updateBook() throws Exception {
    Book book = new Book("Pro Struts", new Author("Rob", "Harrop"));
    ObjectMapper mapper = new ObjectMapper();
    String content = mapper.writeValueAsString(book);
    mockMvc.perform(put("/books/{id}", 1)
        .content(content)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
    assertEquals("Pro Struts", this.repository.findById(1).getTitle());
  }
}

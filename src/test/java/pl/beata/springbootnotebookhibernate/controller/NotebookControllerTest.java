package pl.beata.springbootnotebookhibernate.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.beata.springbootnotebookhibernate.model.Notebook;
import pl.beata.springbootnotebookhibernate.service.NotebookService;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NotebookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private NotebookService notebookService;

    private static final String TEXT_HTML_UTF8_VALUE = "text/html;charset=UTF-8";


    @Test
    public void should_find_all_notebook_success() throws Exception {
        MultiValueMap paramsMap = new LinkedMultiValueMap();

        paramsMap.add("page", "1");
        paramsMap.add("size", "5");

        when(notebookService.getNotes(any(Pageable.class)));

        mockMvc.perform(get("/").params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_HTML_UTF8_VALUE))
                .andExpect(xpath("(//*[contains(@class, 'card-title')])[1]").string("Piąta"))
                .andExpect(xpath("(//*[contains(@class, 'card-subtitle')])[1]").string("opis piątej"))
                .andExpect(xpath("(//*[contains(@class, 'card-text')])[1]").string("treść piątej notatki"));
    }

    @Test
    public void should_add_note_success() throws Exception {
        Notebook notebook = new Notebook(
                "Szósta", "opis szóstej", "treść szóstej notatki", LocalDateTime.now(), LocalDateTime.now());
        when(notebookService.addNote(notebook));

        mockMvc.perform(post("/add")
                    .param("title", "Szósta")
                    .param("description", "opis szóstej")
                    .param("text", "treść szóstej notatki"))
                .andExpect(redirectedUrl("/"))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("successMessage", "Notatka została poprawnie dodana"));
    }

    @Test
    public void should_not_add_note_when_empty_title_error() throws Exception {
        mockMvc.perform(post("/add")
                .param("title", "")
                .param("description", "opis szostej")
                .param("text", "treść szostej notatki"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_HTML_UTF8_VALUE))
                .andExpect(xpath("(//*[contains(@class, 'invalid-feedback')])[1]").string("Tytuł nie może być pusty!"));
    }

    @Test
    public void should_edit_note_success() throws Exception {
        Long id = 5L;
        Notebook notebook = new Notebook(
                "Piąta", "opis piątej", "treść piątej notatki", LocalDateTime.now(), LocalDateTime.now());
        notebook.setId(id);

        Notebook modifyNotebook = new Notebook(
                "Piąta", "zmodyfikowany opis piątej", "zmodyfikowana treść piątej notatki", LocalDateTime.now(), LocalDateTime.now());
        modifyNotebook.setId(id);

        when(notebookService.editNote(id)).thenReturn(notebook);
        when(notebookService.addNote(modifyNotebook));

        mockMvc.perform(post("/edit/{id}", id)
                .param("title", "Piąta")
                .param("description", "zmodyfikowany opis piątej")
                .param("text", "zmodyfikowana treść piątej notatki")
                .param("created", "2020-05-07T00:40:44.515209700"))
            .andExpect(redirectedUrl("/"))
            .andExpect(status().isFound())
            .andExpect(flash().attribute("successMessage", "Notatka została poprawnie zmodyfikowana"));
    }

    @Test
    public void should_not_edit_note_when_empty_text_error() throws Exception {
        mockMvc.perform(post("/edit/{id}", 5L)
                .param("title", "Piąta")
                .param("description", "zmodyfikowany opis piątej")
                .param("text", "")
                //.param("created", String.valueOf(LocalDateTime.now()))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_HTML_UTF8_VALUE))
                .andExpect(xpath("(//*[contains(@class, 'invalid-feedback')])[1]").string("Treść nie może być pusta!"));
    }

    @Test
    public void should_delete_note_success() throws Exception {
        mockMvc.perform(get("/delete/{id}", 5))
                .andExpect(redirectedUrl("/"))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("successMessage", "Notatka została poprawnie usunięta"));
    }
}

package pl.beata.springbootnotebookhibernate.service;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.beata.springbootnotebookhibernate.model.Notebook;
import pl.beata.springbootnotebookhibernate.repository.NotebookRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class NotebookService {

    private NotebookRepository notebookRepository;

    public NotebookService(NotebookRepository notebookRepository) {
        this.notebookRepository = notebookRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    private void init() {
        notebookRepository.save(new Notebook("Pierwsza", "opis pierwszej", "treść pierwszej notatki", LocalDateTime.now(), LocalDateTime.now()));
        notebookRepository.save(new Notebook("Druga", "opis drugiej", "treść drugiej notatki", LocalDateTime.now(), LocalDateTime.now()));
        notebookRepository.save(new Notebook("Trzecia", "opis trzeciej", "treść trzeciej notatki", LocalDateTime.now(), LocalDateTime.now()));
        notebookRepository.save(new Notebook("Czwarta", "opis czwartej", "treść czwartej notatki", LocalDateTime.now(), LocalDateTime.now()));
        notebookRepository.save(new Notebook("Piąta", "opis piątej", "treść piątej notatki", LocalDateTime.now(), LocalDateTime.now()));
    }

    public Page<Notebook> getNotes(Pageable pageable) {
        return Optional.ofNullable(notebookRepository.findAll(pageable)).orElseGet(Page::empty);
    }

    public boolean addNote(Notebook notebook) {
        if(notebook.getId() == null) {
            notebook.setCreated(LocalDateTime.now());
        }
        notebook.setModified(LocalDateTime.now());

        Optional<Notebook> note =  Optional.ofNullable(notebookRepository.save(notebook));

        return note.isPresent();
    }

    public Notebook editNote(Long id) {
        return findNote(id);
    }

    public void removeNote(Long id) {
        Notebook notebook = findNote(id);
        notebookRepository.delete(notebook);
    }

    private Notebook findNote(Long id) {
        return notebookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid note id: " + id));
    }

}

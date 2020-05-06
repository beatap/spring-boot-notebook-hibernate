package pl.beata.springbootnotebookhibernate.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.beata.springbootnotebookhibernate.model.Notebook;
import pl.beata.springbootnotebookhibernate.service.NotebookService;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class NotebookController {

    private NotebookService notebookService;


    public NotebookController(NotebookService notebookService) {
        this.notebookService = notebookService;
    }


    @GetMapping("/")
    public String listNote(@RequestParam Optional<Integer> page,
                           @RequestParam Optional<Integer> size,
                           Model model) {
        int pageReq = page.orElseGet(() -> Integer.valueOf(1).intValue());
        int sizeReq = size.orElseGet(() -> Integer.valueOf(2).intValue());
        Page<Notebook> notebookPage = notebookService.getNotes(PageRequest.of(pageReq - 1, sizeReq, Sort.by("created").descending()));

        model.addAttribute("notes", notebookPage);

        return "index";
    }

    @GetMapping("/add-form")
    public String showAddForm(Notebook notebook) {
        return "add-note";
    }

    @PostMapping("/add")
    public String addNote(@Valid Notebook notebook, BindingResult result, RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            return "add-note";
        }
        notebookService.addNote(notebook);
        redirectAttributes.addFlashAttribute("successMessage", "Notatka została poprawnie dodana");

        return "redirect:/";
    }

    @GetMapping("/edit-form/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("notebook", notebookService.editNote(id));

        return "edit-note";
    }

    @PostMapping("/edit/{id}")
    public String editNote(@PathVariable Long id, @Valid Notebook notebook, BindingResult result, RedirectAttributes redirectAttributes) {
        if(result.hasErrors()) {
            notebook.setId(id);
            return "edit-note";
        }
        notebookService.addNote(notebook);
        redirectAttributes.addFlashAttribute("successMessage", "Notatka została poprawnie zmodyfikowana");

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteNote(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        notebookService.removeNote(id);
        redirectAttributes.addFlashAttribute("successMessage", "Notatka została poprawnie usunięta");

        return "redirect:/";
    }
}

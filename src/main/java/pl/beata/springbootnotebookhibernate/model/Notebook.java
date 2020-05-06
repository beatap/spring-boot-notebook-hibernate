package pl.beata.springbootnotebookhibernate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "notebooks")
@Data
@NoArgsConstructor
public class Notebook {

    public Notebook(String title, String description, String text, LocalDateTime created, LocalDateTime modified) {
        this.title = title;
        this.description = description;
        this.text = text;
        this.created = created;
        this.modified = modified;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tytuł nie może być pusty!")
    @Column(nullable = false)
    private String title;

    @Size(message = "Maksymalna ilość znaków to 50!", max = 50)
    @Column(length = 50)
    private String description;

    @NotBlank(message = "Treść nie może być pusta!")
    @Size(message = "Maksymalna ilość znaków to 9999!", max = 9999)
    @Column(nullable = false, length = 9999)
    private String text;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime created;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime modified;
}

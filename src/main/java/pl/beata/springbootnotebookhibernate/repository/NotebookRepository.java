package pl.beata.springbootnotebookhibernate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.beata.springbootnotebookhibernate.model.Notebook;

@Repository
public interface NotebookRepository extends JpaRepository<Notebook, Long> {
}

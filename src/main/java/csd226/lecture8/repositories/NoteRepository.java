package csd226.lecture8.repositories;

import csd226.lecture8.data.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByTitleContaining(String title);
}

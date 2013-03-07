package com.naphaso.alice.service;

import com.naphaso.alice.dao.NoteDAO;
import com.naphaso.alice.persistence.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/5/13
 * Time: 3:40 PM
 */

@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    private NoteDAO noteDAO;

    @Override @Transactional
    public void addNote(Note note) {
        noteDAO.addNote(note);
    }

    @Override @Transactional
    public List<Note> listNote() {
        return noteDAO.listNote();
    }

    @Override @Transactional
    public void removeNote(Integer id) {
        noteDAO.removeNote(id);
    }

    @Override
    public void index() {
        noteDAO.index();
    }

    @Override @Transactional
    public List<Note> search(String query) {
        return noteDAO.search(query);
    }
}

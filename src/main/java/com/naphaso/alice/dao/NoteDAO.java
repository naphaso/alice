package com.naphaso.alice.dao;

import com.naphaso.alice.persistence.Note;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/5/13
 * Time: 3:37 PM
 */

public interface NoteDAO {
    public void addNote(Note note);
    public List<Note> listNote();
    public void removeNote(Integer id);
    public void index();
    public List<Note> search(String query);
}

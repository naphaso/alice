package com.naphaso.alice.service;

import com.naphaso.alice.persistence.Note;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/5/13
 * Time: 3:39 PM
 */

public interface NoteService {
    public void addNote(Note contact);
    public List<Note> listNote();
    public void removeNote(Integer id);
    public void index();
    public List<Note> search(String query);
}

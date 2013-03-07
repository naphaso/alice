package com.naphaso.alice.dao;

import com.naphaso.alice.persistence.Note;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.QueryParser;

import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/5/13
 * Time: 3:38 PM
 */


@Repository
public class NoteDAOImpl implements NoteDAO {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void addNote(Note note) {
        sessionFactory.getCurrentSession().save(note);

    }

    @Override
    public List<Note> listNote() {
        return sessionFactory.getCurrentSession().createQuery("from Note").list();
    }

    @Override
    public void removeNote(Integer id) {
        Note note = (Note) sessionFactory.getCurrentSession()
                .load(Note.class, id);
        if (null != note) {
            sessionFactory.getCurrentSession().delete(note);
        }
    }

    @Override
    public void index() {
        FullTextSession fullTextSession = Search.getFullTextSession(sessionFactory.getCurrentSession());
        try {
            fullTextSession.createIndexer().startAndWait();
        } catch (InterruptedException e) {

        }
    }

    @Override
    public List<Note> search(String query) {
        FullTextSession fullTextSession = Search.getFullTextSession(sessionFactory.getCurrentSession());
        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(Note.class).get();
        org.apache.lucene.search.Query ftquery = queryBuilder.keyword().onFields("title", "content").matching(query).createQuery();
        org.hibernate.Query hibQuery =
                fullTextSession.createFullTextQuery(ftquery, Note.class);
        return hibQuery.list();
    }
}

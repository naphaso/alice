package com.naphaso.alice.persistence;


import org.hibernate.search.annotations.*;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: wolong
 * Date: 3/5/13
 * Time: 3:20 PM
 */
@Indexed
@Entity @Table
public class Note {
    @DocumentId
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id", nullable = false, unique = true, updatable = false)
    private Integer id;

    @Basic @Column(name = "title")
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String title;


    @Basic @Column(name = "content")
    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String content;

    public Note() {}

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

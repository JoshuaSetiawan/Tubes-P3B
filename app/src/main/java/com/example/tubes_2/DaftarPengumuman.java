package com.example.tubes_2;

public class DaftarPengumuman {
    String id;
    String title;
    String updated_at;
    String created_at;
    Author author;
    Tags tags;
    public DaftarPengumuman(String id,String title,String updated_at
            ,String created_at,Author author,Tags tags){
        this.id=id;
        this.title=title;
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.author = author;
        this.tags = tags;
    }
}
class Tags{
    String tag;
    String tag_id;
    public Tags(String tag,String tag_id){
        this.tag = tag;
        this.tag_id = tag_id;
    }
}
class Author{
    String id;
    String author;
    public Author(String id,String author){
        this.id= id;
        this.author =author;
    }
}
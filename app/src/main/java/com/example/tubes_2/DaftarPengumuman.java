package com.example.tubes_2;

import java.util.ArrayList;

public class DaftarPengumuman{
    String id;
    String title;
    String updated_at;
    String created_at;
    Author author;
    ArrayList<Tags> tags;
    public DaftarPengumuman(String id,String title,
                            String updated_at
                            ,String created_at,
                            Author author,ArrayList<Tags> tags){
        this.id = id;
        this.title = title;
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.author = author;
        this.tags = tags;
    }
}

//public class DaftarPengumuman {
//    Meta metadata;
//    String id;
//    String title;
//    String updated_at;
//    String created_at;
//    Author author;
//    Tags tags;
//    public DaftarPengumuman(Meta metadata,String id,String title,String updated_at
//            ,String created_at,Author author,Tags tags){
//        this.metadata =metadata;
//        this.id=id;
//        this.title=title;
//        this.updated_at = updated_at;
//        this.created_at = created_at;
//        this.author = author;
//        this.tags = tags;
//    }
//}
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
class Pertemuan{
    String id;
    String title;
    String started_datetime;
    String end_datetime;
    public Pertemuan(String id,String title,String started_datetime,String end_datetime){
        this.id = id;
        this.title = title;
        this.started_datetime = started_datetime;
        this.end_datetime = end_datetime;
    }
}
class Undangan{
    String id;
    String title;
    String started_datetime;
    String end_datetime;
    String description;
    String organizer;
    boolean attending;
    public Undangan(String id,String title,String started_datetime,String end_datetime,String description,String organizer,boolean attending){
        this.id = id;
        this.title = title;
        this.started_datetime = started_datetime;
        this.end_datetime = end_datetime;
        this.description = description;
        this.organizer = organizer;
        this.attending = attending;
    }
}
//class Meta{
//    String next;
//    public Meta(String next){
//        this.next = next;
//    }
//}
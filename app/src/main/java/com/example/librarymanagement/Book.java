package com.example.librarymanagement;

public class Book {

    private String book_name,author,description;
    private int copy_number;

    public Book(String book_name, String author, String description, int copy_number) {
        this.book_name = book_name;
        this.author = author;
        this.description = description;
        this.copy_number = copy_number;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCopy_number() {
        return copy_number;
    }

    public void setCopy_number(int copy_number) {
        this.copy_number = copy_number;
    }
}

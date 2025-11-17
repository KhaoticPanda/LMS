package model;

public class books {
    private int bookId;
    private String title;
    private String author;
    private String category;
    private int copiesAvailable;

    // Constructor
    public books(int bookId, String title, String author, String category, int copiesAvailable) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.copiesAvailable = copiesAvailable;
    }

    // Getters and Setters
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCopiesAvailable() {
        return copiesAvailable;
    }

    public void setCopiesAvailable(int copiesAvailable) {
        this.copiesAvailable = copiesAvailable;
    }

    // Optional: toString method for debugging
    @Override
    public String toString() {
        return "books{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", copiesAvailable=" + copiesAvailable +
                '}';
    }
}

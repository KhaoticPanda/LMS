package model;

import java.time.LocalDate;

/**
 * Model class representing a borrow record in the library system.
 * 
 * This class holds details about a borrowed book, including the book ID,
 * title, borrow and due dates, return date, status, and any applicable fine.
 */
public class borrowRecord {

    // ---------- Fields ----------

    /** Unique ID of the book */
    private int bookId;

    /** Title of the borrowed book */
    private String title;

    /** Date the book was borrowed */
    private LocalDate borrowDate;

    /** Due date for returning the book */
    private LocalDate dueDate;

    /** Date the book was returned (null if not returned yet) */
    private LocalDate returnDate;

    /** Current status: Borrowed, Returned, Overdue */
    private String status;

    /** Fine amount if the book is overdue */
    private double fine;


    // ---------- Constructor ----------

    /**
     * Constructs a new borrowRecord.
     *
     * @param bookId     ID of the book
     * @param title      Title of the book
     * @param borrowDate Date borrowed
     * @param dueDate    Due date
     * @param returnDate Date returned (can be null)
     * @param status     Current status
     * @param fine       Late fee
     */
    public borrowRecord(int bookId, String title, LocalDate borrowDate, LocalDate dueDate,
                        LocalDate returnDate, String status, double fine) {
        this.bookId = bookId;
        this.title = title;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
        this.fine = fine;
    }


    // ---------- Getters ----------

    public int getBookId() { return bookId; }

    public String getTitle() { return title; }

    public LocalDate getBorrowDate() { return borrowDate; }

    public LocalDate getDueDate() { return dueDate; }

    public LocalDate getReturnDate() { return returnDate; }

    public String getStatus() { return status; }

    public double getFine() { return fine; }


    // ---------- Setters ----------

    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public void setStatus(String status) { this.status = status; }

    public void setFine(double fine) { this.fine = fine; }


    // ---------- Utility Methods ----------

    /**
     * Determines if the borrowed book is overdue.
     *
     * @return true if overdue, false otherwise
     */
    public boolean isOverdue() {
        if (returnDate != null) {
            return returnDate.isAfter(dueDate);
        } else {
            return LocalDate.now().isAfter(dueDate);
        }
    }

    /**
     * Calculates the fine based on overdue days.
     *
     * @param perDayFee Fee per day overdue
     */
    public void calculateFine(double perDayFee) {
        long daysLate;
        if (returnDate != null) {
            daysLate = java.time.temporal.ChronoUnit.DAYS.between(dueDate, returnDate);
        } else {
            daysLate = java.time.temporal.ChronoUnit.DAYS.between(dueDate, LocalDate.now());
        }

        fine = (daysLate > 0) ? daysLate * perDayFee : 0;
    }

}

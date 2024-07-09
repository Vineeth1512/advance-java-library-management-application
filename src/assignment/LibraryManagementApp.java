package assignment;

import java.util.List;
import java.util.Scanner;

public class LibraryManagementApp {
    private LibraryDatabase libraryDB;

    public LibraryManagementApp() {
        libraryDB = new LibraryDatabase();
    }

    public void start() throws NotUniqueBookException, InvalidISBNException, BookNotFoundException {
       
    	Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Library Management System Menu:");
            System.out.println("1. Add a book");
            System.out.println("2. Display all books");
            System.out.println("3. Edit book details");
            System.out.println("4. Delete a book");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    // Add a book
                    System.out.println("Enter the book details:");
                    Book newBook = new Book();
                    System.out.print("Title: ");
                    newBook.setTitle(scanner.nextLine());
                    System.out.print("Author: ");
                    newBook.setAuthor(scanner.nextLine());
                    System.out.print("ISBN: ");
                    newBook.setIsbn(scanner.nextLine());
                    System.out.print("Publication Year: ");
                    newBook.setPublicationYear(scanner.nextInt());
                    scanner.nextLine(); // Consume the newline character
                    System.out.print("Genre: ");
                    newBook.setGenre(scanner.nextLine());
                    libraryDB.addBook(newBook);
                    System.out.println("Book added successfully!");
                    break;

                case 2:
                    // Display all books
                    List<Book> books = libraryDB.getAllBooks();
                    if (books.isEmpty()) {
                        System.out.println("The library is empty.");
                    } else {
                        System.out.println("List of all books in the library:");
                        for (Book book : books) {
                            System.out.println(book.getTitle() + " by " + book.getAuthor());
                        }
                    }
                    break;

                case 3:
                    // Edit book details
                    System.out.println("Enter the ISBN of the book you want to edit:");
                    String isbnToEdit = scanner.nextLine();
                    Book updatedBook = new Book();
                    updatedBook.setIsbn(isbnToEdit);
                    System.out.print("New Title: ");
                    updatedBook.setTitle(scanner.nextLine());
                    System.out.print("New Author: ");
                    updatedBook.setAuthor(scanner.nextLine());
                    System.out.print("New Publication Year: ");
                    updatedBook.setPublicationYear(scanner.nextInt());
                    scanner.nextLine(); // Consume the newline character
                    System.out.print("New Genre: ");
                    updatedBook.setGenre(scanner.nextLine());
                    libraryDB.editBook(isbnToEdit, updatedBook);
                    System.out.println("Book details updated successfully!");
                    break;

                case 4:
                    // Delete a book
                    System.out.println("Enter the ISBN of the book you want to delete:");
                    String isbnToDelete = scanner.nextLine();
                    libraryDB.deleteBook(isbnToDelete);
                    System.out.println("Book deleted successfully!");
                    break;

                case 5:
                    libraryDB.closeConnection();
                    System.out.println("Exiting the program.");
                   // System.exit(0);
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
       
    	LibraryManagementApp app = new LibraryManagementApp();
        try {
            app.start();
        } catch (NotUniqueBookException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidISBNException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (BookNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

package assignment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibraryDatabase {
	private Connection connection;

	public LibraryDatabase() {
		// Initialize the database connection here (replace the connection URL,
		// username, and password with your own)
		String url = "jdbc:mysql://localhost:3306/librarymanagementsystem";
		String username = "root";
		String password = "@1234";
		try {
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Modify the addBook method to throw NotUniqueBookException
	public void addBook(Book book) throws NotUniqueBookException, InvalidISBNException {
		try {
			if (!isIsbnValid(book.getIsbn())) {
				throw new IllegalArgumentException("Invalid ISBN. Please enter a valid ISBN.");
			}

			if (!isIsbnUnique(book.getIsbn())) {
				throw new NotUniqueBookException("A book with this ISBN already exists. Please use a different ISBN.");
			}

			String sql = "INSERT INTO books (title, author, isbn, publicationYear, genre) VALUES (?, ?, ?, ?, ?)";
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.setString(1, book.getTitle());
				preparedStatement.setString(2, book.getAuthor());
				preparedStatement.setString(3, book.getIsbn());
				preparedStatement.setInt(4, book.getPublicationYear());
				preparedStatement.setString(5, book.getGenre());
				preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// Handle database-related errors
		}
	}

	// Modify the editBook method to throw NotUniqueBookException
	public void editBook(String isbn, Book updatedBook)
			throws NotUniqueBookException, InvalidISBNException, BookNotFoundException {
		System.out.println("update method isbnToEdit: " + isbn);
		System.out.println(isbn);

		try {

			if (!isIsbnValid(updatedBook.getIsbn())) {
				System.out.println(updatedBook.getIsbn());
				throw new InvalidISBNException("Invalid ISBN. Please enter a valid ISBN.");
			}

			// Check if a book with the provided ISBN exists
			if (!doesBookExist(isbn)) {
				throw new BookNotFoundException("Book with ISBN " + isbn + " not found. Cannot edit.");
			}

			/*
			 * if (!isIsbnValid(isbn)){ System.out.println(isbn); throw new
			 * InvalidISBNException("Invalid ISBN. Please enter a valid ISBN."); }
			 */

			/*
			 * if (!isIsbnUnique(updatedBook.getIsbn()) &&
			 * !isbn.equals(updatedBook.getIsbn())) { throw new NotUniqueBookException(
			 * "A book with the updated ISBN already exists. Please use a different ISBN.");
			 * }
			 */

			String sql = "UPDATE books SET title = ?, author = ?, publicationYear = ?, genre = ? WHERE isbn = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.setString(1, updatedBook.getTitle());
				preparedStatement.setString(2, updatedBook.getAuthor());
				preparedStatement.setInt(3, updatedBook.getPublicationYear());
				preparedStatement.setString(4, updatedBook.getGenre());
				preparedStatement.setString(5, isbn);
				preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// Handle database-related errors
		}
	}

	public List<Book> getAllBooks() {
		List<Book> books = new ArrayList<>();
		String sql = "SELECT * FROM books";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet resultSet = preparedStatement.executeQuery()) {
			while (resultSet.next()) {
				Book book = new Book();
				book.setTitle(resultSet.getString("title"));
				book.setAuthor(resultSet.getString("author"));
				book.setIsbn(resultSet.getString("isbn"));
				book.setPublicationYear(resultSet.getInt("publicationYear"));
				book.setGenre(resultSet.getString("genre"));
				books.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return books;
	}

	public void deleteBook(String isbn) throws BookNotFoundException {

		// Check if a book with the provided ISBN exists
		if (!doesBookExist(isbn)) {
			throw new BookNotFoundException("Book with ISBN " + isbn + " not found. Cannot Delete.");
		}
		String sql = "DELETE FROM books WHERE isbn = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, isbn);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Close the database connection when the program exits
	public void closeConnection() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Add a new method in LibraryDatabase to check for duplicate ISBNs
	public boolean isIsbnUnique(String isbn) {
		String sql = "SELECT COUNT(*) FROM books WHERE isbn = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, isbn);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					int count = resultSet.getInt(1);
					return count == 0; // Return true if the ISBN is unique (count is 0)
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false; // An error occurred, so treat it as a non-unique ISBN
	}

	// Helper method to check if an ISBN is valid
	public boolean isIsbnValid(String isbn) throws InvalidISBNException {
		System.out.println();
		if (isbn.length() != 13 || !isbn.matches("\\d{13}")) {
			throw new InvalidISBNException("Invalid ISBN. ISBN must be a 13-digit number.");
		}
		return true; // ISBN is valid
	}

	public boolean doesBookExist(String isbn) {
		String sql = "SELECT COUNT(*) FROM books WHERE isbn = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, isbn);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					int count = resultSet.getInt(1);
					return count > 0; // Return true if a book with the ISBN exists
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false; // An error occurred, so treat it as if the book doesn't exist
	}

}

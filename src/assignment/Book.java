package assignment;

public class Book {
	private String title;
	private String author;
	private String isbn;
	private int publicationYear;
	private String genre;

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

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public int getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(int publicationYear) {
		this.publicationYear = publicationYear;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}
}

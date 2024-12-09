package business;

import java.util.List;

import business.Book;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

public interface ControllerInterface {
	public void login(String id, String password) throws LoginException;
	public List<String> allMemberIds();
	public List<String> allBookIds();
	public List<LibraryMember> findAllLibraryMembers();
    public List<Book> allBooks();
    public List<Author> allAuthors();
    public void addNewBook(Book book);
    public Book getBookByIsbn(String isnb);
}

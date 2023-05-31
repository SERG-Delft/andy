package delft;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class Book {
    private String ISBN;
    private int price;
    private int amount;

    public Book(String ISBN, int price, int amount) {
        this.ISBN = ISBN;
        this.price = price;
        this.amount = amount;
    }

    public int getPrice() {
        return price;
    }
    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return ISBN.equals(book.ISBN);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ISBN);
    }
}

interface BookRepository {
    Book findByISBN(String ISBN);
}

interface BuyBookProcess {
    void buyBook(Book book, int amount);
}

class Overview {
    int totalPrice;
    Map<Book, Integer> unavailable;

    public Overview() {
        this.unavailable = new HashMap<>();
    }
}

class BookStore {

    private BookRepository bookRepository;
    private BuyBookProcess process;

    public BookStore(BookRepository bookRepository, BuyBookProcess process) {
        this.bookRepository = bookRepository;
        this.process = process;
    }

    private void retrieveBook(String ISBN, int amount, Overview overview) {
        Book book = bookRepository.findByISBN(ISBN);
        if (book.getAmount() < amount) {
            overview.unavailable.put(book, amount - book.getAmount());
            amount = book.getAmount();
        }
        overview.totalPrice += amount * book.getPrice();
        process.buyBook(book, amount);
    }

    /**
     * This is the method to test!!
     */
    public Overview getPriceForCart(Map<String, Integer> order) {
        if(order==null)
            return null;

        Overview overview = new Overview();
        overview.totalPrice = 0;
        for (String ISBN : order.keySet())
            retrieveBook(ISBN, order.get(ISBN), overview);
        return overview;
    }

}

package delft;

import static java.util.stream.Collectors.toList;

import java.util.List;

class InvoiceFilter {

	final IssuedInvoices issuedInvoices;

	public InvoiceFilter(IssuedInvoices issuedInvoices) {
		this.issuedInvoices = issuedInvoices;
	}

	public List<Invoice> lowValueInvoices() {
		return issuedInvoices.all().stream().filter(invoice -> invoice.getValue() < 100).collect(toList());
	}
}

interface IssuedInvoices {

	List<Invoice> all();

	void save(Invoice inv);
}

class Invoice {

	private final int value;

	public Invoice(int value) {
		this.value = value;
	}

	int getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o instanceof Invoice) {
			return value == ((Invoice) o).value;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}

package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;

class InvoiceFilterTest {

	private InvoiceFilter invoiceFilter;

	private IssuedInvoices issuedInvoices;

	@BeforeEach
	void setup() {
		issuedInvoices = mock(IssuedInvoices.class);
		invoiceFilter = new InvoiceFilter(issuedInvoices);
	}

    @Test
	void allHighValueInvoices() {
		when(issuedInvoices.all()).thenReturn(List.of(new Invoice(453), new Invoice(100)));
		assertThat(invoiceFilter.lowValueInvoices()).isEmpty();
	}

    @Test
	void allLowValueInvoices() {
		when(issuedInvoices.all()).thenReturn(List.of(new Invoice(43), new Invoice(99)));
		assertThat(invoiceFilter.lowValueInvoices()).containsExactly(new Invoice(43), new Invoice(99));
	}
    
}

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

class MyHealthTest {

	private AuthService authService;
    private LabResultRepository repository;
    private Log log;

    private MyHealth app;

	@BeforeEach
	void setup() {
		authService = Mockito.mock(AuthService.class);
		repository = Mockito.mock(LabResultRepository.class);
		log = Mockito.mock(Log.class);

		app = new MyHealth(authService, repository, log);
        
        doNothing().when(log).warn(anyString());
	}

    @Test
    void testNotAuthorised() {
        long userId = 42L;
        long resultId = 13L;

        when(authService.canViewLabResult(userId, resultId)).thenReturn(false);
        when(repository.getLabResultById(resultId)).thenReturn("result");

        assertThat(app.getLabResult(userId, resultId)).isNull();

        verify(log).warn(anyString());
    }

    @Test
    void testReturnsFromRepositoryIfPresent() {
        long userId = 42L;
        long resultId = 13L;

        when(authService.canViewLabResult(userId, resultId)).thenReturn(true);
        when(repository.getLabResultById(resultId)).thenReturn("result");

        assertThat(app.getLabResult(userId, resultId)).isEqualTo("result");
    }

    @Test
    void testReturnsNullAndLogsIfNotPresent() {
        long userId = 42L;
        long resultId = 13L;

        when(authService.canViewLabResult(userId, resultId)).thenReturn(true);
        when(repository.getLabResultById(resultId)).thenReturn(null);

        assertThat(app.getLabResult(userId, resultId)).isNull();

        verify(log).warn(anyString());
    }

}

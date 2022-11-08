package CodeSharingE.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class SnippetNotFoundException extends RuntimeException {
    public SnippetNotFoundException(String cause) {
        super(cause);
    }
}

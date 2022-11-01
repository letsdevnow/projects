package CinemaRestServiceE.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class Response {
    @Getter
    @Setter
    private String error;
    //@Getter @Setter private String debugMessage;
}

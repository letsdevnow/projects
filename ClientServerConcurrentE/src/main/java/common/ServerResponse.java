package common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ServerResponse {
    private String response;
    private String value;
    private String reason;

    public ServerResponse(String response, String value) {
        this.response = response;
        this.value = value;
    }

    public ServerResponse(String response) {
        this.response = response;
    }

}

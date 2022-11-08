package common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServerRequest {
    private String type;
    private String key;
    private String value;

    public ServerRequest(String type) {
        this.type = type;
    }

    public ServerRequest(String type, String key) {
        this.type = type;
        this.key = key;
    }

}
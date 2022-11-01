package CinemaRestServiceE.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Seat {
    private int row = 0;
    private int column = 0;
    @JsonIgnore
    private boolean isBooked = false;
    private int price = 0;
    @JsonIgnore
    private String token = "";

    public Seat(int row, int column, int price) {
        this.row = row;
        this.column = column;
        this.price = price;
    }
}

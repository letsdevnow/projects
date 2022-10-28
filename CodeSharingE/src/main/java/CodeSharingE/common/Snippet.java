package CodeSharingE.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "snippets")
public class Snippet {
    @Getter
    @Setter
    @Id
    @Column (name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @Getter @Setter
    @Column (name = "code")
    private String code = "";

    @Setter
    @Column (name = "date")
    private LocalDateTime date = LocalDateTime.now();

    public String getDate() {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    
    @JsonIgnore
    public LocalDateTime getFullDate() {
        return date;
    }
}


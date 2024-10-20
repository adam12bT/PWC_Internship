package PWC.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 250)
    private String content;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "comment_post", nullable = false)
    private Post post;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "comment_user", nullable = false)
    private User user;

}

package by.clevertec.commentsproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "news")
@NoArgsConstructor
@EqualsAndHashCode
public class News implements Serializable {

    @Id
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "news")
    private List<Comment> comments;


}

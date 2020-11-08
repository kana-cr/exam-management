package us.sep.message.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.sep.base.AbstractAuditBase;
import us.sep.message.builder.CarouselBO;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carousel", indexes = {
        @Index(name = "carousel_index", columnList = "carouselId")})
public class CarouselDO extends AbstractAuditBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false , length = 32 ,updatable = false)
    private String carouselId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false , columnDefinition="text")
    private String text;

    @Column(nullable = false)
    private String label;

    @Column
    private String note;

    @Column(nullable = false)
    private String subDate;

    public CarouselBO toCarouselBO(){
        return CarouselBO.builder().title(title).text(text).label(label)
                .note(note).subDate(subDate).carouselId(carouselId).build();
    }
}

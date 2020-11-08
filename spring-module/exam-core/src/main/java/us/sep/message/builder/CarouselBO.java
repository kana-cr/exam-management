package us.sep.message.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarouselBO  {


    private String title;

    private String text;

    private String label;

    private String note;

    private String subDate;

    private String carouselId;
}

package us.sep.biz.message.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarouselRequest {

    private String carouselId;

    @NotBlank
    private String title;

    @NotBlank
    private String text;

    @NotBlank
    private String label;

    @NotBlank
    private String subDate;

    private String note;
}

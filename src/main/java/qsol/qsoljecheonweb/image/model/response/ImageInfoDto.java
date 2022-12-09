package qsol.qsoljecheonweb.image.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
public class ImageInfoDto {
    private String imgName;
    private String varcd;
    private String vmlcd;
}

package qsol.qsoljecheonweb.device.model.response;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HistoryViewDto {
    private int testid;
    private int locid;
    private String cid;
}

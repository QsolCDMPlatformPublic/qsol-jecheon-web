package qsol.qsoljecheonweb.code.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarCodeViewDto {
    private String code;
    private String codegp;
    private String codenm;
    private Boolean useyn;
    private String useynMessage;

    private String reference;

    public Boolean isLessCharactersCodenm(String charactors) {
        return charactors.length() < 2;
    }

    public String postCodeTypeChange(String preCode, String postCode) {
        Long typeChangePostCode = Long.parseLong(postCode);
        ++typeChangePostCode;
        String reTypeChangePostCode = Long.toString(typeChangePostCode);
        for(int count = reTypeChangePostCode.length(); count < 4; count++) {
            reTypeChangePostCode = '0'+reTypeChangePostCode;
        }
        return preCode+reTypeChangePostCode;
    }
}

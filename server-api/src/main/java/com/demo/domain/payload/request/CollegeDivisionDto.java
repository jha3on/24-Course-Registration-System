package com.demo.domain.payload.request;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(of = {"collegeDivisionName", "collegeDivisionNumber"})
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CollegeDivisionDto implements Serializable {
    private String collegeDivisionName;
    private String collegeDivisionNumber;

    @Builder(access = AccessLevel.PUBLIC)
    public CollegeDivisionDto(String collegeDivisionName, String collegeDivisionNumber) {
        this.collegeDivisionName = collegeDivisionName;
        this.collegeDivisionNumber = collegeDivisionNumber;
    }

    public static CollegeDivisionDto of(String collegeDivisionName, String collegeDivisionNumber) {
        return CollegeDivisionDto.builder()
                .collegeDivisionName(collegeDivisionName)
                .collegeDivisionNumber(collegeDivisionNumber)
                .build();
    }
}
package com.savvycom.userservice.domain.model.pagging;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageOutput<T> {
    @Schema(description = "List of item")
    List<T> content;

    @Schema(description = "Page number")
    private Integer pageNo;

    @Schema(description = "The number of elements in one page")
    private Integer pageSize;

    @Schema(description = "The number of total elements")
    private Long totalElements;

}

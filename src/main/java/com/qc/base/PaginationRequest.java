package com.qc.base;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class PaginationRequest {
    @NotNull
    @Min(0)
    private int current;

    @NotNull
    @Min(1)
    @Max(50)
    private int pageSize;
}

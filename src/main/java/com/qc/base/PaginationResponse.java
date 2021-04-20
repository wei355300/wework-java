package com.qc.base;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.Collection;

@Data
public class PaginationResponse<L> {
    private Collection<L> list;
    private int current;
    private int pageSize;
    private long total;

    public static <T> PaginationResponse<T> toPagination(PageInfo<T> pageInfo) {
        PaginationResponse p = new PaginationResponse();
        p.setList(pageInfo.getList());
        p.setCurrent(pageInfo.getPageNum());
        p.setPageSize(pageInfo.getPageSize());
        p.setTotal(pageInfo.getTotal());
        return p;
    }
}

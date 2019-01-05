package com.polypay.platform;

import java.util.List;

/**
 * 
 * @author mwj
 *
 * @param <E>
 */
public class Page<E> {

    private List<E> rows;

    private Integer pageSize = 15;

    private Integer pageIndex = 1;

    private Integer total; //总记录条数

    public List<E> getRows() {
        return rows;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public Integer getTotal() {
        return total;
    }

    public void setRows(List<E> rows) {
        this.rows = rows;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}

package io.leego.unique.common;

import java.io.Serializable;

/**
 * @author Yihleego
 */
public class Pageable implements Serializable {
    private static final long serialVersionUID = 1838009842356246312L;
    /** One-based page index */
    protected Integer page;
    /** The size of the page to be returned */
    protected Integer size;
    /** Zero-based row index */
    protected Integer offset;
    /** The rows of the page to be returned */
    protected Integer rows;

    public Pageable() {
    }

    public Pageable(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

    public Pageable(Integer page, Integer size, Integer offset, Integer rows) {
        this.page = page;
        this.size = size;
        this.offset = offset;
        this.rows = rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

}

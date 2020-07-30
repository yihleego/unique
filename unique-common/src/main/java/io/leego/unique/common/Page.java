package io.leego.unique.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Yihleego
 */
public class Page<T> implements Serializable {
    private static final long serialVersionUID = 3214571808482585491L;
    /** Data list */
    protected List<T> list;
    /** One-based page index */
    protected Integer page;
    /** The size of the page to be returned */
    protected Integer size;
    /** Total quantity */
    protected Long total;
    /** Total pages */
    protected Long totalPages;
    /** Next page */
    protected Boolean next;
    /** Previous page */
    protected Boolean previous;
    /** Extra data */
    protected Object extra;

    public Page(List<T> list, Integer page, Integer size, Long total, Long totalPages, Boolean next, Boolean previous, Object extra) {
        this.list = list;
        this.page = page;
        this.size = size;
        this.total = total;
        this.totalPages = totalPages;
        this.next = next;
        this.previous = previous;
        this.extra = extra;
    }

    public Page(List<T> list, Integer page, Integer size, Long total, Long totalPages, Boolean next, Boolean previous) {
        this.list = list;
        this.page = page;
        this.size = size;
        this.total = total;
        this.totalPages = totalPages;
        this.next = next;
        this.previous = previous;
    }

    public Page(List<T> list, Integer page, Integer size) {
        this.list = list;
        this.page = page;
        this.size = size;
    }

    public Page(List<T> list, Long total) {
        this.list = list;
        this.total = total;
    }

    public Page(List<T> list) {
        this.list = list;
    }

    public static <T> Page<T> of(List<T> list, Integer page, Integer size, Long total, Long totalPages, Boolean next, Boolean previous, Object extra) {
        return new Page<>(list, page, size, total, totalPages, next, previous, extra);
    }

    public static <T> Page<T> of(List<T> list, Integer page, Integer size, Long total, Long totalPages, Boolean next, Boolean previous) {
        return new Page<>(list, page, size, total, totalPages, next, previous);
    }

    public static <T> Page<T> of(List<T> list, Integer page, Integer size, Long total) {
        if (page == null || size == null) {
            return new Page<>(list, total);
        }
        if (total == null) {
            return new Page<>(list, page, size);
        }
        boolean next;
        boolean previous;
        long totalPages;
        if (page > 0 && size > 0) {
            next = page * size < total;
            previous = page != 1;
            totalPages = total % size > 0 ? total / size + 1 : total / size;
        } else {
            next = false;
            previous = false;
            totalPages = 0L;
        }
        return new Page<>(list, page, size, total, totalPages, next, previous);
    }

    public static <T> Page<T> of(List<T> list, Integer page, Integer size) {
        return new Page<>(list, page, size);
    }

    public static <T> Page<T> of(List<T> list, Long total) {
        return new Page<>(list, total);
    }

    public static <T> Page<T> of(List<T> list) {
        return new Page<>(list);
    }

    public static <T> Page<T> of(Collection<T> collection) {
        if (collection == null) {
            return new Page<>(new ArrayList<>());
        }
        if (collection instanceof List) {
            return new Page<>((List<T>) collection);
        }
        return new Page<>(new ArrayList<>(collection));
    }

    public static <T> Page<T> empty() {
        return new Page<>(new ArrayList<>());
    }

    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        return new Page<>(list == null ? null : list.stream().map(converter).collect(Collectors.toList()),
                page, size, total, totalPages, next, previous, extra);
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
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

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public Boolean getNext() {
        return next;
    }

    public void setNext(Boolean next) {
        this.next = next;
    }

    public Boolean getPrevious() {
        return previous;
    }

    public void setPrevious(Boolean previous) {
        this.previous = previous;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }

    public <E> E getExtra(Class<E> type) {
        return type.cast(extra);
    }

    public int getCurrent() {
        return list != null ? list.size() : 0;
    }

    public boolean isEmpty() {
        return list == null || list.isEmpty();
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private List<T> list;
        private Integer page;
        private Integer size;
        private Long total;
        private Long totalPages;
        private Boolean next;
        private Boolean previous;

        public Builder<T> list(List<T> list) {
            this.list = list;
            return this;
        }

        public Builder<T> page(Integer page) {
            this.page = page;
            return this;
        }

        public Builder<T> size(Integer size) {
            this.size = size;
            return this;
        }

        public Builder<T> total(Long total) {
            this.total = total;
            return this;
        }

        public Builder<T> totalPages(Long totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public Builder<T> next(Boolean next) {
            this.next = next;
            return this;
        }

        public Builder<T> previous(Boolean previous) {
            this.previous = previous;
            return this;
        }

        public Page<T> build() {
            return new Page<>(list, page, size, total, totalPages, next, previous);
        }

    }

}

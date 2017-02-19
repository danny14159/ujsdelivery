package com.express.core.extend;

import java.io.Serializable;
import java.util.List;


public class QueryResult implements Serializable {

    private static final long serialVersionUID = 5104522523949248573L;
    private List<?> list;
    private Pager pager;

    public QueryResult() {}

    public QueryResult(List<?> list, Pager pager) {
        this.list = list;
        this.pager = pager;
    }

    public List<?> getList() {
        return list;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getList(Class<T> eleType) {
        return (List<T>) list;
    }

    public QueryResult setList(List<?> list) {
        this.list = list;
        return this;
    }

    public Pager getPager() {
        return pager;
    }

    public QueryResult setPager(Pager pager) {
        this.pager = pager;
        return this;
    }

}

package com.express.core.bean;

public class MessageItem<T> {

	private Integer index;
	
	private T obj;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public T getObj() {
		return obj;
	}

	public void setObj(T obj) {
		this.obj = obj;
	}

	public MessageItem(Integer index, T obj) {
		super();
		this.index = index;
		this.obj = obj;
	}
}

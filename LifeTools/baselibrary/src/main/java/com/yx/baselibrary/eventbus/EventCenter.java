package com.yx.baselibrary.eventbus;

/**
 * @ClassName EventCenter
 * @Description eventbus 传递bean
 * Created by yx on 2017-02-27.
 */

public class EventCenter<T> {
    private T data;
    private int eventCode;

    public EventCenter(int eventCode) {
        this(eventCode, null);
    }

    public EventCenter(int eventCode, T data) {
        this.eventCode = -1;
        this.eventCode = eventCode;
        this.data = data;
    }

    public int getEventCode() {
        return this.eventCode;
    }

    public T getData() {
        return this.data;
    }
}

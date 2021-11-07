package com.zst.week13.array.mq.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

@AllArgsConstructor
@Data
@Builder
public class AmqMessage<T> {
    private HashMap<String, Object> headers;
    private T body;
}

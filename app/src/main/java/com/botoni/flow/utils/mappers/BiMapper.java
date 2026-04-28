package com.botoni.flow.utils.mappers;

public interface BiMapper<I, O> {
    O mapTo(I i);
    I mapFrom(O o);
}

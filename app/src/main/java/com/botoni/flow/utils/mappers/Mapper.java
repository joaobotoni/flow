package com.botoni.flow.utils.mappers;

public interface Mapper<I, O> {
    O mapper(I i);
}

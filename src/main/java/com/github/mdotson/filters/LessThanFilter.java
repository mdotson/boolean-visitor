package com.github.mdotson.filters;

import com.github.mdotson.BooleanVisitor;
import lombok.Value;

@Value
public class LessThanFilter implements Filter {

    private final String field;
    private final int lessThanValue;

    @Override
    public boolean accept(final BooleanVisitor visitor) {
        return visitor.visit(this);
    }
}

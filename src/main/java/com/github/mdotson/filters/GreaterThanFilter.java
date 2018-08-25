package com.github.mdotson.filters;

import com.github.mdotson.BooleanVisitor;
import lombok.Value;

@Value
public class GreaterThanFilter implements Filter {

    private final String field;
    private final int greaterThanValue;

    @Override
    public boolean accept(final BooleanVisitor visitor) {
        return visitor.visit(this);
    }
}

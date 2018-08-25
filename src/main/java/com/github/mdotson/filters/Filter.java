package com.github.mdotson.filters;

import com.github.mdotson.BooleanVisitor;

public interface Filter {
    boolean accept(final BooleanVisitor visitor);
}

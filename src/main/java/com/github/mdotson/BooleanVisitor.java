package com.github.mdotson;

import com.github.mdotson.filters.GreaterThanFilter;
import com.github.mdotson.filters.LessThanFilter;
import com.github.mdotson.filters.StringMatcherFilter;
import com.google.common.base.Preconditions;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.StringUtils;

public class BooleanVisitor {

    private final DocumentContext jsonDocument;

    public BooleanVisitor(final String json) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(json));
        this.jsonDocument = JsonPath.parse(json);
    }

    public boolean visit(final StringMatcherFilter filter) {
        final String path = String.format("$.[?(@.%s =~ /%s/)]", filter.getField(), filter.getRegex());
        final JSONArray value = jsonDocument.read(path);
        return !value.isEmpty();
    }

    public boolean visit(final LessThanFilter filter) {
        final String path = String.format("$.[?(@.%s < %s)]", filter.getField(), filter.getLessThanValue());
        final JSONArray value = jsonDocument.read(path);
        return !value.isEmpty();
    }

    public boolean visit(final GreaterThanFilter filter) {
        final String path = String.format("$.[?(@.%s > %s)]", filter.getField(), filter.getGreaterThanValue());
        final JSONArray value = jsonDocument.read(path);
        return !value.isEmpty();
    }
}

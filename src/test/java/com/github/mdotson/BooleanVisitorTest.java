package com.github.mdotson;

import com.github.mdotson.filters.*;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BooleanVisitorTest {

    private final StringMatcher jeanLucNameMatcher = new StringMatcher("firstName", "Jean-Luc");
    private final StringMatcher wesleyNameMatcher = new StringMatcher("firstName", "Wesley");
    private final StringMatcher worfNameMatcher = new StringMatcher("firstName", "Worf");
    private final StringMatcher rikerNameMatcher = new StringMatcher("firstName", "William");
    private final StringMatcher lastNameMatcher = new StringMatcher("lastName", "Picard");
    private final LessThanFilter ageLessThan70Filter = new LessThanFilter("personalInfo.age", 70);
    private final GreaterThanFilter ageGreaterThan60Filter = new GreaterThanFilter("personalInfo.age", 60);
    private final GreaterThanFilter ageGreaterThan65Filter = new GreaterThanFilter("personalInfo.age", 65);

    private final String json = "{\"firstName\": \"Jean-Luc\", \"lastName\": \"Picard\", \"personalInfo\": {\"age\":65}}";
    private final BooleanVisitor visitor = new BooleanVisitor(json);

    @Nested
    class and {

        @Test
        public void complexAndTestPasses() {
            final AndFilter andFilter = new AndFilter(ImmutableList.of(jeanLucNameMatcher, lastNameMatcher,
                    ageLessThan70Filter, ageGreaterThan60Filter));

            assertTrue(andFilter.accept(visitor));
        }

        @Test
        public void complexAndTestFails() {
            final AndFilter andFilter = new AndFilter(ImmutableList.of(jeanLucNameMatcher, lastNameMatcher,
                    ageLessThan70Filter, ageGreaterThan65Filter));

            assertFalse(andFilter.accept(visitor));
        }
    }

    @Nested
    class or {

        @Test
        public void complexOrTestPasses() {
            final OrFilter orFilter = new OrFilter(ImmutableList.of(jeanLucNameMatcher, wesleyNameMatcher,
                    worfNameMatcher));

            assertTrue(orFilter.accept(new BooleanVisitor(json)));
        }

        @Test
        public void complexOrTestFails() {
            final OrFilter orFilter = new OrFilter(ImmutableList.of(wesleyNameMatcher, worfNameMatcher,
                    rikerNameMatcher));

            assertFalse(orFilter.accept(new BooleanVisitor(json)));
        }
    }

    @Nested
    class andOr {

        @Test
        public void complexAndOrPasses() {
            final OrFilter orFilter = new OrFilter(ImmutableList.of(jeanLucNameMatcher, wesleyNameMatcher,
                    worfNameMatcher));
            final AndFilter andFilter = new AndFilter(ImmutableList.of(orFilter, lastNameMatcher,
                    ageLessThan70Filter, ageGreaterThan60Filter));

            assertTrue(andFilter.accept(visitor));
        }

        @Test
        public void complexAndOrFails() {
            final OrFilter orFilter = new OrFilter(ImmutableList.of(jeanLucNameMatcher, wesleyNameMatcher,
                    worfNameMatcher));
            final AndFilter andFilter = new AndFilter(ImmutableList.of(orFilter, lastNameMatcher,
                    ageLessThan70Filter, ageGreaterThan65Filter));

            assertFalse(andFilter.accept(visitor));
        }
    }

    @Nested
    class orAnd {

        @Test
        public void compleOrAndPasses() {
            final AndFilter andFilter = new AndFilter(ImmutableList.of(jeanLucNameMatcher, lastNameMatcher,
                    ageLessThan70Filter, ageGreaterThan60Filter));
            final OrFilter orFilter = new OrFilter(ImmutableList.of(wesleyNameMatcher,
                    worfNameMatcher, andFilter));

            assertTrue(orFilter.accept(visitor));
        }

        @Test
        public void complexOrAndFails() {
            final AndFilter andFilter = new AndFilter(ImmutableList.of(rikerNameMatcher, lastNameMatcher,
                    ageLessThan70Filter, ageGreaterThan60Filter));
            final OrFilter orFilter = new OrFilter(ImmutableList.of(wesleyNameMatcher,
                    worfNameMatcher, andFilter));

            assertTrue(orFilter.accept(visitor));
        }
    }
}
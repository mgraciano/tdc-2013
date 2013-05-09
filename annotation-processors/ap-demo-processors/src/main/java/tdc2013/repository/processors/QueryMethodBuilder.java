/*
 * Copyright (c) 2013, Klaus Boeing & Michel Graciano.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the project's authors nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS AND/OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package tdc2013.repository.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class QueryMethodBuilder {

    private String entityName;
    private String fluentQueryString;
    private final String RELATIONAL_OPERATORS_PATTERN = "Equal|Between|GreaterThan|GreaterThanEquals|IsNotNull|IsNull|LessThan|LessThanEquals|Like|NotEqual";
    private final String LOGICAL_OPERATORS_PATTERN = "And|Or";

    public QueryMethodBuilder(String entityName, String fluentQueryString) {
        this.entityName = entityName;
        this.fluentQueryString = fluentQueryString;
    }

    public String build() {
        StringBuilder queryBuilder;
        String normalizePattern = "(".concat(RELATIONAL_OPERATORS_PATTERN).concat("|").concat(LOGICAL_OPERATORS_PATTERN).concat(")");
        String normalizedString = fluentQueryString.replaceAll("findBy", "").replaceAll(normalizePattern, "|$1|");

        try (Scanner scanner = new Scanner(normalizedString)) {
            scanner.useDelimiter(Pattern.compile("\\|"));
            List<Expression> expressions = new ArrayList<>();
            Expression expression = null;
            while (scanner.hasNext()) {
                String value = scanner.next();
                System.out.println(value);
                if (value.isEmpty()) {
                    continue;
                }

                if (isRelationalOperator(value) && expression != null) {
                    expression.relationalOperator = value;
                } else if (isLogicalOperator(value) && expression != null) {
                    expression.logicalOperator = value;
                    expressions.add(expression);
                } else {
                    expression = new Expression(value);
                }
            }

            expressions.add(expression);

            queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT entity FROM ");
            queryBuilder.append(entityName);
            queryBuilder.append(" entity WHERE ");

            for (Expression exp : expressions) {
                queryBuilder.append(exp.getQueryExpression("entity"));
            }
        }

        return queryBuilder.toString();
    }

    private static class Expression {

        private String property;
        private String relationalOperator;
        private String logicalOperator;

        Expression(String property) {
            this.property = property.substring(0, 1).toLowerCase().concat(property.substring(1));
        }

        private String getQueryExpression(String alias) {
            StringBuilder builder = new StringBuilder();
            builder.append(alias);
            builder.append(".");
            builder.append(property);
            builder.append(" ");

            switch (relationalOperator) {
                case "Equal":
                    builder.append(" = ? ");
                    break;
                case "Between":
                    builder.append(" BETWEEN ? AND ? ");
                    break;
                case "GreaterThan":
                    builder.append(" > ? ");
                    break;
                case "GreaterThanEquals":
                    builder.append(" >= ? ");
                    break;
                case "IsNotNull":
                    builder.append(" IS NOT NULL ");
                    break;
                case "IsNull":
                    builder.append(" IS NULL ");
                    break;
                case "LessThan":
                    builder.append(" <? ");
                    break;
                case "LessThanEquals":
                    builder.append(" <=? ");
                    break;
                case "Like":
                    builder.append(" LIKE ? ");
                    break;
                case "NotEqual":
                    builder.append(" <> ? ");
                    break;
            }

            if (logicalOperator != null) {
                builder.append(" ");
                builder.append(logicalOperator);
                builder.append(" ");
            }

            return builder.toString();
        }
    }

    private boolean isRelationalOperator(String value) {
        return value.matches(RELATIONAL_OPERATORS_PATTERN);
    }

    private boolean isLogicalOperator(String value) {
        return value.matches(LOGICAL_OPERATORS_PATTERN);
    }
}

/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.apache.commons.lang;

import org.openrewrite.java.template.Matcher;
import org.openrewrite.java.tree.Expression;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.TypeUtils;

public class RepeatableArgumentMatcher implements Matcher<Expression> {
    /**
     * @param arg an argument to a method invocation
     * @return true if the argument is a simple getter that returns a String, or an identifier or field access
     */
    static boolean isRepeatableArgument(Expression arg) {
        if (arg instanceof J.Literal || arg instanceof J.Identifier || arg instanceof J.FieldAccess) {
            return true;
        }
        // Allow simple getters that return a String
        return arg instanceof J.MethodInvocation mi
                && mi.getSelect() instanceof J.Identifier
                && mi.getSimpleName().startsWith("get")
                && (mi.getArguments().isEmpty() || mi.getArguments().getFirst() instanceof J.Empty)
                && TypeUtils.isAssignableTo("java.lang.String", mi.getMethodType());
    }

    @Override
    public boolean matches(Expression expr) {
        return isRepeatableArgument(expr);
    }
}

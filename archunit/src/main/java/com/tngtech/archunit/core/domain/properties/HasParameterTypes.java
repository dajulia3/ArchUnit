/*
 * Copyright 2019 TNG Technology Consulting GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tngtech.archunit.core.domain.properties;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.tngtech.archunit.PublicAPI;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClassList;

import static com.tngtech.archunit.PublicAPI.Usage.ACCESS;
import static com.tngtech.archunit.base.DescribedPredicate.equalTo;
import static com.tngtech.archunit.core.domain.Formatters.formatMethodParameterTypeNames;
import static com.tngtech.archunit.core.domain.JavaClass.namesOf;
import static com.tngtech.archunit.core.domain.JavaClassList.GET_NAMES;

public interface HasParameterTypes {
    @PublicAPI(usage = ACCESS)
    JavaClassList getParameters();

    final class Predicates {
        private Predicates() {
        }

        @PublicAPI(usage = ACCESS)
        public static DescribedPredicate<HasParameterTypes> parameterTypes(final Class<?>... types) {
            return parameterTypes(namesOf(types));
        }

        @PublicAPI(usage = ACCESS)
        public static DescribedPredicate<HasParameterTypes> parameterTypes(final String... types) {
            return parameterTypes(ImmutableList.copyOf(types));
        }

        @PublicAPI(usage = ACCESS)
        public static DescribedPredicate<HasParameterTypes> parameterTypes(final List<String> typeNames) {
            return parameterTypes(equalTo(typeNames).onResultOf(GET_NAMES)
                    .as("[%s]", formatMethodParameterTypeNames(typeNames)));
        }

        @PublicAPI(usage = ACCESS)
        public static DescribedPredicate<HasParameterTypes> parameterTypes(final DescribedPredicate<JavaClassList> predicate) {
            return new ParameterTypesPredicate(predicate);
        }

        private static class ParameterTypesPredicate extends DescribedPredicate<HasParameterTypes> {
            private final DescribedPredicate<JavaClassList> predicate;

            ParameterTypesPredicate(DescribedPredicate<JavaClassList> predicate) {
                super("parameter types " + predicate.getDescription());
                this.predicate = predicate;
            }

            @Override
            public boolean apply(HasParameterTypes input) {
                return predicate.apply(input.getParameters());
            }
        }
    }
}
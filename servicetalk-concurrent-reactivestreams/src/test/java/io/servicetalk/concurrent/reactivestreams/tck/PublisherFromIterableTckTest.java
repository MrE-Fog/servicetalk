/*
 * Copyright © 2018 Apple Inc. and the ServiceTalk project authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.servicetalk.concurrent.reactivestreams.tck;

import io.servicetalk.concurrent.api.Publisher;

import org.testng.annotations.Test;

import static io.servicetalk.concurrent.api.Publisher.fromIterable;
import static io.servicetalk.concurrent.reactivestreams.tck.TckUtils.requestNToInt;
import static java.util.stream.IntStream.range;

@Test
public class PublisherFromIterableTckTest extends AbstractPublisherTckTest<Integer> {
    @Override
    protected Publisher<Integer> createServiceTalkPublisher(final long elements) {
        return fromIterable(() -> range(0, requestNToInt(elements)).iterator());
    }

    @Override
    public long maxElementsFromPublisher() {
        return TckUtils.maxElementsFromPublisher();
    }
}

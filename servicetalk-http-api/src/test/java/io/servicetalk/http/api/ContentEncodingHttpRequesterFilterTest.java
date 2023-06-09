/*
 * Copyright © 2022 Apple Inc. and the ServiceTalk project authors
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
package io.servicetalk.http.api;

import io.servicetalk.buffer.api.Buffer;
import io.servicetalk.concurrent.api.Publisher;
import io.servicetalk.concurrent.api.Single;
import io.servicetalk.encoding.api.BufferDecoderGroupBuilder;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static io.servicetalk.buffer.netty.BufferAllocators.DEFAULT_ALLOCATOR;
import static io.servicetalk.concurrent.api.Publisher.from;
import static io.servicetalk.concurrent.api.Single.failed;
import static io.servicetalk.encoding.api.Identity.identityEncoder;
import static io.servicetalk.encoding.netty.NettyBufferEncoders.gzipDefault;
import static io.servicetalk.http.api.HttpHeaderNames.CONTENT_ENCODING;
import static io.servicetalk.http.api.HttpHeaderNames.CONTENT_LENGTH;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ContentEncodingHttpRequesterFilterTest extends AbstractHttpRequesterFilterTest {
    @ParameterizedTest(name = "{displayName} [{index}] {0}-{1}")
    @MethodSource("requesterTypes")
    void contentLengthRemovedOnEncode(final RequesterType type, final SecurityType security) throws Exception {
        setUp(security);
        StreamingHttpRequester filter = createFilter(type, (respFactory, request) -> {
            try {
                assertContentLength(request.headers(), request.payloadBody());
            } catch (Exception e) {
                return failed(e);
            }

            // Simulate the server compressed reply, with CONTENT_LENGTH included.
            String responseStr = "aaaaaaaaaaaaaaaa";
            Buffer responseBuf = DEFAULT_ALLOCATOR.fromAscii(responseStr);
            Buffer encoded = gzipDefault().encoder().serialize(responseBuf, DEFAULT_ALLOCATOR);
            return Single.succeeded(respFactory.ok().payloadBody(from(encoded))
                    .addHeader(CONTENT_LENGTH, String.valueOf(encoded.readableBytes()))
                    .addHeader(CONTENT_ENCODING, gzipDefault().encodingName()));
        }, new ContentEncodingHttpRequesterFilter(new BufferDecoderGroupBuilder()
                .add(gzipDefault(), true)
                .add(identityEncoder(), false)
                .build()));

        // Simulate the user (or earlier filter) setting the content length before compression.
        StreamingHttpRequest request = filter.post("/foo");
        String payloadBody = "bbbbbbbbbbbbbbbbbbb";
        request.payloadBody(from(filter.executionContext().bufferAllocator().fromAscii(payloadBody)));
        request.headers().add(CONTENT_LENGTH, String.valueOf(payloadBody.length()));
        request.contentEncoding(gzipDefault());
        StreamingHttpResponse response = filter.request(request).toFuture().get();
        assertContentLength(response.headers(), response.payloadBody());
    }

    static void assertContentLength(HttpHeaders headers, Publisher<Buffer> publisher) throws Exception {
        CharSequence reqLen = headers.get(CONTENT_LENGTH);
        if (reqLen != null) {
            final int len = publisher.collect(() -> 0, (sum, buff) -> sum + buff.readableBytes()).toFuture().get();
            assertThat(Integer.parseInt(reqLen.toString()), equalTo(len));
        }
    }
}

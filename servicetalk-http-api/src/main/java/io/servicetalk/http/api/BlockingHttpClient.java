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
package io.servicetalk.http.api;

/**
 * The equivalent of {@link HttpClient} but with synchronous/blocking APIs instead of asynchronous APIs.
 */
public interface BlockingHttpClient extends BlockingHttpRequester {
    /**
     * Reserve a {@link BlockingHttpConnection} based on provided {@link HttpRequestMetaData}.
     * <p>
     * If a new connection should be opened instead of potentially reusing an already established one, the
     * {@link HttpContextKeys#HTTP_FORCE_NEW_CONNECTION} must be set.
     *
     * @param metaData Allows the underlying layers to know what {@link BlockingHttpConnection}s are valid to
     * reserve for future {@link HttpRequest requests} with the same {@link HttpRequestMetaData}.
     * For example this may provide some insight into shard or other info.
     * @return a {@link ReservedBlockingHttpConnection}.
     * @throws Exception if a exception occurs during the reservation process.
     * @see HttpContextKeys#HTTP_FORCE_NEW_CONNECTION
     */
    ReservedBlockingHttpConnection reserveConnection(HttpRequestMetaData metaData) throws Exception;

    /**
     * Convert this {@link BlockingHttpClient} to the {@link StreamingHttpClient} API.
     *
     * @return a {@link StreamingHttpClient} representation of this {@link BlockingHttpClient}.
     */
    StreamingHttpClient asStreamingClient();

    /**
     * Convert this {@link BlockingHttpClient} to the {@link HttpClient} API.
     *
     * @return a {@link HttpClient} representation of this {@link BlockingHttpClient}.
     */
    default HttpClient asClient() {
        return asStreamingClient().asClient();
    }

    /**
     * Convert this {@link BlockingHttpClient} to the {@link BlockingStreamingHttpClient} API.
     *
     * @return a {@link BlockingStreamingHttpClient} representation of this {@link BlockingHttpClient}.
     */
    default BlockingStreamingHttpClient asBlockingStreamingClient() {
        return asStreamingClient().asBlockingStreamingClient();
    }
}

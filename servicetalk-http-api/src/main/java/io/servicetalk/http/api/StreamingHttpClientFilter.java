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

import io.servicetalk.concurrent.api.Completable;
import io.servicetalk.concurrent.api.Single;

import static java.util.Objects.requireNonNull;

/**
 * A {@link StreamingHttpClient} that delegates all methods to a different {@link StreamingHttpClient}.
 *
 * @see StreamingHttpClientFilterFactory
 */
public class StreamingHttpClientFilter implements FilterableStreamingHttpClient {
    private final FilterableStreamingHttpClient delegate;

    /**
     * Create a new instance.
     *
     * @param delegate The {@link FilterableStreamingHttpClient} to delegate all calls to.
     */
    protected StreamingHttpClientFilter(final FilterableStreamingHttpClient delegate) {
        this.delegate = requireNonNull(delegate);
    }

    @Override
    public final Single<StreamingHttpResponse> request(final StreamingHttpRequest request) {
        return request(delegate, request);
    }

    @Override
    public Single<? extends FilterableReservedStreamingHttpConnection> reserveConnection(
            final HttpRequestMetaData metaData) {
        return delegate.reserveConnection(metaData).map(ClientFilterToReservedConnectionFilter::new);
    }

    @Override
    public HttpExecutionContext executionContext() {
        return delegate.executionContext();
    }

    @Override
    public StreamingHttpResponseFactory httpResponseFactory() {
        return delegate.httpResponseFactory();
    }

    @Override
    public Completable onClose() {
        return delegate.onClose();
    }

    @Override
    public Completable onClosing() {
        return delegate.onClosing();
    }

    @Override
    public Completable closeAsync() {
        return delegate.closeAsync();
    }

    @Override
    public Completable closeAsyncGracefully() {
        return delegate.closeAsyncGracefully();
    }

    @Override
    public StreamingHttpRequest newRequest(final HttpRequestMethod method, final String requestTarget) {
        return delegate.newRequest(method, requestTarget);
    }

    @Override
    public String toString() {
        return getClass().getName() + '(' + delegate + ')';
    }

    /**
     * Get the {@link FilterableStreamingHttpClient} this method delegates to.
     *
     * @return the {@link FilterableStreamingHttpClient} this method delegates to.
     */
    protected final FilterableStreamingHttpClient delegate() {
        return delegate;
    }

    /**
     * Called when the filter needs to delegate the request using the provided {@link StreamingHttpRequester} on
     * which to call {@link StreamingHttpRequester#request(StreamingHttpRequest)}.
     *
     * @param delegate The {@link StreamingHttpRequester} to delegate requests to.
     * @param request The request to delegate.
     * @return the response.
     */
    // An overload that takes StreamingHttpRequester as an argument helps to delegate to a reserved connection, applying
    // the business logic of this filter.
    protected Single<StreamingHttpResponse> request(final StreamingHttpRequester delegate,
                                                    final StreamingHttpRequest request) {
        return delegate.request(request);
    }

    private final class ClientFilterToReservedConnectionFilter extends ReservedStreamingHttpConnectionFilter {
        ClientFilterToReservedConnectionFilter(final FilterableReservedStreamingHttpConnection delegate) {
            super(delegate);
        }

        @Override
        public Single<StreamingHttpResponse> request(final StreamingHttpRequest request) {
            return StreamingHttpClientFilter.this.request(delegate(), request);
        }
    }
}

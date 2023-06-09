/*
 * Copyright © 2018-2019, 2021 Apple Inc. and the ServiceTalk project authors
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

import io.servicetalk.context.api.ContextMap;
import io.servicetalk.encoding.api.ContentCodec;

import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;

class DefaultHttpResponseMetaData extends AbstractHttpMetaData implements HttpResponseMetaData {

    private HttpResponseStatus status;

    DefaultHttpResponseMetaData(final HttpResponseStatus status, final HttpProtocolVersion version,
                                final HttpHeaders headers, @Nullable final ContextMap context) {
        super(version, headers, context);
        this.status = requireNonNull(status);
    }

    @Override
    public HttpResponseMetaData version(final HttpProtocolVersion version) {
        super.version(version);
        return this;
    }

    @Override
    public final HttpResponseStatus status() {
        return status;
    }

    @Override
    public HttpResponseMetaData status(final HttpResponseStatus status) {
        this.status = requireNonNull(status);
        return this;
    }

    @Deprecated
    @Override
    public HttpResponseMetaData encoding(final ContentCodec encoding) {
        super.encoding(encoding);
        return this;
    }

    @Override
    public HttpResponseMetaData context(final ContextMap context) {
        super.context(context);
        return this;
    }

    @Override
    public final String toString() {
        return version() + " " + status();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final DefaultHttpResponseMetaData that = (DefaultHttpResponseMetaData) o;

        return status.equals(that.status);
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + status.hashCode();
    }
}

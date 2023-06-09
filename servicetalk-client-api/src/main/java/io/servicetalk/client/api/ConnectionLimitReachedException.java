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
package io.servicetalk.client.api;

import io.servicetalk.client.api.LimitingConnectionFactoryFilter.ConnectionLimiter;
import io.servicetalk.transport.api.RetryableException;

import java.net.ConnectException;

/**
 * Thrown when the number of connections reached their limit for a given resource (i.e. a host)
 * depending on the context.
 *
 * @see ConnectionLimiter#newConnectionRefusedException(Object)
 * @see LimitingConnectionFactoryFilter#withMax(int)
 */
public class ConnectionLimitReachedException extends ConnectException implements RetryableException {

    private static final long serialVersionUID = 645105614301638032L;

    /**
     * Creates a new instance.
     *
     * @param message the detail message.
     */
    public ConnectionLimitReachedException(final String message) {
        super(message);
    }
}

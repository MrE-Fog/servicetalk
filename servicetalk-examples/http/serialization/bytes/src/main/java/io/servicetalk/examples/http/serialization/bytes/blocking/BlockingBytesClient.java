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
package io.servicetalk.examples.http.serialization.bytes.blocking;

import io.servicetalk.http.api.BlockingHttpClient;
import io.servicetalk.http.api.HttpResponse;
import io.servicetalk.http.netty.HttpClients;

import static io.servicetalk.examples.http.serialization.bytes.SerializerUtils.SERIALIZER;
import static java.nio.charset.StandardCharsets.UTF_8;

public final class BlockingBytesClient {
    public static void main(String[] args) throws Exception {
        try (BlockingHttpClient client = HttpClients.forSingleAddress("localhost", 8080).buildBlocking()) {
            HttpResponse resp = client.request(client.get("/bytes"));
            System.out.println(resp.toString((name, value) -> value));
            final byte[] bytes = resp.payloadBody(SERIALIZER); // Deserialize the whole payload body as a byte[].
            System.out.println(new String(bytes, UTF_8)); // convert to String just to print to console.
        }
    }
}

/**
 * Copyright 2015-2016 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wildfly.swarm.webservices;

import org.wildfly.swarm.config.Webservices;
import org.wildfly.swarm.config.webservices.EndpointConfig;
import org.wildfly.swarm.spi.api.Fraction;
import org.wildfly.swarm.spi.api.annotations.MarshalDMR;
import org.wildfly.swarm.spi.api.annotations.WildFlyExtension;

@WildFlyExtension(module = "org.jboss.as.webservices")
@MarshalDMR
public class WebServicesFraction extends Webservices<WebServicesFraction> implements Fraction<WebServicesFraction> {

    public static WebServicesFraction createDefaultFraction() {
        return new WebServicesFraction().applyDefaults();
    }

    @Override
    public WebServicesFraction applyDefaults() {
        endpointConfig(STANDARD_ENDPOINT_CONFIG);
        endpointConfig(RECORDING_ENDPOINT_CONFIG, this::configureRemoteEndpoint);
        clientConfig(STANDARD_CLIENT_CONFIG);

        return this;
    }

    private final void configureRemoteEndpoint(EndpointConfig<?> endpoint) {
        endpoint.preHandlerChain(RECORDING_HANDLERS, (chain) ->
                chain.protocolBindings(SOAP_PROTOCOLS)
                        .handler(RECORDING_HANDLER, (handler) ->
                                handler.attributeClass(RECORDING_HANDLER_CLASS)));
    }

    private static final String STANDARD_ENDPOINT_CONFIG = "Standard-Endpoint-Config";

    private static final String RECORDING_ENDPOINT_CONFIG = "Recording-Endpoint-Config";

    private static final String RECORDING_HANDLERS = "recording-handlers";

    private static final String SOAP_PROTOCOLS = "##SOAP11_HTTP ##SOAP11_HTTP_MTOM ##SOAP12_HTTP ##SOAP12_HTTP_MTOM";

    private static final String RECORDING_HANDLER = "RecordingHandler";

    private static final String RECORDING_HANDLER_CLASS = "org.jboss.ws.common.invocation.RecordingServerHandler";

    private static final String STANDARD_CLIENT_CONFIG = "Standard-Client-Config";
}

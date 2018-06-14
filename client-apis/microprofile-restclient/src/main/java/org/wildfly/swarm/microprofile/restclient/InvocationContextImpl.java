/**
 * Copyright 2018 Red Hat, Inc, and individual contributors.
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
package org.wildfly.swarm.microprofile.restclient;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.spi.InterceptionType;
import javax.enterprise.inject.spi.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.client.ResponseProcessingException;

/**
 *
 * @author Martin Kouba
 */
class InvocationContextImpl implements InvocationContext {

    private final Object target;

    private final Method method;

    private Object[] args;

    private int position;

    private final Map<String, Object> contextData;

    private final List<InterceptorInvocation> chain;

    /**
     * @param target
     * @param method
     * @param args
     * @param chain
     */
    InvocationContextImpl(Object target, Method method, Object[] args, List<InterceptorInvocation> chain) {
        this.target = target;
        this.method = method;
        this.args = args;
        this.contextData = new HashMap<>();
        this.position = 0;
        this.chain = chain;
    }

    boolean hasNextInterceptor() {
        return position < chain.size();
    }

    protected Object invokeNext() throws Exception {
        int oldPosition = position;
        try {
            return chain.get(position++).invoke(this);
        } finally {
            position = oldPosition;
        }
    }

    protected Object interceptorChainCompleted() throws Exception {
        try {
            return method.invoke(target, args);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof ResponseProcessingException) {
                ResponseProcessingException rpe = (ResponseProcessingException) e.getCause();
                // Note that the default client engine leverages a single connection
                // MP FT: we need to close the response otherwise we would not be able to retry if the method returns javax.ws.rs.core.Response
                rpe.getResponse().close();
                Throwable cause = rpe.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                }
            }
            throw e;
        }
    }

    @Override
    public Object proceed() throws Exception {
        try {
            if (hasNextInterceptor()) {
                return invokeNext();
            } else {
                return interceptorChainCompleted();
            }
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            if (cause instanceof Exception) {
                throw (Exception) cause;
            }
            throw new RuntimeException(cause);
        }
    }

    @Override
    public Object getTarget() {
        return target;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Constructor<?> getConstructor() {
        return null;
    }

    @Override
    public Object[] getParameters() throws IllegalStateException {
        return args;
    }

    @Override
    public void setParameters(Object[] params) throws IllegalStateException, IllegalArgumentException {
        this.args = params;
    }

    @Override
    public Map<String, Object> getContextData() {
        return contextData;
    }

    @Override
    public Object getTimer() {
        return null;
    }

    static class InterceptorInvocation {

        @SuppressWarnings("rawtypes")
        private final Interceptor interceptor;

        private final Object interceptorInstance;

        public InterceptorInvocation(Interceptor<?> interceptor, Object interceptorInstance) {
            this.interceptor = interceptor;
            this.interceptorInstance = interceptorInstance;
        }

        @SuppressWarnings("unchecked")
        Object invoke(InvocationContext ctx) throws Exception {
            return interceptor.intercept(InterceptionType.AROUND_INVOKE, interceptorInstance, ctx);
        }

    }

}

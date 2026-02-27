/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package io.github.naveenb2004.socks5.base.configuration;

import io.github.naveenb2004.socks5.base.exception.SOCKS5Exception;

public abstract class AbstractSOCKS5Configuration {
    private final int internalBufferSize;
    private final int connectionTimeout;

    protected AbstractSOCKS5Configuration(final int internalBufferSize,
                                          final int connectionTimeout) {
        this.internalBufferSize = internalBufferSize;
        this.connectionTimeout = connectionTimeout;
    }

    public int getInternalBufferSize() {
        return internalBufferSize;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public static abstract class SOCKS5ConfigurationBuilder {
        protected int internalBufferSize = 10_240; // 10KB
        protected int connectionTimeout = 0;

        protected SOCKS5ConfigurationBuilder() {
        }

        public SOCKS5ConfigurationBuilder internalBufferSize(final int internalBufferSize) {
            if (internalBufferSize <= 0) throw new SOCKS5Exception("Config error: internalBufferSize must be positive");
            this.internalBufferSize = internalBufferSize;
            return this;
        }

        public SOCKS5ConfigurationBuilder connectionTimeout(final int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public abstract AbstractSOCKS5Configuration build();
    }
}

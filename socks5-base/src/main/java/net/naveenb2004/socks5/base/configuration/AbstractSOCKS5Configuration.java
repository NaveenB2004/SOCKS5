/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package net.naveenb2004.socks5.base.configuration;

import net.naveenb2004.socks5.base.exception.SOCKS5Exception;

public abstract class AbstractSOCKS5Configuration {
    private final int internalBufferSize;
    private final int connectionTimeout;

    protected AbstractSOCKS5Configuration(final int internalBufferSize,
                                          final int connectionTimeout) {
        this.internalBufferSize = internalBufferSize;
        this.connectionTimeout = connectionTimeout;
    }

    public final int getInternalBufferSize() {
        return internalBufferSize;
    }

    public final int getConnectionTimeout() {
        return connectionTimeout;
    }

    public static abstract class SOCKS5ConfigurationBuilder {
        protected int internalBufferSize = 10_240; // 10KB
        protected int connectionTimeout = 0;

        protected SOCKS5ConfigurationBuilder() {
        }

        public final SOCKS5ConfigurationBuilder internalBufferSize(final int internalBufferSize) {
            if (internalBufferSize <= 0) throw new SOCKS5Exception("Config error: internalBufferSize must be positive");
            this.internalBufferSize = internalBufferSize;
            return this;
        }

        public final SOCKS5ConfigurationBuilder connectionTimeout(final int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public abstract AbstractSOCKS5Configuration build();
    }
}

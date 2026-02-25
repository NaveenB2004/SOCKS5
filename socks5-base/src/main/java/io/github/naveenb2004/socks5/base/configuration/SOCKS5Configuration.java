/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package io.github.naveenb2004.socks5.base.configuration;

public abstract class SOCKS5Configuration {
    public static final int VERSION = 0x05;

    private final int internalBufferSize;

    protected SOCKS5Configuration(final int internalBufferSize) {
        this.internalBufferSize = internalBufferSize;
    }

    public int getInternalBufferSize() {
        return internalBufferSize;
    }

    public static abstract class SOCKS5ConfigurationBuilder {
        protected int internalBufferSize = 10_240; // 10KB

        protected SOCKS5ConfigurationBuilder() {
        }

        public SOCKS5ConfigurationBuilder internalBufferSize(final int internalBufferSize) {
            this.internalBufferSize = internalBufferSize;
            return this;
        }

        public abstract SOCKS5Configuration build();
    }
}

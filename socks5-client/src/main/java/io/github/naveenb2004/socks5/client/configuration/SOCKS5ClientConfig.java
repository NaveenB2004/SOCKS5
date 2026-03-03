/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package io.github.naveenb2004.socks5.client.configuration;

import io.github.naveenb2004.socks5.base.configuration.AbstractSOCKS5Configuration;

public final class SOCKS5ClientConfig extends AbstractSOCKS5Configuration {
    private SOCKS5ClientConfig(final int internalBufferSize,
                               final int connectionTimeout) {
        super(internalBufferSize, connectionTimeout);
    }

    public static SOCKS5ClientConfigurationBuilder builder() {
        return new SOCKS5ClientConfigurationBuilder();
    }

    public static final class SOCKS5ClientConfigurationBuilder extends SOCKS5ConfigurationBuilder {

        private SOCKS5ClientConfigurationBuilder() {
            super();
        }

        @Override
        public AbstractSOCKS5Configuration build() {
            return new SOCKS5ClientConfig(
                    super.internalBufferSize,
                    super.connectionTimeout
            );
        }
    }
}

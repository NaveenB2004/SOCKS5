/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package io.github.naveenb2004.socks5.server.configuration;

import io.github.naveenb2004.socks5.base.configuration.AbstractSOCKS5Configuration;

public final class SOCKS5ServerConfiguration extends AbstractSOCKS5Configuration {
    private SOCKS5ServerConfiguration(final int internalBufferSize) {
        super(internalBufferSize);
    }

    public static SOCKS5ServerConfigurationBuilder builder() {
        return new SOCKS5ServerConfigurationBuilder();
    }

    public static class SOCKS5ServerConfigurationBuilder extends SOCKS5ConfigurationBuilder {

        private SOCKS5ServerConfigurationBuilder() {
            super();
        }

        @Override
        public SOCKS5ServerConfiguration build() {
            return new SOCKS5ServerConfiguration(
                    super.internalBufferSize
            );
        }
    }
}

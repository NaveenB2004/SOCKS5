/*
 * Copyright (c) 2026 Naveen N. Balasooriya
 *
 * This source code is licensed under the MIT License.
 * See the LICENSE file in the project root for full license information.
 */

package io.github.naveenb2004.socks5.client.configuration;

import io.github.naveenb2004.socks5.base.configuration.SOCKS5Configuration;

public final class SOCKS5ClientConfiguration extends SOCKS5Configuration {
    private SOCKS5ClientConfiguration(final int internalBufferSize) {
        super(internalBufferSize);
    }

    public static SOCKS5ClientConfigurationBuilder builder() {
        return new SOCKS5ClientConfigurationBuilder();
    }

    public static class SOCKS5ClientConfigurationBuilder extends SOCKS5ConfigurationBuilder {

        private SOCKS5ClientConfigurationBuilder() {
            super();
        }

        @Override
        public SOCKS5Configuration build() {
            return new SOCKS5ClientConfiguration(
                    super.internalBufferSize
            );
        }
    }
}

package com.botoni.flow.data.models;

import java.util.UUID;

public final class Configuration {
    public final String host;
    public final String port;
    public final String username;
    public final String site;
    public final String applicationId;
    public final String applicationName;

    private Configuration(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.username = builder.username;
        this.site = builder.site;
        this.applicationId = builder.applicationId;
        this.applicationName = builder.applicationName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String host, port, username, site, applicationId, applicationName;

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(String port) {
            this.port = port;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder site(String site) {
            this.site = site;
            return this;
        }

        public Builder applicationId(String applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        public Builder applicationName(String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        public Configuration build() {
            return new Configuration(this);
        }
    }
}
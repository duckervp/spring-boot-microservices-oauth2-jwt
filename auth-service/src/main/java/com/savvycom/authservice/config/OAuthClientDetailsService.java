package com.savvycom.authservice.config;

import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;


public class OAuthClientDetailsService extends JdbcClientDetailsService {
    public OAuthClientDetailsService(DataSource dataSource, ServiceConfig serviceConfig) {
        super(dataSource);
        StringBuilder sql = new StringBuilder()
                .append("SELECT username AS client_id, password AS client_secret, NULL AS resource_ids, 'ui' AS scope, ")
                .append("'client_credentials' AS authorized_grant_types, ")
                .append("NULL AS web_server_redirect_uri, role AS authorities, ")
                .append(serviceConfig.getAccessTokenValiditySeconds())
                .append(" AS access_token_validity, ")
                .append(serviceConfig.getRefreshTokenValiditySeconds())
                .append(" AS refresh_token_validity, ")
                .append("NULL AS additional_information, NULL AS autoapprove ")
                .append("FROM db_user2.user where id = ?");
        this.setSelectClientDetailsSql(sql.toString());
    }
}

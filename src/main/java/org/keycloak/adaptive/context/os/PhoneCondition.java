package org.keycloak.adaptive.context.os;

import org.keycloak.adaptive.context.ContextUtils;
import org.keycloak.adaptive.context.DeviceContext;
import org.keycloak.adaptive.context.DeviceContextFactory;
import org.keycloak.adaptive.spi.policy.Operation;
import org.keycloak.adaptive.spi.policy.UserContextCondition;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import java.util.Set;

public class PhoneCondition implements UserContextCondition, ConditionalAuthenticator {
    private final KeycloakSession session;
    private final DeviceContext deviceContext;
    private final Set<Operation<DeviceContext>> rules;

    public PhoneCondition(KeycloakSession session, Set<Operation<DeviceContext>> rules) {
        this.session = session;
        this.deviceContext = ContextUtils.getContext(session, DeviceContext.class, DeviceContextFactory.PROVIDER_ID);
        this.rules = rules;
    }

    @Override
    public void close() {
    }

    @Override
    public boolean matchCondition(AuthenticationFlowContext context) {
        AuthenticatorConfigModel authConfig = context.getAuthenticatorConfig();
        if (authConfig != null) {
            boolean isMobile = Boolean.parseBoolean(authConfig.getConfig().get(PhoneConditionFactory.IS_MOBILE_CONF));
            return rules.stream().allMatch(f -> f.match(deviceContext, Boolean.toString(isMobile)));
        }
        return false;
    }

    @Override
    public void action(AuthenticationFlowContext context) {

    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {

    }
}

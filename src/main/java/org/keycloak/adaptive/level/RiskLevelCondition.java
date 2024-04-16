package org.keycloak.adaptive.level;

import org.jboss.logging.Logger;
import org.keycloak.adaptive.spi.engine.RiskEngine;
import org.keycloak.adaptive.spi.level.RiskLevelsProvider;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.utils.StringUtil;

import java.util.Optional;

public class RiskLevelCondition implements ConditionalAuthenticator {
    private static final Logger logger = Logger.getLogger(RiskLevelCondition.class);

    @Override
    public boolean matchCondition(AuthenticationFlowContext context) {
        final AuthenticatorConfigModel authConfig = context.getAuthenticatorConfig();

        if (authConfig != null) {
            var risk = RiskEngine.getStoredRisk(context)
                    .orElseThrow(() -> new IllegalStateException("No risk has been evaluated. Did you forget to add Risk Engine authenticator to the flow?"));

            var riskLevelProvider = context.getSession().getProvider(RiskLevelsProvider.class, SimpleRiskLevelsFactory.PROVIDER_ID);
            if (riskLevelProvider == null) {
                logger.errorf("Cannot find risk level provider '%s'", SimpleRiskLevelsFactory.PROVIDER_ID);
                throw new IllegalStateException("Risk Level Provider is not found");
            }

            var level = Optional.ofNullable(authConfig.getConfig().get(RiskLevelConditionFactory.LEVEL_CONFIG))
                    .filter(StringUtil::isNotBlank)
                    .flatMap(f -> riskLevelProvider.getRiskLevels().stream().filter(g -> g.getName().equals(f)).findAny())
                    .orElseThrow(() -> new IllegalStateException("Cannot find specified level for provider: " + SimpleRiskLevelsFactory.PROVIDER_ID));

            var matches = level.matchesRisk(risk);

            if (matches) {
                logger.debugf("Risk Level Condition (%s) matches the evaluated level: %f <= %f <= %f", level.getName(), level.getLowestRiskValue(), risk, level.getHighestRiskValue());
                return true;
            } else {
                logger.tracef("Risk Level Condition (%s) DOES NOT MATCH the evaluated level: %f", level.getName(), risk);
                return false;
            }
        }
        return false;
    }

    @Override
    public void action(AuthenticationFlowContext context) {

    }

    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {

    }

    @Override
    public void close() {

    }
}

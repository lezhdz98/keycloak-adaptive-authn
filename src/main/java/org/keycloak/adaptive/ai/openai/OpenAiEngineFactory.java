/*
 * Copyright 2024 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.keycloak.adaptive.ai.openai;

import org.keycloak.Config;
import org.keycloak.adaptive.spi.ai.AiEngineFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class OpenAiEngineFactory implements AiEngineFactory {
    public static final String PROVIDER_ID = "default";
    public static final String DEFAULT_MODEL = "gpt-3.5-turbo";
    public static final String DEFAULT_URL = "https://api.openai.com/v1/chat/completions";

    static final String URL_PROPERTY = "OPEN_AI_API_URL";
    static final String KEY_PROPERTY = "OPEN_AI_API_KEY";
    static final String ORGANIZATION_PROPERTY = "OPEN_AI_API_ORGANIZATION";
    static final String PROJECT_PROPERTY = "OPEN_AI_API_PROJECT";
    static final String MODEL_PROPERTY = "OPEN_AI_API_MODEL";

    @Override
    public OpenAiEngine create(KeycloakSession session) {
        return new OpenAiEngine(session);
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public void init(Config.Scope config) {

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {

    }
}

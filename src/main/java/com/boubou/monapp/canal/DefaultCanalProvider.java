package com.boubou.monapp.canal;

import java.util.Arrays;
import java.util.List;

/**
 * Fournit les canaux de notification par défaut (Email et Console).
 */
public class DefaultCanalProvider implements CanalProvider {
    @Override
    public List<Canal> getCanaux() {
        return Arrays.asList(new EmailCanal(), new ConsoleCanal());
    }
}
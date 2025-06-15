package com.boubou.monapp.canal;

import java.util.List;

/**
 * Fournit une liste de canaux de notification disponibles.
 */
public interface CanalProvider {
    List<Canal> getCanaux();
}
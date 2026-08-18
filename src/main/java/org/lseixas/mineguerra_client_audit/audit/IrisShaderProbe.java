package org.lseixas.mineguerra_client_audit.audit;

import net.fabricmc.loader.api.FabricLoader;
import org.lseixas.mineguerra_client_audit.MineguerraClientAuditMod;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Set;

/**
 * Lê o shader Iris selecionado sem dependência hard no mod.
 */
final class IrisShaderProbe {

    private static final Set<String> OFF_NAMES = Set.of(
            "",
            "off",
            "(off)",
            "internal",
            "(internal)",
            "none",
            "(none)"
    );

    private IrisShaderProbe() {
    }

    static String currentShaderName() {
        if (!FabricLoader.getInstance().isModLoaded("iris")) {
            return "";
        }
        if (!isShaderPackInUse()) {
            return "";
        }
        try {
            Class<?> iris = Class.forName("net.irisshaders.iris.Iris");
            Method method = iris.getMethod("getCurrentPackName");
            Object name = method.invoke(null);
            if (name == null) {
                return "";
            }
            String raw = name.toString().trim();
            return isOffName(raw) ? "" : raw;
        } catch (ReflectiveOperationException ex) {
            MineguerraClientAuditMod.LOGGER.debug("Nao foi possivel ler shader Iris", ex);
            return "";
        }
    }

    private static boolean isShaderPackInUse() {
        try {
            Class<?> api = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
            Object instance = api.getMethod("getInstance").invoke(null);
            Object inUse = api.getMethod("isShaderPackInUse").invoke(instance);
            return Boolean.TRUE.equals(inUse);
        } catch (ReflectiveOperationException ex) {
            return true;
        }
    }

    static boolean isOffName(String name) {
        return OFF_NAMES.contains(name.toLowerCase(Locale.ROOT));
    }
}

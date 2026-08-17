package org.lseixas.mineguerra_client_audit.audit;

import net.fabricmc.loader.api.FabricLoader;
import org.lseixas.mineguerra_client_audit.MineguerraClientAuditMod;

import java.lang.reflect.Method;

/**
 * Lê o shader Iris selecionado sem dependência hard no mod.
 */
final class IrisShaderProbe {

    private IrisShaderProbe() {
    }

    static String currentShaderName() {
        if (!FabricLoader.getInstance().isModLoaded("iris")) {
            return "";
        }
        try {
            Class<?> iris = Class.forName("net.irisshaders.iris.Iris");
            Method method = iris.getMethod("getCurrentPackName");
            Object name = method.invoke(null);
            return name == null ? "" : name.toString();
        } catch (ReflectiveOperationException ex) {
            MineguerraClientAuditMod.LOGGER.debug("Nao foi possivel ler shader Iris", ex);
            return "";
        }
    }
}

package nomowanderer.registry;

import nomowanderer.registry.services.RegistryFactory;
import nomowanderer.registry.services.RegistryUtil;

import java.util.ServiceLoader;

public class Services {

  public static final RegistryUtil REGISTRY_UTIL = load(RegistryUtil.class);
  public static final RegistryFactory REGISTRY_FACTORY = load(RegistryFactory.class);

  public static <T> T load(Class<T> c) {
      return ServiceLoader.load(c)
        .findFirst()
        .orElseThrow(
            () -> new NullPointerException("Failed to load service: " + c.getName()));
  }
}

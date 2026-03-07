package net.liukrast.compat;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.fml.javafmlmod.AutomaticEventSubscriber;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.LoadingModList;

import java.lang.annotation.ElementType;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * This class is internal. Please read and use {@link Compat}
 * */
@Mod("compat_manager")
public class CompatManager {
    public CompatManager() {
        ModList.get().forEachModContainer((id, container) -> {
            var bus = container.getEventBus();
            if(bus == null) return;
            bus.addListener((Consumer<FMLConstructModEvent>) event -> init(container));
        });
    }

    private static void init(ModContainer container) {
        assert container.getEventBus() != null;
        container.getModInfo().getOwningFile().getFile().getScanResult().getAnnotatedBy(Compat.class, ElementType.TYPE)
                .forEach(data -> {
                    if(LoadingModList.get().getModFileById((String)data.annotationData().get("value")) == null) return;
                    if(AutomaticEventSubscriber.getSides(data.annotationData().get("dist")).contains(FMLLoader.getDist())) return;
                    try {
                        Class<?> clazz = Class.forName(data.memberName());

                        try {
                            var constructors = clazz.getConstructors();
                            if (constructors.length != 1) {
                                throw new RuntimeException("Compat class " + clazz + " must have exactly 1 public constructor, found " + constructors.length);
                            }
                            var constructor = constructors[0];

                            // Allowed arguments for injection via constructor
                            Map<Class<?>, Object> allowedConstructorArgs = Map.of(
                                    IEventBus.class, container.getEventBus(),
                                    ModContainer.class, container,
                                    FMLModContainer.class, container,
                                    Dist.class, FMLLoader.getDist());

                            var parameterTypes = constructor.getParameterTypes();
                            Object[] constructorArgs = new Object[parameterTypes.length];
                            Set<Class<?>> foundArgs = new HashSet<>();

                            for (int i = 0; i < parameterTypes.length; i++) {
                                Object argInstance = allowedConstructorArgs.get(parameterTypes[i]);
                                if (argInstance == null) {
                                    throw new RuntimeException("Compat constructor has unsupported argument " + parameterTypes[i] + ". Allowed optional argument classes: " +
                                            allowedConstructorArgs.keySet().stream().map(Class::getSimpleName).collect(Collectors.joining(", ")));
                                }

                                if (foundArgs.contains(parameterTypes[i])) {
                                    throw new RuntimeException("Duplicate compat constructor argument type: " + parameterTypes[i]);
                                }

                                foundArgs.add(parameterTypes[i]);
                                constructorArgs[i] = argInstance;
                            }

                            // All arguments are found
                            constructor.newInstance(constructorArgs);
                        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                            throw new RuntimeException("Failed to create compat instance " + clazz.getName(), e);
                        }
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException("Failed to create compat instance " + data.memberName(), e);
                    }
                });
    }
}

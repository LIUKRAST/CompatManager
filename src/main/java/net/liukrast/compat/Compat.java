package net.liukrast.compat;

import net.neoforged.api.distmarker.Dist;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Compat Manager is a simple library to manage mod compatibility.
 * Create a class with this annotation and create a constructor either empty or with {@link net.neoforged.bus.api.IEventBus} & {@link net.neoforged.fml.ModContainer}
 *
 * <br><br>
 * This annotation looks just the same as {@link net.neoforged.fml.common.Mod}, so you can register events, deferred registries, and whatever you prefer.
 * Your annotated classes are loaded only if the compat mod ID is found, so feel free to import all your classes
 * <br><br>
 * <strong>VERY IMPORTANT: Your mod must annotate the dependency to this mod in their mods toml and specify the ordering AFTER</strong>
 * */
@Retention(RetentionPolicy.RUNTIME)
public @interface Compat {
    /**
     * The unique mod identifier for this compat. <strong>This must match your compatibility mod ID, not your mod ID</strong>
     * */
    String[] value();

    /**
     * @return the side to load this compatibility entrypoint on
     * */
    Dist[] dist() default { Dist.CLIENT, Dist.DEDICATED_SERVER };
}

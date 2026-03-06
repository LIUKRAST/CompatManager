package net.liukrast.compat;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

/**
 * A helper to create compatibility mixins.
 * Create a subfolder {@code compat} inside your mixin folder, then create a subfolder with the compatibility mod ID.
 * That mixin will only be loaded if the specified mod ID is found
 * */
public interface CompatMixinPlugin extends IMixinConfigPlugin {
    @Override
    default boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String[] pack = mixinClassName.split("/");
        if(pack.length == 0) return true;
        if(!pack[0].equals("compat")) return true;
        if(pack.length == 1) throw new IllegalStateException("Mixin found " + mixinClassName + " which is in the compat package but not in any mod subfolder");
        String id = pack[1];
        if(LoadingModList.get() != null) return LoadingModList.get().getModFileById(id) != null;
        return ModList.get().isLoaded(pack[1]);
    }

    class Default implements CompatMixinPlugin {

        @Override
        public void onLoad(String mixinPackage) {

        }

        @Override
        public String getRefMapperConfig() {
            return null;
        }

        @Override
        public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

        }

        @Override
        public List<String> getMixins() {
            return null;
        }

        @Override
        public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        }

        @Override
        public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        }
    }
}

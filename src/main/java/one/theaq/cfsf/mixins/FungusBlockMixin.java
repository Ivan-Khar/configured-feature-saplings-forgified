package one.theaq.cfsf.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.FungusBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(FungusBlock.class)
public abstract class FungusBlockMixin {
  @Redirect(
    method = "performBonemeal",
    at = @At(
      target = "Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;place(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;)Z",
      value = "INVOKE",
      ordinal = 0
  ))
  private boolean fixFungus(ConfiguredFeature<?,?> staticConfiguredFeature, WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos blockPos, ServerLevel _level, RandomSource _random, BlockPos pos, BlockState state) {
    Optional<ResourceKey<ConfiguredFeature<?, ?>>> optionalKey = BuiltinRegistries.CONFIGURED_FEATURE.getResourceKey(staticConfiguredFeature);
    if (optionalKey.isPresent()) {
      Registry<ConfiguredFeature<?, ?>> dynamicRegistryCF = level.registryAccess().registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY);
      Optional<ConfiguredFeature<?, ?>> dynamicCF = dynamicRegistryCF.getOptional(optionalKey.get().location());
      if (dynamicCF.isPresent()) {
        BlockState emptyState = level.getFluidState(pos).createLegacyBlock();
        level.setBlock(pos, emptyState, 4);
        if (dynamicCF.get().place(level, chunkGenerator, random, blockPos)) {
          return true;
        } else {
          level.setBlock(pos, state, 4);
          return false;
        }
      }
    }
    
    return staticConfiguredFeature.place(level, chunkGenerator, random, blockPos);
  }
}

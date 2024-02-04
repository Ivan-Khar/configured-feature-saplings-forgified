package one.theaq.cfsf.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.MossBlock;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(MossBlock.class)
public abstract class MossBlockMixin {
  @Redirect(
    method = "performBonemeal",
    at = @At(
      target = "Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;place(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;)Z",
      value = "INVOKE",
      ordinal = 0
  ))
  private boolean fixMoss(ConfiguredFeature<?,?> staticConfiguredFeature, WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos blockPos) {
    Registry<ConfiguredFeature<?, ?>> dynamicRegistryCF = level.registryAccess().registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY);
    Optional<ConfiguredFeature<?, ?>> dynamicCF = dynamicRegistryCF.getOptional(new ResourceLocation("moss_patch_bonemeal"));
    return dynamicCF.map(configuredFeature -> configuredFeature.place(level, chunkGenerator, random, blockPos)).orElseGet(() -> staticConfiguredFeature.place(level, chunkGenerator, random, blockPos));
    
  }
}

package leah.leahs_immersive_thunder.mixin;

import leah.leahs_immersive_thunder.ImmersiveThunderClient;
import leah.leahs_immersive_thunder.util.ThunderSoundInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightningEntity.class)
public class ImmersiveThunderMixin implements ThunderSoundInterface {
  @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZ)V"))
  private void playSound(World world, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, boolean useDistance) {
    LightningEntity lightning = (LightningEntity) (Object) this;
    PlayerEntity player = MinecraftClient.getInstance().player;

    double distanceToEntity = player.distanceTo(lightning);

    if (distanceToEntity <= closeDistance) {
      playThunderSound(world, lightning, ImmersiveThunderClient.ENTITY_LIGHTNING_BOLT_THUNDER_CLOSE, 5000.0f, false);
    } else if (distanceToEntity <= mediumDistance) {
      playThunderSound(world, lightning, ImmersiveThunderClient.ENTITY_LIGHTNING_BOLT_THUNDER_MEDIUM, 10000.0f, true);
    } else {
      playThunderSound(world, lightning, ImmersiveThunderClient.ENTITY_LIGHTNING_BOLT_THUNDER_FAR, 10000.0f, true);
    }
  }

  @Override
  public void playThunderSound(World world, LightningEntity lightning, SoundEvent soundEvent, float volume, boolean useDistance) {
    world.playSound(lightning.getX(), lightning.getY(), lightning.getZ(), soundEvent, SoundCategory.WEATHER, volume, 0.8f + world.random.nextFloat() * 0.2f, useDistance);
  }
}

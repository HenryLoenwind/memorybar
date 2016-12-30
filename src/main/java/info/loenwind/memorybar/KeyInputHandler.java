package info.loenwind.memorybar;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class KeyInputHandler {

  private static KeyBinding keyBinding;
  private static boolean showMem = false;

  public static void init() {
    keyBinding = new KeyBinding("key.memorybar", Keyboard.CHAR_NONE, "key.categories.misc");
    ClientRegistry.registerKeyBinding(keyBinding);
    MinecraftForge.EVENT_BUS.register(KeyInputHandler.class);
  }

  @SubscribeEvent
  public static void onKeyInput(InputEvent.MouseInputEvent event) {
    if (keyBinding.isPressed()) {
      showMem = !showMem;
    }
  }

  @SubscribeEvent
  public static void onRender(RenderGameOverlayEvent.Post event) {
    if ((showMem || Minecraft.getMinecraft().gameSettings.showDebugInfo) && event.getType() == ElementType.ALL) {
      MemoryPainter.drawMemoryBar(event.getResolution().getScaledWidth());
    }
  }

}

package info.loenwind.memorybar;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MemoryBarMod.MODID, version = MemoryBarMod.VERSION)
public class MemoryBarMod {
  public static final String MODID = "memorybar";
  public static final String VERSION = "1.0.0";

  @SidedProxy(clientSide = "info.loenwind.memorybar.ClientProxy", serverSide = "info.loenwind.memorybar.ServerProxy")
  public static IProxy proxy;

  @EventHandler
  public void init(FMLPreInitializationEvent event) {
    proxy.preinit(event);
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {
    proxy.init(event);
  }
}

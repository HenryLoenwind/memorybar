package info.loenwind.memorybar;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ServerProxy implements IProxy {

  @Override
  public void init(FMLInitializationEvent event) {
  }

  @Override
  public void preinit(FMLPreInitializationEvent event) {
    event.getModLog().warn("Memory Bar is a client-side mod. You do not need it on the server. However, it will do no harm here...");
  }

}

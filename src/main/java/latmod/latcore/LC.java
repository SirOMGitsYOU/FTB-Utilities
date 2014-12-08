package latmod.latcore;
import latmod.core.*;
import latmod.core.net.LMNetHandler;
import latmod.latcore.cmd.*;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;

@Mod(modid = LC.MOD_ID, name = "LatCoreMC", version = LC.VERSION, dependencies = "required-after:Forge@[10.13.2.1231,)")
public class LC
{
	protected static final String MOD_ID = "LatCoreMC";
	public static final String VERSION = "@VERSION@";
	
	@Mod.Instance(LC.MOD_ID)
	public static LC inst;
	
	@SidedProxy(clientSide = "latmod.latcore.client.LCClient", serverSide = "latmod.latcore.LCCommon")
	public static LCCommon proxy;
	
	public static LMMod mod;
	
	public LC()
	{
		MinecraftForge.EVENT_BUS.register(LCEventHandler.instance);
		FMLCommonHandler.instance().bus().register(LCEventHandler.instance);
	}
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		if(LatCoreMC.isDevEnv)
			LatCoreMC.logger.info("Loading LatCoreMC, Dev Build");
		else
			LatCoreMC.logger.info("Loading LatCoreMC, Build #" + VERSION);
		
		mod = new LMMod(MOD_ID, new LCConfig(e), null);
		mod.logger = LatCoreMC.logger;
		
		ODItems.preInit();
		
		mod.onPostLoaded();
		
		LatCoreMC.addGuiHandler(this, proxy);
		
		proxy.preInit(e);
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent e)
	{
		LMNetHandler.init();
		proxy.init(e);
		
		FMLInterModComms.sendMessage("Waila", "register", "latmod.core.waila.RegisterWailaEvent.registerHandlers");
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e)
	{
		ODItems.postInit();
		mod.loadRecipes();
		proxy.postInit(e);
	}
	
	@Mod.EventHandler
	public void registerCommands(FMLServerStartingEvent e)
	{
		regCmd(e, new CmdLatCore(LCConfig.Commands.latcore));
		regCmd(e, new CmdLatCoreAdmin(LCConfig.Commands.latcoreadmin));
		regCmd(e, new CmdRealNick(LCConfig.Commands.realnick));
		regCmd(e, new CmdTpOverride(LCConfig.Commands.teleport));
		regCmd(e, new CmdListOverride(LCConfig.Commands.list));
		e.registerServerCommand(new CmdGamemodeOverride(LCConfig.Commands.gamemode));
		e.registerServerCommand(new CmdGameruleOverride(LCConfig.Commands.gamerule));
	}
	
	private static void regCmd(FMLServerStartingEvent e, CommandBaseLC c)
	{ if(c.enabled > 0) e.registerServerCommand(c); }
}
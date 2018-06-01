package com.feed_the_beast.ftbutilities.command.chunks;

import com.feed_the_beast.ftblib.lib.cmd.CmdBase;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftbutilities.data.ClaimedChunks;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;
import java.util.OptionalInt;

/**
 * @author LatvianModder
 */
public class CmdUnclaimEverything extends CmdBase
{
	public CmdUnclaimEverything()
	{
		super("unclaim_everything", Level.OP);
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		if (args.length == 1)
		{
			return getListOfStringsMatchingLastWord(args, getDimensionNames());
		}

		return super.getTabCompletions(server, sender, args, pos);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (!ClaimedChunks.isActive())
		{
			throw new CommandException("feature_disabled_server");
		}

		OptionalInt dimension = parseDimension(sender, args, 0);

		for (ForgeTeam team : Universe.get().getTeams())
		{
			ClaimedChunks.instance.unclaimAllChunks(team, dimension);
		}
	}
}
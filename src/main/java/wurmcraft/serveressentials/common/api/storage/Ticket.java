package wurmcraft.serveressentials.common.api.storage;

import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class Ticket {

	private UUID user;
	private String[] messages = new String[1];
	private BlockPos location;

	public Ticket (UUID uuid,String message,BlockPos pos) {
		this.user = uuid;
		this.messages[0] = message;
		this.location = pos;
	}

	public UUID getUser () {
		return user;
	}

	private String[] getMessages () {
		return messages;
	}

	private BlockPos getLocation () {
		return location;
	}
}

package wurmcraft.serveressentials.common.api.storage;

public class Channel {

	private String name;
	private String prefix;
	private boolean chatFillter;
	private boolean logChat;
	private Type type;
	private String typeData;

	/**
	 All the settings about a channel

	 @param name Name of Channel (Used for Commands)
	 @param prefix Added before the player's name in chat
	 @param chatFillter Is the chat filter acive in this channel
	 @param logChat Chat logged to a file
	 @param type Type of channel
	 @param data Stores the data about the type (IE: Pass = "Password", Team = "teamName", Rank = "Rank Name")
	 */
	public Channel (String name,String prefix,boolean chatFillter,boolean logChat,Type type,String data) {
		this.name = name;
		this.prefix = prefix;
		this.chatFillter = chatFillter;
		this.logChat = logChat;
		this.type = type;
		this.typeData = data;
	}

	public Channel (String name,String prefix,Type type,String data) {
		this.name = name;
		this.prefix = prefix;
		this.chatFillter = true;
		this.logChat = false;
		this.type = type;
		this.typeData = data;
	}

	public String getName () {
		return name;
	}

	public void setName (String name) {
		this.name = name;
	}

	public String getPrefix () {
		return prefix;
	}

	public void setPrefix (String prefix) {
		this.prefix = prefix;
	}

	public boolean isChatFillter () {
		return chatFillter;
	}

	public void setChatFillter (boolean chatFillter) {
		this.chatFillter = chatFillter;
	}

	public boolean isLogChat () {
		return logChat;
	}

	public void setLogChat (boolean logChat) {
		this.logChat = logChat;
	}

	public Type getType () {
		return type;
	}

	public void setType (Type type) {
		this.type = type;
	}

	public String getTypeData () {
		return typeData;
	}

	public void setTypeData (String typeData) {
		this.typeData = typeData;
	}

	public enum Type {
		PUBLIC,PASS,TEAM,RANK
	}
}

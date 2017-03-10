package wurmcraft.serveressentials.common.api.permissions;

/**
	* @see IRank
	*/
public class Rank implements IRank {

		private String   name;
		private boolean  defaul;
		private String   prefix;
		private String   suffix;
		private String[] inheritance;
		private String[] permissions;

		public Rank(String name, boolean defaul, String prefix, String suffix, String[] inheritance, String[] permissions) {
				this.name = name; this.defaul = defaul; this.prefix = prefix; this.suffix = suffix; this.inheritance = inheritance;
				this.permissions = permissions;
		}

		@Override
		public String getName() {
				return name;
		}

		@Override
		public boolean isDefault() {
				return defaul;
		}

		@Override
		public String getPrefix() {
				return prefix;
		}

		@Override
		public String getSuffix() {
				return suffix;
		}

		@Override
		public String[] getInheritance() {
				return inheritance;
		}

		@Override
		public String[] getPermissions() {
				return permissions;
		}
}

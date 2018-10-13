package com.wurmcraft.serveressentials.api.json.claim2;

import com.wurmcraft.serveressentials.api.json.user.LocationWrapper;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Used by claiming module to store all data related to a claimed area
 *
 * @see com.wurmcraft.serveressentials.common.claim2.SEClaim
 */
public interface Claim {

  /** Unique ID of the claim (Used for storage and verifying a claim is the same as itself. */
  UUID getUniqueID();

  /**
   * Owner of the claim
   *
   * <p>If UUID is "[ServerEssentials]" its an admin claim if not its the users uuid
   *
   * @return Instance of the claim's owner
   * @see Claim#isAdminClaim()
   */
  ClaimOwner getOwner();

  /** Lowest Corner of the claim 2D Claiming will ignore the Y value in this */
  LocationWrapper getLowerCorner();

  /** Highest Corner of the claim 2D Claiming will ignore the Y value in this */
  LocationWrapper getHigherCorner();

  /**
   * Type of claim this is
   *
   * @see ClaimType
   */
  ClaimType getClaimType();

  /** Total Claimed Area in Blocks used by this claim */
  int getArea();

  /** Total claim blocks used by this claim */
  int getClaimCount();

  /** Width of Claim in Blocks Index 0 is X Index 1 is Z */
  int[] getWidth();

  /** Height of claim in Blocks */
  int getHeight();

  /**
   * Change the owner of a claim
   *
   * @return SUCCESSFUL or FAILED
   */
  ClaimResponse transferOwner(ClaimOwner owner);

  /**
   * Get List of all trusted Users and there trustLevel
   *
   * @see Claim#isTrusted(UUID, ClaimPerm[])
   */
  Map<UUID, ClaimPerm[]> getTrusted();

  /**
   * Removes / Clears all the trusted users
   *
   * @return SUCCESSFUL or FAILED
   */
  ClaimResponse removeAllTrusted();

  /**
   * Trusts a user with a set of permissions (trustLevel)
   *
   * @param uuid User GameProfile ID
   * @param perms The Trust Level of this user
   * @return SUCCESSFUL, FAILED or EMPTY
   */
  ClaimResponse addTrusted(UUID uuid, ClaimPerm[] perms);

  /**
   * Trust a user with the default set of permissions (trustLevel)
   *
   * @param uuid User GameProfile ID
   * @return SUCCESSFUL or FAILED
   */
  ClaimResponse addTrusted(UUID uuid);

  /**
   * Trust a name with a set of permissions (trustLevel)
   *
   * @param group A UUID or String
   * @return SUCCESSFUL, FAILED, UNSUPPORTED or EMPTY
   */
  ClaimResponse addTrusted(Object group, ClaimPerm[] perms);

  /**
   * Removes a name from having any permissions (trustLevel)
   *
   * @param id A UUID or String
   * @return SUCCESSFUL, FAILED or EMPTY
   */
  ClaimResponse removeTrusted(Object id);

  /**
   * Checks if a certain UUID has any permission
   *
   * @param uuid UUID of the player
   */
  boolean isTrusted(UUID uuid);

  /**
   * Check if a certain UUID has a set of permissions
   *
   * @param uuid UUID of the player
   */
  boolean isTrusted(UUID uuid, ClaimPerm[] perms);

  /** Check if a certain player has any permission */
  boolean isTrusted(EntityPlayer player);

  /** Check if a certain player has a set of permissions */
  boolean isTrusted(EntityPlayer player, ClaimPerm[] perms);

  /**
   * Check if a certain object has any permission
   *
   * @param group A UUID or String
   */
  boolean isTrusted(Object group);

  /**
   * Check if a certain object has a set of permissions
   *
   * @param group A UUID or String
   */
  boolean isTrusted(Object group, ClaimPerm[] perms);

  /** Checks to see if the claimOwner is an Admin */
  boolean isAdminClaim();

  /** Checks if a location is within the claim */
  boolean isWithin(LocationWrapper loc);

  /**
   * @param direction EnumFaceDirection 0 = up 1 = down 2 = north
   * @param amount Amount of expand
   * @return SUCCESSFUL or FAILED
   */
  ClaimResponse extend(int direction, int amount);

  /** Used to check if the claim has been modified and needs to be saved */
  boolean isDirty();

  /**
   * Sets if a claim needs to be saved or not
   *
   * @return SUCCESSFUL or FAILED
   */
  ClaimResponse setDirty(boolean dirty);
}

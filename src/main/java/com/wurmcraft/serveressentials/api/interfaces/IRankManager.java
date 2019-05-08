package com.wurmcraft.serveressentials.api.interfaces;

import com.wurmcraft.serveressentials.api.user.rank.Rank;

/**
 * Manager for registed rank within Server-Essentials
 *
 * @see com.wurmcraft.serveressentials.api.ServerEssentialsAPI#rankManager
 */
public interface IRankManager {

  /**
   * Adds a rank to the manager's database
   *
   * @param rank rank to be registered
   * @return if the rank has been correctly registered
   */
  boolean register(Rank rank);

  /**
   * Removes a rank from the manager's database
   *
   * @param rank rank to be removed from the database
   * @return if the rank has been correctly removed
   */
  boolean remove(Rank rank);

  /**
   * Checks if a rank has been registered and exists within the manager's database
   *
   * @param rank rank to check
   * @return if the rank exists or not
   */
  boolean exists(Rank rank);

  /**
   * Checks if a rank has been registered and exists within the manager's database
   *
   * @param name rank to check
   * @return if the rank exists or not
   * @see IRankManager#exists(Rank)
   */
  boolean exists(String name);

  /**
   * Gets a rank from its name by looking up in the rank manager's database
   *
   * @param rank rank to get instance of
   * @return the rank if it exists if not it should be null
   */
  Rank getRank(String rank);

  /**
   * Gets a list of all the current ranks
   *
   * @return a array of all the current ranks
   */
  Rank[] getRanks();
}

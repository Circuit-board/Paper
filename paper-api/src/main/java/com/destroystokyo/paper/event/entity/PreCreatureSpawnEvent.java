package com.destroystokyo.paper.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * WARNING: This event only fires for a limited number of cases, and not for every case that {@link CreatureSpawnEvent} does.
 * <p>
 * You should still listen to {@link CreatureSpawnEvent} as a backup, and only use this event as an "enhancement".
 * The intent of this event is to improve server performance, so it fires even if the spawning might fail later, for
 * example when the entity would be unable to spawn due to limited space or lighting.
 * <p>
 * Currently: NATURAL and SPAWNER based reasons. <!-- Please submit a Pull Request for future additions. -->
 * Also, Plugins that replace Entity Registrations with their own custom entities might not fire this event.
 */
@NullMarked
public class PreCreatureSpawnEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Location location;
    private final EntityType type;
    private final CreatureSpawnEvent.SpawnReason reason;
    private boolean shouldAbortSpawn;

    private boolean cancelled;

    @ApiStatus.Internal
    public PreCreatureSpawnEvent(final Location location, final EntityType type, final CreatureSpawnEvent.SpawnReason reason) {
        this.location = location;
        this.type = type;
        this.reason = reason;
    }

    /**
     * @return The location this creature is being spawned at
     */
    public Location getSpawnLocation() {
        return this.location.clone();
    }

    /**
     * @return The type of creature being spawned
     */
    public EntityType getType() {
        return this.type;
    }

    /**
     * @return Reason this creature is spawning (ie, NATURAL vs SPAWNER)
     */
    public CreatureSpawnEvent.SpawnReason getReason() {
        return this.reason;
    }

    /**
     * @return If the spawn process should be aborted vs trying more attempts
     */
    public boolean shouldAbortSpawn() {
        return this.shouldAbortSpawn;
    }

    /**
     * Set this if you are more blanket blocking all types of these spawns, and wish to abort the spawn process from
     * trying more attempts after this cancellation.
     *
     * @param shouldAbortSpawn Set if the spawn process should be aborted vs trying more attempts
     */
    public void setShouldAbortSpawn(final boolean shouldAbortSpawn) {
        this.shouldAbortSpawn = shouldAbortSpawn;
    }

    /**
     * @return If the spawn of this creature is cancelled or not
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Cancelling this event is more efficient than cancelling {@link CreatureSpawnEvent}
     *
     * @param cancel {@code true} if you wish to cancel this event, and abort the spawn of this creature
     */
    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}

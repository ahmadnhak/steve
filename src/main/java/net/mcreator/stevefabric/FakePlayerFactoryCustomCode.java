package net.mcreator.stevefabric;


import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.world.ServerWorld;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.UUID;

public class FakePlayerFactoryCustomCode {
    private static GameProfile MINECRAFT = new GameProfile(UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE77"), "[Minecraft]");
    // Map of all active fake player usernames to their entities
    private static Map<GameProfile, FakePlayerCustomCode> fakePlayers = Maps.newHashMap();
    private static WeakReference<FakePlayerCustomCode> MINECRAFT_PLAYER = null;

    public static FakePlayerCustomCode getMinecraft(ServerWorld world) {
        FakePlayerCustomCode ret = MINECRAFT_PLAYER != null ? MINECRAFT_PLAYER.get() : null;
        if (ret == null) {
            ret = FakePlayerFactoryCustomCode.get(world, MINECRAFT);
            MINECRAFT_PLAYER = new WeakReference<FakePlayerCustomCode>(ret);
        }
        return ret;
    }

    public static FakePlayerCustomCode get(ServerWorld world, GameProfile username) {
        if (!fakePlayers.containsKey(username)) {
            FakePlayerCustomCode fakePlayer = new FakePlayerCustomCode(world, username);
            fakePlayers.put(username, fakePlayer);
        }

        return fakePlayers.get(username);
    }

    public static void unloadWorld(ServerWorld world) {
        fakePlayers.entrySet().removeIf(entry -> entry.getValue().world == world);
        if (MINECRAFT_PLAYER != null && MINECRAFT_PLAYER.get() != null && MINECRAFT_PLAYER.get().world == world) // This shouldn't be strictly necessary, but lets be aggressive.
        {
            FakePlayerCustomCode mc = MINECRAFT_PLAYER.get();
            if (mc != null && mc.world == world) {
                MINECRAFT_PLAYER = null;
            }
        }
    }
}


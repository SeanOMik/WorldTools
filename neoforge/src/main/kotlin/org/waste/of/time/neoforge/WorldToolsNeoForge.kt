package org.waste.of.time.neoforge

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.world.chunk.WorldChunk
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent
import net.neoforged.neoforge.client.event.ScreenEvent
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent
import net.neoforged.neoforge.event.level.ChunkEvent
import net.neoforged.fml.common.Mod
import org.waste.of.time.Events
import org.waste.of.time.WorldTools
import org.waste.of.time.WorldTools.LOG
import org.waste.of.time.manager.CaptureManager
import net.neoforged.neoforge.common.NeoForge
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS

@Mod(WorldTools.MOD_ID)
object WorldToolsNeoForge {
    init {
        WorldTools.initialize()
        
        MOD_BUS.addListener<RegisterKeyMappingsEvent> {
            it.register(WorldTools.CAPTURE_KEY)
            it.register(WorldTools.CONFIG_KEY)
        }
        FORGE_BUS.addListener<RegisterClientCommandsEvent> {
            it.dispatcher.register()
        }
        FORGE_BUS.addListener<ClientPlayerNetworkEvent.LoggingIn> {
            Events.onClientJoin()
        }
        FORGE_BUS.addListener<ClientPlayerNetworkEvent.LoggingOut> {
            Events.onClientDisconnect()
        }
        FORGE_BUS.addListener<EntityJoinLevelEvent> {
            Events.onEntityLoad(it.entity)
        }
        FORGE_BUS.addListener<EntityLeaveLevelEvent> {
            Events.onEntityUnload(it.entity)
        }
        FORGE_BUS.addListener<ClientTickEvent.Pre> {
            //if (it.phase == TickEvent.Phase.START) Events.onClientTickStart()
            Events.onClientTickStart()
        }
        FORGE_BUS.addListener<ChunkEvent.Load> {
            if (it.chunk is WorldChunk) Events.onChunkLoad(it.chunk as WorldChunk)
        }
        FORGE_BUS.addListener<ChunkEvent.Unload> {
            if (it.chunk is WorldChunk) Events.onChunkUnload(it.chunk as WorldChunk)
        }
        FORGE_BUS.addListener<ScreenEvent.Closing> {
            Events.onScreenRemoved(it.screen)
        }

        LOG.info("WorldTools Forge initialized")
    }

    private fun CommandDispatcher<ServerCommandSource>.register() {
        register(
            literal("worldtools")
                .then(literal("capture")
                    .then(argument("name", StringArgumentType.string()).executes {
                        CaptureManager.start(it.getArgument("name", String::class.java))
                        0
                    })
                    .then(literal("start").executes {
                        CaptureManager.start()
                        0
                    })
                    .then(literal("stop").executes {
                        CaptureManager.stop()
                        0
                    })
                    .executes {
                        CaptureManager.toggleCapture()
                        0
                    }
                )
        )
    }
}

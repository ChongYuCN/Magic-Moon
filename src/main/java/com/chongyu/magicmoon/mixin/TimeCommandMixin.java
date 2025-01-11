package com.chongyu.magicmoon.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.TimeArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TimeCommand;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(TimeCommand.class)
public class TimeCommandMixin {

    @Inject(at=@At("HEAD"), method = "register", cancellable = true)
    private static void register(CommandDispatcher<ServerCommandSource> dispatcher, CallbackInfo ci) {
        if(!FabricLoader.getInstance().isModLoaded("aliveandwell")){
            dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("time").requires((source) -> {
                return source.hasPermissionLevel(2);
            })).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder) ((LiteralArgumentBuilder)CommandManager.literal("setday"))))).then(CommandManager.argument("time", TimeArgumentType.time()).executes((context) -> {

                return executeSet((ServerCommandSource)context.getSource(), (IntegerArgumentType.getInteger(context, "time")-1)*24000);}))
            ).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder) ((LiteralArgumentBuilder)CommandManager.literal("setdayNight"))))).then(CommandManager.argument("time", TimeArgumentType.time()).executes((context) -> {
                return executeSet((ServerCommandSource)context.getSource(), (IntegerArgumentType.getInteger(context, "time")-1)*24000+13000);}))
            ).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder) ((LiteralArgumentBuilder)CommandManager.literal("setdaySleeping"))))).then(CommandManager.argument("time", TimeArgumentType.time()).executes((context) -> {
                return executeSet((ServerCommandSource)context.getSource(), (IntegerArgumentType.getInteger(context, "time")-1)*24000+14900);}))
            ).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder) ((LiteralArgumentBuilder)CommandManager.literal("setdayMorning"))))).then(CommandManager.argument("time", TimeArgumentType.time()).executes((context) -> {
                return executeSet((ServerCommandSource)context.getSource(), (IntegerArgumentType.getInteger(context, "time")-1)*24000+23900);}))
            ))));

            ci.cancel();
        }
    }

    @Shadow
    private static int getDayTime(ServerWorld world) {
        return (int)(world.getTimeOfDay() % 24000L);
    }

    @Shadow
    public static int executeSet(ServerCommandSource source, int time) {
        Iterator var2 = source.getServer().getWorlds().iterator();

        while(var2.hasNext()) {
            ServerWorld serverWorld = (ServerWorld)var2.next();
            serverWorld.setTimeOfDay((long)time);
        }

        source.sendFeedback(Text.translatable("commands.time.set", new Object[]{time}), true);
        return getDayTime(source.getWorld());
    }
}

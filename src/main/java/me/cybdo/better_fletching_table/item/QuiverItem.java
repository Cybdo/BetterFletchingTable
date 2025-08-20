package me.cybdo.better_fletching_table.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.World;

public class QuiverItem extends Item {

    public static final int TOOLTIP_STACKS_COLUMNS = 4;
    public static final int TOOLTIP_STACKS_ROWS = 3;
    public static final int MAX_TOOLTIP_STACKS_SHOWN = 12;
    public static final int MAX_TOOLTIP_STACKS_SHOWN_WHEN_TOO_MANY_TYPES = 11;
    private static final int FULL_ITEM_BAR_COLOR = ColorHelper.fromFloats(1.0F, 1.0F, 0.33F, 0.33F);
    private static final int ITEM_BAR_COLOR = ColorHelper.fromFloats(1.0F, 0.44F, 0.53F, 1.0F);
    private static final int field_54109 = 10;
    private static final int field_54110 = 2;
    private static final int MAX_USE_TIME = 200;


    public QuiverItem(Settings settings) {
        super(settings);
    }

    public static float getAmountFilled(ItemStack stack) {
        BundleContentsComponent bundleContentsComponent = (BundleContentsComponent) stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT);
        return bundleContentsComponent.getOccupancy().floatValue();
    }

    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        BundleContentsComponent bundleContentsComponent = (BundleContentsComponent) stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContentsComponent == null) {
            return false;
        } else {
            ItemStack itemStack = slot.getStack();
            BundleContentsComponent.Builder builder = new BundleContentsComponent.Builder(bundleContentsComponent);
            if (clickType == ClickType.LEFT && !itemStack.isEmpty()) {
                if (builder.add(slot, player) > 0) {
                    playInsertSound(player);
                } else {
                    playInsertFailSound(player);
                }

                stack.set(DataComponentTypes.BUNDLE_CONTENTS, builder.build());
                this.onContentChanged(player);
                return true;
            } else if (clickType == ClickType.RIGHT && itemStack.isEmpty()) {
                ItemStack itemStack2 = builder.removeSelected();
                if (itemStack2 != null) {
                    ItemStack itemStack3 = slot.insertStack(itemStack2);
                    if (itemStack3.getCount() > 0) {
                        builder.add(itemStack3);
                    } else {
                        playRemoveOneSound(player);
                    }
                }

                stack.set(DataComponentTypes.BUNDLE_CONTENTS, builder.build());
                this.onContentChanged(player);
                return true;
            } else {
                return false;
            }
        }
    }



    private static void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    private static void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    private static void playInsertFailSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT_FAIL, 1.0F, 1.0F);
    }

    private static void playDropContentsSound(World world, Entity entity) {
        world.playSound((Entity) null, entity.getBlockPos(), SoundEvents.ITEM_BUNDLE_DROP_CONTENTS, SoundCategory.PLAYERS, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    private void onContentChanged(PlayerEntity user) {
        ScreenHandler screenHandler = user.currentScreenHandler;
        if (screenHandler != null) {
            screenHandler.onContentChanged(user.getInventory());
        }


    }
}
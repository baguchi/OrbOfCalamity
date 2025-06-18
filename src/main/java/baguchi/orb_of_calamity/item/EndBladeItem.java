package baguchi.orb_of_calamity.item;

import baguchi.orb_of_calamity.OrbOfCalamity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class EndBladeItem extends Item {
    public static final ResourceLocation BASE_ENTITY_RANGE = ResourceLocation.withDefaultNamespace("base_entity_range");
    public static final ResourceLocation BASE_BLOCK_RANGE = ResourceLocation.withDefaultNamespace("base_block_range");
    public static final ResourceLocation SWEEP = ResourceLocation.fromNamespaceAndPath(OrbOfCalamity.MODID, "sweep");


    public EndBladeItem(Properties properties) {
        super(properties.sword(ToolMaterial.DIAMOND, 9, -3.0F).attributes(createAttributes()));
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 9.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -3.0F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND
                )
                .add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(BASE_ENTITY_RANGE, 0.5F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.BLOCK_INTERACTION_RANGE, new AttributeModifier(BASE_BLOCK_RANGE, 0.5F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.SWEEPING_DAMAGE_RATIO, new AttributeModifier(SWEEP, 0.5F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }
}

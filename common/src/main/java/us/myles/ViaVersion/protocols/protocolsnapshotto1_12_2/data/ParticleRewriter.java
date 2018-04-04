package us.myles.ViaVersion.protocols.protocolsnapshotto1_12_2.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.type.Type;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ParticleRewriter {
    private static List<NewParticle> particles = new LinkedList<>();

    static {
        add(34); // (0->34) explode -> minecraft:poof
        add(19); // (1->19) largeexplode -> minecraft:explosion
        add(18); // (2->18) hugeexplosion -> minecraft:explosion_emitter
        add(21); // (3->21) fireworksSpark -> minecraft:firework
        add(4); // (4->4) bubble -> minecraft:bubble
        add(43); // (5->43) splash -> minecraft:splash
        add(22); // (6->22) wake -> minecraft:fishing
        add(42); // (7->42) suspended -> minecraft:underwater
        add(42); // (8->42) depthsuspend -> minecraft:underwater (COMPLETELY REMOVED)
        add(6); // (9->6) crit -> minecraft:crit
        add(14); // (10->14) magicCrit -> minecraft:enchanted_hit
        add(37); // (11->37) smoke -> minecraft:smoke
        add(30); // (12->30) largesmoke -> minecraft:large_smoke
        add(12); // (13->12) spell -> minecraft:effect
        add(26); // (14->26) instantSpell -> minecraft:instant_effect
        add(17); // (15->17) mobSpell -> minecraft:entity_effect
        add(0); // (16->0) mobSpellAmbient -> minecraft:ambient_entity_effect
        add(44); // (17->44) witchMagic -> minecraft:witch
        add(10); // (18->10) dripWater -> minecraft:dripping_water
        add(9); // (19->9) dripLava -> minecraft:dripping_lava
        add(1); // (20->1) angryVillager -> minecraft:angry_villager
        add(24); // (21->24) happyVillager -> minecraft:happy_villager
        add(32); // (22->32) townaura -> minecraft:mycelium
        add(33); // (23->33) note -> minecraft:note
        add(35); // (24->35) portal -> minecraft:portal
        add(15); // (25->15) enchantmenttable -> minecraft:enchant
        add(23); // (26->23) flame -> minecraft:flame
        add(31); // (27->31) lava -> minecraft:lava
        add(-1); // (28->-1) footstap -> REMOVED (TODO REPLACEMENT/CLIENT_SIDED?)
        add(5); // (29->5) cloud -> minecraft:cloud
        add(11, reddustHandler()); // (30->11) reddust -> minecraft:dust
        //    Red	Float	Red value, 0-1
        //    Green	Float	Green value, 0-1
        //    Blue	Float	Blue value, 0-1
        //    Scale	Float	The scale, will be clamped between 0.01 and 4.
        add(29); // (31->29) snowballpoof -> minecraft:item_snowball
        add(34); // (32->34) snowshovel -> minecraft:poof
        add(28); // (33->28) slime -> minecraft:item_slime
        add(25); // (34->25) heart -> minecraft:heart
        add(2); // (35->2) barrier -> minecraft:barrier
        add(27, iconcrackHandler()); // (36->27) iconcrack_(id)_(data) -> minecraft:item
        // Item	Slot	The item that will be used.
        add(3, blockHandler()); // (37->3) blockcrack_(id+(data<<12))   -> minecraft:block
        add(3, blockdustHandler()); // (38->3) blockdust_(id)               -> minecraft:block
        // BlockState	VarInt	The ID of the block state.
        add(36); // (39->36) droplet -> minecraft:rain
        add(-1); // (40->-1) take -> REMOVED (TODO REPLACENT/CLIENT_SIDED?)
        add(13); // (41->13) mobappearance -> minecraft:elder_guardian
        add(8); // (42->8) dragonbreath -> minecraft:dragon_breath
        add(16); // (43->16) endrod -> minecraft:end_rod
        add(7); // (44->7) damageindicator -> minecraft:damage_indicator
        add(40); // (45->40) sweepattack -> minecraft:sweep_attack
        add(20, fallingdustHandler()); // (46->20) fallingdust -> minecraft:falling_dust
        // BlockState	VarInt	The ID of the block state.
        add(41); // (47->41) totem -> minecraft:totem_of_undying
        add(38); // (48->38) spit -> minecraft:spit

        /*
            NEW particles
            minecraft:squid_ink	39	None
            minecraft:bubble_pop	45	None
            minecraft:current_down	46	None
            minecraft:bubble_column_up	47	None
         */
    }

    public static Particle rewriteParticle(int particleId, int[] data) {
        if (particles.size() >= particleId) {
            Via.getPlatform().getLogger().severe("Failed to transform particles with id " + particleId + " and data " + Arrays.toString(data));
            return null;
        }

        NewParticle rewrite = particles.get(particleId);
        return rewrite.handle(new Particle(rewrite.getId()), data);
    }

    private static void add(int newId) {
        particles.add(new NewParticle(newId, null));
    }

    private static void add(int newId, ParticleDataHandler dataHandler) {
        particles.add(new NewParticle(newId, dataHandler));
    }

    interface ParticleDataHandler {
        Particle handler(Particle particle, int[] data);
    }

    // TODO TEST
    private static ParticleDataHandler reddustHandler() {
        return new ParticleDataHandler() {
            @Override
            public Particle handler(Particle particle, int[] data) {
                particle.getArguments().add(new Particle.ParticleData(Type.FLOAT, 1)); // Red 0 - 1
                particle.getArguments().add(new Particle.ParticleData(Type.FLOAT, 0)); // Green 0 - 1
                particle.getArguments().add(new Particle.ParticleData(Type.FLOAT, 0)); // Blue 0 - 1
                particle.getArguments().add(new Particle.ParticleData(Type.FLOAT, 1));// Scale 0.01 - 4 TODO test scale
                return particle;
            }
        };
    }

    // TODO TEST
    private static ParticleDataHandler iconcrackHandler() {
        return new ParticleDataHandler() {
            @Override
            public Particle handler(Particle particle, int[] data) {
                Item item;
                if (data.length == 1)
                    item = new Item((short) data[0], (byte) 1, (short) 0, null);
                else if (data.length == 2)
                    item = new Item((short) data[0], (byte) 1, (short) data[1], null);
                else
                    return particle;

                particle.getArguments().add(new Particle.ParticleData(Type.FLAT_ITEM, item));
                return particle;
            }
        };
    }

    // TODO HANDLE
    private static ParticleDataHandler blockHandler() {
        return new ParticleDataHandler() {
            @Override
            public Particle handler(Particle particle, int[] data) {
                return particle;
            }
        };
    }

    // TODO HANDLE
    private static ParticleDataHandler blockdustHandler() {
        return new ParticleDataHandler() {
            @Override
            public Particle handler(Particle particle, int[] data) {
                return particle;
            }
        };
    }

    // TODO HANDLE
    private static ParticleDataHandler fallingdustHandler() {
        return new ParticleDataHandler() {
            @Override
            public Particle handler(Particle particle, int[] data) {
                return particle;
            }
        };
    }

    @Data
    @RequiredArgsConstructor
    private static class NewParticle {
        private final int id;
        private final ParticleDataHandler handler;

        public Particle handle(Particle particle, int[] data) {
            if (handler != null)
                return handler.handler(particle, data);
            return particle;
        }
    }


}
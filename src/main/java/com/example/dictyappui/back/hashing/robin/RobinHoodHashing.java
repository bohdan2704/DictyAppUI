package com.example.dictyappui.back.hashing.robin;

public class RobinHoodHashing {
    private static final int INITIAL_CAPACITY = 17; // Initial size of the hash table

    private Entity[] map;

    private int size = 0;

    public RobinHoodHashing() {
        map = new Entity[INITIAL_CAPACITY];
        size = 0;
    }

    public int primaryHashing(String word) {
        int intHash = 0;
        for (int i = 0; i < word.length(); i++) {
            intHash = (intHash << 5) - intHash + word.charAt(i);
        }
//        System.out.println(word + " -- " + Math.abs(intHash) % map.length);
        return Math.abs(intHash+1) % map.length;
    }

//    public int insert(String key, String value) {
//        int index = primaryHashing(key);
//        Entity entity = new Entity(key, value);
//
//        int shove = 0;
//        while (map[index] != null) {
//            // Calculate the probe distance for the existing entity
//            int existingProbe = map[index].getProbeLength();
//
//            // Check if the current location has a shorter probe length
//            if (shove > existingProbe) {
//                // Swap the positions of the current entity and the new entity
//                Entity temp = map[index];
//                map[index] = entity;
//                entity = temp;
//
//                // Update the probe length for the moved entity
//                shove = existingProbe;
//            }
//
//            // Move to the next slot
//            shove++;
//
//            // Increment the probe length for the current entity
//            map[index].setProbeLength(shove);
//
//            index = (index + 1) % map.length;
//
//        }
//
//        // Insert the entity into the map
//        map[index] = entity;
//        size++;
//
//        return index;
//    }


    public int insert(String key, String value) {
        int index = primaryHashing(key);
        Entity entity = new Entity(key, value);
        System.out.println(entity + " -- " + index);
        while (map[index]!=null) {
            entity.setId(index);
            if (entity.getProbeLength() > map[index].getProbeLength()) {
                Entity temp = map[index];
                map[index] = entity;
                entity = temp;
            }
            entity.setProbeLength(entity.getProbeLength()+1);
            index = (index+1)%map.length;
        }
        entity.setId(index);
        map[index] = entity;
        return index;
    }

    public Entity[] getMap() {
        return map;
    }
}

package com.guardianangel.utils;

import com.guardianangel.entities.weapons.WeaponType;

import java.util.Random;

public class AmmoDropUtil {
    private static final Random random = new Random();

    public static int getRandomAmmoAmount() {
        return random.nextInt(11) + 10;
    }

    public static WeaponType getRandomWeaponType() {
        return random.nextBoolean() ? WeaponType.PISTOL : WeaponType.RIFLE;
    }
}

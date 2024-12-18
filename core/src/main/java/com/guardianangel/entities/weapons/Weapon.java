package com.guardianangel.entities.weapons;

import com.badlogic.gdx.audio.Sound;

public abstract class Weapon {
    protected String name;
    protected int maxAmmo;
    protected int currentAmmo;
    protected float reloadCooldown;
    protected float reloadTimer;
    protected boolean isReloading;
    protected int ammoToAddAfterReload;
    protected String crosshairPath;

    protected float shootCooldown;
    protected float shootTimer;

    protected Sound shootSound;
    protected Sound reloadSound;

    // Новый параметр урона
    protected int damage;

    public Weapon(String name, int maxAmmo, float reloadCooldown, String crosshairPath, float shootCooldown, int damage) {
        this.name = name;
        this.maxAmmo = maxAmmo;
        this.currentAmmo = maxAmmo;
        this.reloadCooldown = reloadCooldown;
        this.shootCooldown = shootCooldown;
        this.shootTimer = 0;
        this.isReloading = false;
        this.crosshairPath = crosshairPath;
        this.damage = damage; // Инициализация урона
    }

    public void update(float deltaTime) {
        if (isReloading) {
            reloadTimer -= deltaTime;
            if (reloadTimer <= 0) {
                isReloading = false;

                currentAmmo += ammoToAddAfterReload;
                ammoToAddAfterReload = 0;
            }
        }
        if (shootTimer > 0) {
            shootTimer -= deltaTime;
        }
    }

    public boolean canShoot() {
        return !isReloading && currentAmmo > 0 && shootTimer <= 0;
    }

    public void shoot() {
        if (canShoot()) {
            currentAmmo--;
            shootTimer = shootCooldown;
            onShoot();
        }
    }

    public void reload(int ammoToAdd) {
        if (!isReloading && currentAmmo < maxAmmo) {
            isReloading = true;
            reloadTimer = reloadCooldown;
            ammoToAddAfterReload = Math.min(ammoToAdd, maxAmmo - currentAmmo);
            onReload();
        }
    }

    protected abstract void onShoot();
    protected abstract void onReload();

    public int getCurrentAmmo() {
        return currentAmmo;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public boolean isReloading() {
        return isReloading;
    }

    public String getCrosshairPath() {
        return crosshairPath;
    }

    public int getDamage() {
        return damage; // Получаем урон
    }
}


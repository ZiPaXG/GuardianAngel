package com.guardianangel.entities.weapons;

public abstract class Weapon {
    protected String name;
    protected int maxAmmo;
    protected int currentAmmo;
    protected float reloadCooldown;
    protected float reloadTimer;
    protected boolean isReloading;

    public Weapon(String name, int maxAmmo, float reloadCooldown) {
        this.name = name;
        this.maxAmmo = maxAmmo;
        this.currentAmmo = maxAmmo;
        this.reloadCooldown = reloadCooldown;
        this.reloadTimer = 0;
        this.isReloading = false;
    }

    public void update(float deltaTime) {
        if (isReloading) {
            reloadTimer -= deltaTime;
            if (reloadTimer <= 0) {
                currentAmmo = maxAmmo;
                isReloading = false;
            }
        }
    }

    public boolean canShoot() {
        return !isReloading && currentAmmo > 0;
    }

    public void shoot() {
        if (canShoot()) {
            currentAmmo--;
            onShoot();
        }
    }

    public void reload() {
        if (!isReloading && currentAmmo < maxAmmo) {
            isReloading = true;
            reloadTimer = reloadCooldown;
        }
    }

    protected abstract void onShoot();

    public int getCurrentAmmo() {
        return currentAmmo;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public boolean isReloading() {
        return isReloading;
    }
}

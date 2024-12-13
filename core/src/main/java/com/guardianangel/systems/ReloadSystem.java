package com.guardianangel.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.guardianangel.entities.PlayerEntity;
import com.guardianangel.entities.weapons.Weapon;

public class ReloadSystem extends EntitySystem {
    private final PlayerEntity player;

    public ReloadSystem(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public void update(float deltaTime) {
        Weapon weapon = player.getCurrentWeapon();

        weapon.update(deltaTime);

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            if (!weapon.isReloading() && player.getAmmoAmount() > 0) {
                int ammoNeeded = weapon.getMaxAmmo() - weapon.getCurrentAmmo();
                int ammoToReload = Math.min(ammoNeeded, player.getAmmoAmount());

                if (ammoToReload > 0) {
                    weapon.reload(ammoToReload);
                    player.changeAmmoAmount(-ammoToReload);
                }
            }
        }
    }

}

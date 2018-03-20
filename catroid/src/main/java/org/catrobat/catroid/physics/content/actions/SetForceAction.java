/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2017 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.physics.content.actions;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.InterpretationException;
import org.catrobat.catroid.physics.PhysicsObject;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class SetForceAction extends TemporalAction {

	private Sprite sprite;
	private PhysicsObject physicsObject;
	private Formula forceX;
	private Formula forceY;
	private static Timer lastTimer;

	@Override
	protected void update(float percent) {
		cancelLastForce();
		physicsObject.setVelocity(0, 0);

		final Float newForceX;
		try {
			newForceX = forceX == null ? Float.valueOf(0f) : forceX.interpretFloat(sprite);
		} catch (InterpretationException interpretationException) {
			Log.d(getClass().getSimpleName(), "Formula interpretation for this specific Brick failed.", interpretationException);
			return;
		}
		final Float newForceY;
		try {
			newForceY = forceY == null ? Float.valueOf(0f) : forceY.interpretFloat(sprite);
		} catch (InterpretationException interpretationException) {
			Log.d(getClass().getSimpleName(), "Formula interpretation for this specific Brick failed.", interpretationException);
			return;
		}

		final float mass = physicsObject.getMass();
		lastTimer = new Timer();

		try {
			TimerTask timerTask = new TimerTask() {
				@Override
				public void run() {
					Vector2 instantaneousVelocity = physicsObject.getVelocity();
					physicsObject.setVelocity(instantaneousVelocity.x + (newForceX / mass), instantaneousVelocity.y +
							(newForceY / mass));
				}
			};
			lastTimer.schedule(timerTask, 0, 1000);
		} catch (IllegalStateException e){
			android.util.Log.i("Force Action", "resume error");
		}
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public void setPhysicsObject(PhysicsObject physicsObject) {
		this.physicsObject = physicsObject;
	}

	public void setForce(Formula forceX, Formula forceY) {
		this.forceX = forceX;
		this.forceY = forceY;
	}

	public static void cancelLastForce()
	{
		if(lastTimer != null) {
			lastTimer.cancel();
			lastTimer.purge();
		}
	}
}

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
package org.catrobat.catroid.physics.content.bricks;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.R;
import org.catrobat.catroid.common.BrickValues;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.BrickViewProvider;
import org.catrobat.catroid.content.bricks.FormulaBrick;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.ui.fragment.FormulaEditorFragment;

import java.util.List;

public class SetForceBrick extends FormulaBrick {
	private static final long serialVersionUID = 1L;

	private transient View prototypeView;

	public SetForceBrick() {
		addAllowedBrickField(BrickField.PHYSICS_FORCE_X);
		addAllowedBrickField(BrickField.PHYSICS_FORCE_Y);
	}

	public SetForceBrick(Vector2 force) {
		initializeBrickFields(new Formula(force.x), new Formula(force.y));
	}

	public SetForceBrick(Formula forceX, Formula forceY) {
		initializeBrickFields(forceX, forceY);
	}

	private void initializeBrickFields(Formula forceX, Formula forceY) {
		addAllowedBrickField(BrickField.PHYSICS_FORCE_X);
		addAllowedBrickField(BrickField.PHYSICS_FORCE_Y);
		setFormulaWithBrickField(BrickField.PHYSICS_FORCE_X, forceX);
		setFormulaWithBrickField(BrickField.PHYSICS_FORCE_Y, forceY);
	}

	@Override
	public int getRequiredResources() {
		return PHYSICS;
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}

		view = View.inflate(context, R.layout.brick_physics_set_force, null);
		view = BrickViewProvider.setAlphaOnView(view, alphaValue);

		setCheckboxView(R.id.brick_set_force_checkbox);

		TextView editX = (TextView) view.findViewById(R.id.brick_set_force_edit_text_x);
		getFormulaWithBrickField(BrickField.PHYSICS_FORCE_X).setTextFieldId(R.id.brick_set_force_edit_text_x);
		getFormulaWithBrickField(BrickField.PHYSICS_FORCE_X).refreshTextField(view);

		editX.setOnClickListener(this);

		TextView editY = (TextView) view.findViewById(R.id.brick_set_force_edit_text_y);
		getFormulaWithBrickField(BrickField.PHYSICS_FORCE_Y).setTextFieldId(R.id.brick_set_force_edit_text_y);
		getFormulaWithBrickField(BrickField.PHYSICS_FORCE_Y).refreshTextField(view);

		editY.setOnClickListener(this);
		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		prototypeView = View.inflate(context, R.layout.brick_physics_set_force, null);
		TextView textForceX = (TextView) prototypeView.findViewById(R.id.brick_set_force_edit_text_x);
		textForceX.setText(String.valueOf(BrickValues.PHYSIC_FORCE.x));
		TextView textForceY = (TextView) prototypeView.findViewById(R.id.brick_set_force_edit_text_y);
		textForceY.setText(String.valueOf(BrickValues.PHYSIC_FORCE.y));
		return prototypeView;
	}

	@Override
	public void showFormulaEditorToEditFormula(View view) {
		if (checkbox.getVisibility() == View.VISIBLE) {
			return;
		}
		switch (view.getId()) {
			case R.id.brick_set_force_edit_text_y:
				FormulaEditorFragment.showFragment(view, this, BrickField.PHYSICS_FORCE_Y);
				break;

			case R.id.brick_set_force_edit_text_x:
			default:
				FormulaEditorFragment.showFragment(view, this, BrickField.PHYSICS_FORCE_X);
				break;
		}
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory().createSetForceAction(sprite,
				getFormulaWithBrickField(BrickField.PHYSICS_FORCE_X),
				getFormulaWithBrickField(BrickField.PHYSICS_FORCE_Y)));
		return null;
	}
}

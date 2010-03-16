/*
 * Copyright (C) 2010 Thomas Barber
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *
 */
package org.pentaho.pat.client.util.dnd;

import java.util.ArrayList;

import org.pentaho.pat.client.ui.widgets.DimensionFlexTable;
import org.pentaho.pat.rpc.dto.IAxis;

import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @created Feb 25th, 2010
 * @since 0.6.0
 * @author tom(at)wamonline.org.uk
 * 
 */
public interface DimensionMovements {

	/**
	 * 
	 * @param w
	 * @param sourceRow
	 * @param isSourceRow
	 * @param sourceTable
	 * @param targetTable
	 * @param targetAxis
	 */
	public abstract void moveDimension(final Widget w, final int sourceRow,
			final boolean isSourceRow, final DimensionFlexTable sourceTable,
			final DimensionFlexTable targetTable, final IAxis targetAxis);

	/**
	 * 
	 * @param targetAxis
	 * @param w
	 * @param sourceTable
	 * @param sourceRow
	 * @param isSourceRow
	 * @param targetTable
	 */
	public abstract void moveDimensionCreateSelection(final IAxis targetAxis,
			final Widget w, final DimensionFlexTable sourceTable,
			final int sourceRow, final boolean isSourceRow,
			final DimensionFlexTable targetTable);

	/**
	 * 
	 * @param memberList
	 * @param sourceTable
	 * @param sourceRow
	 * @param isSourceRow
	 * @param targetTable
	 * @param targetAxis
	 * @param w
	 */
	public abstract void measureClearSelection(
			final ArrayList<String> memberList,
			final DimensionFlexTable sourceTable, final int sourceRow,
			final boolean isSourceRow, final DimensionFlexTable targetTable,
			final IAxis targetAxis, final Widget w);

	/**
	 * 
	 * @param w
	 * @param sourceRow
	 * @param isSourceRow
	 * @param sourceTable
	 * @param targetTable
	 * @param targetAxis
	 */
	public abstract void moveMeasure(final Widget w, final int sourceRow,
			final boolean isSourceRow, final DimensionFlexTable sourceTable,
			final DimensionFlexTable targetTable, final IAxis targetAxis);

	/**
	 * 
	 * @param memberList
	 * @param sourceTable
	 * @param sourceRow
	 * @param isSourceRow
	 * @param targetTable
	 * @param w
	 */
	public abstract void measureCreateSelection(
			final ArrayList<String> memberList,
			final DimensionFlexTable sourceTable, final int sourceRow,
			final boolean isSourceRow, final DimensionFlexTable targetTable,
			final Widget w);

	/**
	 * 
	 * @param w
	 * @param sourceRow
	 * @param isSourceRow
	 * @param sourceTable
	 * @param targetTable
	 * @param targetAxis
	 */
	public abstract void moveMeasureGrid(final Widget w, final int sourceRow,
			final boolean isSourceRow, final DimensionFlexTable sourceTable,
			final DimensionFlexTable targetTable, final IAxis targetAxis);

}
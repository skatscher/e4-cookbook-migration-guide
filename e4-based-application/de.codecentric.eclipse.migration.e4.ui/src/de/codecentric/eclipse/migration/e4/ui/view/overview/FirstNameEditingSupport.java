package de.codecentric.eclipse.migration.e4.ui.view.overview;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

import de.codecentric.eclipse.migration.e4.model.Person;

public class FirstNameEditingSupport extends EditingSupport {

	public FirstNameEditingSupport(ColumnViewer viewer) {
		super(viewer);
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return new TextCellEditor((Composite) getViewer().getControl());
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		return ((Person) element).getFirstName();
	}

	@Override
	protected void setValue(Object element, Object value) {
		((Person) element).setFirstName(String.valueOf(value));
	    getViewer().update(element, null);
	}
}

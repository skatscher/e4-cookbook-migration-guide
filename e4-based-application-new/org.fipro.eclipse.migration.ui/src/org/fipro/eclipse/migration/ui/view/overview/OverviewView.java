package org.fipro.eclipse.migration.ui.view.overview;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.fipro.eclipse.migration.model.Person;
import org.fipro.eclipse.migration.service.PersonServiceImpl;
import org.fipro.eclipse.migration.ui.editor.PersonEditor;

public class OverviewView {

	@Inject
	private ESelectionService selectionService;

	@Inject 
	private EModelService modelService;

	@Inject
	private EPartService partService;

	TableViewer viewer;

	@PostConstruct
	public void createPartControl(Composite parent, MApplication app) {
		parent.setLayout(new GridLayout());
		
		IObservable list = new WritableList(PersonServiceImpl.getPersons(10), Person.class);

		viewer = new TableViewer(parent, SWT.MULTI|SWT.BORDER|SWT.H_SCROLL|SWT.V_SCROLL|SWT.FULL_SELECTION);
		ObservableListContentProvider cp = new ObservableListContentProvider();
		viewer.setContentProvider(cp);
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(viewer.getControl());
		
		TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setText("Firstname");
		column.getColumn().setWidth(100);
		column.setLabelProvider(new FirstNameLabelProvider());
//		column.setEditingSupport(new FirstNameEditingSupport(viewer));
		
		column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setText("Lastname");
		column.getColumn().setWidth(100);
		column.setLabelProvider(new LastNameLabelProvider());
//		column.setEditingSupport(new LastNameEditingSupport(viewer));

		column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setText("Married");
		column.getColumn().setWidth(60);
		column.setLabelProvider(new MarriedLabelProvider());
//		column.setEditingSupport(new MarriedEditingSupport(viewer));
		
		column = new TableViewerColumn(viewer, SWT.NONE);
		column.getColumn().setText("Gender");
		column.getColumn().setWidth(80);
		column.setLabelProvider(new GenderLabelProvider());
//		column.setEditingSupport(new GenderEditingSupport(viewer));
		
		viewer.setInput(list);
		
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = event.getStructuredSelection();
				Person person = (Person)selection.getFirstElement();
				selectionService.setSelection(person);
			}
		});
		
		// hook double click for opening an editor
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				ISelection selection = viewer.getSelection();
				if (selection != null && selection instanceof IStructuredSelection) {
					Object obj = ((IStructuredSelection) selection).getFirstElement();
					// if we had a selection lets open the editor
					if (obj != null) {
						Person person = (Person) obj;
						person.addPropertyChangeListener(new PropertyChangeListener() {
							
							@Override
							public void propertyChange(PropertyChangeEvent evt) {
								viewer.refresh();
							}
						});

						MPart personEditor = modelService.createModelElement(MPart.class);
						personEditor.setContributionURI(PersonEditor.CONTRIBUTION_URI);
						personEditor.setIconURI("platform:/plugin/org.fipro.eclipse.migration.ui/icons/editor.gif"); //$NON-NLS-1$
						personEditor.setElementId(PersonEditor.ID);
						String label = person.getFirstName() + " " + person.getLastName(); //$NON-NLS-1$
						personEditor.setLabel(label);
						personEditor.setTooltip(label + " editor"); //$NON-NLS-1$
						personEditor.setCloseable(true);
						personEditor.getTags().add("Editor"); //$NON-NLS-1$
						
						// set the person as transient data
						personEditor.getTransientData().put(PersonEditor.PERSON_INPUT_DATA, person);
						
						// find the editor partstack, which is defined in the perspective contribution in the fragment.e4xmi
						MPartStack stack = (MPartStack) modelService.find("org.fipro.eclipse.migration.ui.partstack.editor", app); //$NON-NLS-1$
						stack.getChildren().add(personEditor);
						
						partService.showPart(personEditor, PartState.ACTIVATE);
					}
				}
			}
		});
	}

	@Focus
	public void setFocus() {
		this.viewer.getControl().setFocus();
	}

}

package org.fipro.eclipse.migration.ui.view;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.fipro.eclipse.migration.model.Person;
import org.fipro.eclipse.migration.model.Person.Gender;
import org.fipro.eclipse.migration.ui.Activator;

public class DescriptionView {

	Text description;

	@PostConstruct
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		description = new Text(parent, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
		
		// read preferences to set the initial text color
		String color = Platform.getPreferencesService().
				  getString("org.fipro.eclipse.migration.ui", "description_color", "black", null);
		
		Color toUse = "blue".equals(color) ? 
				Display.getDefault().getSystemColor(SWT.COLOR_BLUE) : Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
		
		description.setForeground(toUse);

		// register a listener on the PreferencesStore to react on changes
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(
				new IPropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent event) {
						if (event.getProperty() == "description_color") {
							Color toUse = "blue".equals(event.getNewValue()) ? 
									Display.getDefault().getSystemColor(SWT.COLOR_BLUE) : Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
							
							if (description != null && !description.isDisposed()) {
								description.setForeground(toUse);
							}
						}
					}
				});
	}

	@Inject
	protected void updateDescription(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Person person) {
		if (description != null && !description.isDisposed()) {
			if (person != null) {
				description.setText(person.getFirstName() + " " + person.getLastName() 
				+ " is a " + (person.isMarried() ? "married " : "single ")
				+ (Gender.MALE.equals(person.getGender()) ? "man" : "woman"));
			} else {
				description.setText("");
			}
		}
	}

	@Focus
	public void setFocus() {
	}
}

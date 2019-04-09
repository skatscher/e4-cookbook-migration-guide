package org.fipro.eclipse.migration.ui.preferences;

import org.fipro.eclipse.migration.service.preferences.PreferenceNodeContribution;
import org.osgi.service.component.annotations.Component;

@Component(service=PreferenceNodeContribution.class)
public class DescriptionPreferenceContribution extends PreferenceNodeContribution {

	public DescriptionPreferenceContribution() {
		super("description", "Description", null, DescriptionPreferencePage.class, null, null);
	}
}
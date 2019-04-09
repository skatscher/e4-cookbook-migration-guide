package org.fipro.eclipse.migration.service.preferences.impl;

import org.eclipse.e4.core.di.suppliers.ExtendedObjectSupplier;
import org.eclipse.e4.core.di.suppliers.IObjectDescriptor;
import org.eclipse.e4.core.di.suppliers.IRequestor;
import org.eclipse.jface.preference.PreferenceManager;
import org.fipro.eclipse.migration.service.preferences.ContributedPreferenceNode;
import org.fipro.eclipse.migration.service.preferences.PreferenceNodeContribution;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = ExtendedObjectSupplier.class, property = "dependency.injection.annotation=org.fipro.eclipse.migration.service.preferences.PrefManager")
public class PreferenceManagerSupplier extends ExtendedObjectSupplier {

	PreferenceManager prefManager;

	@Override
	public Object get(IObjectDescriptor descriptor, IRequestor requestor, boolean track, boolean group) {
		return getManager();
	}

	protected PreferenceManager getManager() {
		if (this.prefManager == null) {
			this.prefManager = new PreferenceManager();
		}
		return prefManager;
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	synchronized void addPreferenceNode(PreferenceNodeContribution node) {
		for (ContributedPreferenceNode contribNode : node.getPreferenceNodes()) {
			if (contribNode.getPath() == null) {
				getManager().addToRoot(contribNode);
			} else {
				getManager().addTo(contribNode.getPath(), contribNode);
			}
		}
	}

	synchronized void removePreferenceNode(PreferenceNodeContribution node) {
		for (ContributedPreferenceNode contribNode : node.getPreferenceNodes()) {
			getManager().remove(contribNode);
		}
	}
}

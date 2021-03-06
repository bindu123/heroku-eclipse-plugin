package com.heroku.eclipse.ui.wizards;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IImportWizard;
import org.osgi.framework.Bundle;

import com.heroku.api.App;
import com.heroku.eclipse.core.services.HerokuServices.IMPORT_TYPES;
import com.heroku.eclipse.ui.Activator;
import com.heroku.eclipse.ui.utils.HerokuUtils;

/**
 * Import wizard for existing Heroku apps
 * 
 * @author udo.rader@bestsolution.at
 * 
 */
public class HerokuAppImport extends AbstractHerokuAppImportWizard implements IImportWizard {
	private HerokuAppImportWizardPage listPage;
	private HerokuAppProjectTypePage projectTypePage;

	/**
	 * 
	 */
	public HerokuAppImport() {

		super();
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		
		setDefaultPageImageDescriptor(ImageDescriptor.createFromURL(FileLocator.find(
				bundle, new Path("icons/55_45/heroku_logo_55_45.png"), null)));
	}

	@Override
	public void addPages() {
		setNeedsProgressMonitor(false);
		Shell shell = Display.getCurrent().getActiveShell();
		
		if (HerokuUtils.verifyPreferences(new NullProgressMonitor(), service, shell)) {
			try {
				listPage = new HerokuAppImportWizardPage();
				addPage(listPage);
				projectTypePage = new HerokuAppProjectTypePage();
				addPage(projectTypePage);
			}
			catch (Exception e) {
				HerokuUtils.internalError(Display.getCurrent().getActiveShell(), e);
			}
		}
		else {
			shell.close();
		}
	}

	public boolean canFinish() {
		return !listPage.isCurrentPage();
	}

	@Override
	public App getActiveApp() {
		return listPage.getSelectedApp();
	}
	
	@Override
	public IMPORT_TYPES getProjectType() {
		return projectTypePage.getImportType();
	}

}

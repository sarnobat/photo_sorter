package com.rohidekar.photosorter;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.rohidekar.photosorter.actions.KeyActions;
import com.rohidekar.photosorter.model.ClientVisibleConfiguration;
import com.rohidekar.photosorter.model.ImmutableMyModel;
import com.rohidekar.photosorter.model.MyConfiguration;
import com.rohidekar.photosorter.model.MyImageList;
import com.rohidekar.photosorter.model.ServerVisibleConfiguration;

public class OptionParser {

	public void parseOptions(String configText, boolean recursiveDefault,
			boolean showTaggedDefault, final String aDefaultRootDirPath, Set<String> tagsToIgnore,
			ImmutableMyModel model, KeyActions actions) {
		String[] configLines = configText.split("\\n");
		tagsToIgnore.clear();
		boolean recursive = recursiveDefault;
		boolean showTagged = showTaggedDefault;
		Set<String> permittedExtensions = new HashSet<String>();
		for (String configLine : configLines) {
			// Ignore comments
			String configLineTrimmed = configLine.trim();
			if (configLineTrimmed.startsWith("#")) {
				continue;
			}
			if (configLineTrimmed.matches("recursive")) {
				recursive = true;
			}
			if (configLineTrimmed.matches("show_all_tagged")) {
				showTagged = true;
			}
			if (configLineTrimmed.startsWith("EXCLUDE")) {
				String lineRemainder = StringUtils.remove(configLineTrimmed, "EXCLUDE");
				String tagToIgnore = lineRemainder.trim();
				tagsToIgnore.add(tagToIgnore);
			}
			if (configLineTrimmed.startsWith("EXT")) {
				String lineRemainder = StringUtils.remove(configLineTrimmed, "EXT");
				String tagToIgnore = lineRemainder.trim();
				permittedExtensions.add(tagToIgnore.toLowerCase());
			}

		}
		ServerVisibleConfiguration configuration = new MyConfiguration(recursive, showTagged, tagsToIgnore,
				model.configurationNotInitialized() ? aDefaultRootDirPath : model.getRootDirPath(),
				permittedExtensions, actions.getTags());
		model.setConfiguration(configuration);
		MyImageList.refreshImageList(configuration.getRootDirPath(), tagsToIgnore, model);
	}
}

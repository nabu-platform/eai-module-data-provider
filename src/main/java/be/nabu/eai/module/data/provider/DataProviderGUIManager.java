package be.nabu.eai.module.data.provider;

import java.io.IOException;
import java.util.List;

import be.nabu.eai.developer.MainController;
import be.nabu.eai.developer.managers.base.BaseJAXBGUIManager;
import be.nabu.eai.repository.resources.RepositoryEntry;
import be.nabu.libs.property.api.Property;
import be.nabu.libs.property.api.Value;

public class DataProviderGUIManager extends BaseJAXBGUIManager<DataProviderConfiguration, DataProvider> {

	public DataProviderGUIManager() {
		super("HTTP Server Data Provider", DataProvider.class, new DataProviderManager(), DataProviderConfiguration.class);
	}

	@Override
	protected List<Property<?>> getCreateProperties() {
		return null;
	}

	@Override
	protected DataProvider newInstance(MainController controller, RepositoryEntry entry, Value<?>... values) throws IOException {
		return new DataProvider(entry.getId(), entry.getContainer(), entry.getRepository());
	}

	@Override
	public String getCategory() {
		return "Miscellaneous"; 
	}
}

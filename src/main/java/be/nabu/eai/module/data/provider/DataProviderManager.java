package be.nabu.eai.module.data.provider;

import be.nabu.eai.repository.api.Repository;
import be.nabu.eai.repository.managers.base.JAXBArtifactManager;
import be.nabu.libs.resources.api.ResourceContainer;

public class DataProviderManager extends JAXBArtifactManager<DataProviderConfiguration, DataProvider> {

	public DataProviderManager() {
		super(DataProvider.class);
	}

	@Override
	protected DataProvider newInstance(String id, ResourceContainer<?> container, Repository repository) {
		return new DataProvider(id, container, repository);
	}

}

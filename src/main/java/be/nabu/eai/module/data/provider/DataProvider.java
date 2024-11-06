/*
* Copyright (C) 2016 Alexander Verbruggen
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see <https://www.gnu.org/licenses/>.
*/

package be.nabu.eai.module.data.provider;

import java.io.IOException;

import be.nabu.eai.repository.EAIResourceRepository;
import be.nabu.eai.repository.api.Repository;
import be.nabu.eai.repository.artifacts.jaxb.JAXBArtifact;
import be.nabu.libs.artifacts.api.StartableArtifact;
import be.nabu.libs.artifacts.api.StoppableArtifact;
import be.nabu.libs.datastore.api.ContextualStreamableDatastore;
import be.nabu.libs.datastore.resources.ResourceDatastore;
import be.nabu.libs.datastore.resources.WritableDataResource;
import be.nabu.libs.datastore.resources.base.FixedDataRouter;
import be.nabu.libs.http.api.server.MessageDataProvider;
import be.nabu.libs.resources.ResourceUtils;
import be.nabu.libs.resources.api.ManageableContainer;
import be.nabu.libs.resources.api.ReadableResource;
import be.nabu.libs.resources.api.Resource;
import be.nabu.libs.resources.api.ResourceContainer;
import be.nabu.libs.resources.api.WritableResource;
import be.nabu.utils.mime.api.Header;

public class DataProvider extends JAXBArtifact<DataProviderConfiguration> implements StartableArtifact, StoppableArtifact {

	private boolean started;
	private ResourceDatastore<?> datastore;
	
	public DataProvider(String id, ResourceContainer<?> directory, Repository repository) {
		super(id, directory, repository, "data-provider.xml", DataProviderConfiguration.class);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResourceDatastore<?> getDatastore() {
		if (datastore == null) {
			synchronized(this) {
				if (datastore == null) {
					try {
						Resource resource = getConfiguration().getStorage() == null 
							? ResourceUtils.mkdirs(getDirectory(), EAIResourceRepository.PRIVATE)
							: ResourceUtils.mkdir(getConfiguration().getStorage(), null);
						datastore = new ResourceDatastore(new FixedDataRouter((ManageableContainer<?>) resource));
					}
					catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return datastore;
	}
	
	@Override
	public void stop() throws IOException {
		if (getConfiguration().getServer() != null) {
			getConfiguration().getServer().getMessageDataProvider().unroute(getConfiguration().getPath() == null ? "/" : getConfiguration().getPath());
			started = false;
		}
	}

	@Override
	public void start() throws IOException {
		if (getConfiguration().getServer() != null) {
			getConfiguration().getServer().getMessageDataProvider().route(
				getConfiguration().getPath() == null ? "/" : getConfiguration().getPath(), 
				new DatastoreMessageDataProvider(getDatastore())
			);
			started = true;
		}
	}

	@Override
	public boolean isStarted() {
		return started;
	}
	
	private static class DatastoreMessageDataProvider implements MessageDataProvider {

		private ContextualStreamableDatastore<String> datastore;
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public DatastoreMessageDataProvider(ContextualStreamableDatastore datastore) {
			this.datastore = datastore;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public <T extends WritableResource & ReadableResource> T newResource(String method, String target, double version, Header... headers) throws IOException {
			return (T) new WritableDataResource(datastore, datastore.stream(target, "http-request", "application/octet-stream"));
		}
		
	}
	
}

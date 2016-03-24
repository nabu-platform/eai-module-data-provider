package be.nabu.eai.module.data.provider;

import java.net.URI;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import be.nabu.eai.module.http.server.HTTPServerArtifact;
import be.nabu.eai.repository.jaxb.ArtifactXMLAdapter;

@XmlRootElement(name = "dataProvider")
public class DataProviderConfiguration {
	
	private URI storage;
	private HTTPServerArtifact server;
	private String path;
	private Long maxSize;

	public URI getStorage() {
		return storage;
	}
	public void setStorage(URI storage) {
		this.storage = storage;
	}

	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public HTTPServerArtifact getServer() {
		return server;
	}
	public void setServer(HTTPServerArtifact server) {
		this.server = server;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public Long getMaxSize() {
		return maxSize;
	}
	public void setMaxSize(Long maxSize) {
		this.maxSize = maxSize;
	}

}

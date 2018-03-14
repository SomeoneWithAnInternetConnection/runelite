package net.runelite.http.service.cache;

import java.io.IOException;
import net.runelite.cache.fs.Archive;
import net.runelite.cache.fs.Index;
import net.runelite.cache.fs.Storage;
import net.runelite.cache.fs.Store;
import net.runelite.cache.index.FileData;
import net.runelite.http.service.cache.beans.ArchiveEntry;
import net.runelite.http.service.cache.beans.CacheEntry;
import net.runelite.http.service.cache.beans.IndexEntry;

//@Component
public class CacheStorage implements Storage
{
//	@Autowired
	private final CacheService cacheService;

	private CacheEntry cacheEntry;

	public CacheStorage(CacheService cacheService)
	{
		this.cacheService = cacheService;
	}

	@Override
	public void init(Store store) throws IOException
	{
		cacheEntry = cacheService.findMostRecent();
		for (IndexEntry indexEntry: cacheService.findIndexesForCache(cacheEntry)) {
			store.addIndex(indexEntry.getIndexId());
		}
	}

	@Override
	public void close() throws IOException
	{

	}

	@Override
	public void load(Store store) throws IOException
	{
		for (Index index : store.getIndexes()) {
			IndexEntry indexEntry = cacheService.findIndexForCache(cacheEntry, index.getId());
			for (ArchiveEntry archiveEntry : cacheService.findArchivesForIndex(indexEntry)) {
				Archive archive = index.addArchive(archiveEntry.getArchiveId());
				archive.setNameHash(archiveEntry.getNameHash());
				archive.setCrc(archiveEntry.getCrc());
				archive.setRevision(archiveEntry.getRevision());

				FileData[] fileData = cacheService.getArchiveFileData(archiveEntry);
				archive.setFileData(fileData);
			}
		}
	}

	@Override
	public void save(Store store) throws IOException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public byte[] loadArchive(Archive archive) throws IOException
	{
		Index index = archive.getIndex();
		IndexEntry indexEntry = cacheService.findIndexForCache(cacheEntry, index.getId());
		ArchiveEntry archiveEntry = cacheService.findArchiveForIndex(indexEntry, archive.getArchiveId());
		return cacheService.getArchive(archiveEntry);
	}

	@Override
	public void saveArchive(Archive archive, byte[] data) throws IOException
	{
		throw new UnsupportedOperationException();
	}
}

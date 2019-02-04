/*
  Copyright (c) OpenRS

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
 */
package io.unity.application.storage;

import io.unity.application.storage.util.FileChannelUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * A file store holds multiple files inside a "virtual" file system made up of
 * several index files and a single data file.
 * 
 * @author Graham
 * @author `Discardedx2
 */
public final class FileStore implements Closeable {

	/**
	 * Creates the file store stored in the specified directory.
	 * 
	 * @param root
	 *            The directory containing the index and data files.
	 * @param indices
	 *            The amount of indices
	 * @return The file store.
	 * @throws FileNotFoundException
	 *             if any of the {@code main_file_cache.*} files could not be
	 *             found.
	 */
	public static FileStore create(String root, int indices) throws IOException {
		return create(new File(root), indices);
	}

	/**
	 * Creates the file store stored in the specified directory.
	 * 
	 * @param root
	 *            The directory containing the index and data files.
	 * @param indices
	 *            The amount of indices
	 * @return The file store.
	 * @throws FileNotFoundException
	 *             if any of the {@code main_file_cache.*} files could not be
	 *             found.
	 */
	public static FileStore create(File root, int indices) throws IOException {
		if (!root.exists() && !root.mkdirs())
			throw new IOException();

		for (int i = 0; i < indices; i++) {
			File index = new File(root, "main_file_cache.idx" + i);
			if (!index.createNewFile())
				throw new IOException();
		}

		File meta = new File(root, "main_file_cache.idx255");
		if (!meta.createNewFile())
			throw new IOException();

		File data = new File(root, "main_file_cache.dat2");
		if (!data.createNewFile())
			throw new IOException();

		return open(root);
	}

	/**
	 * Opens the file store stored in the specified directory.
	 * 
	 * @param root
	 *            The directory containing the index and data files.
	 * @return The file store.
	 * @throws FileNotFoundException
	 *             if any of the {@code main_file_cache.*} files could not be
	 *             found.
	 */
	@SuppressWarnings("resource")
	public static FileStore open(File root) throws FileNotFoundException {
		File data = new File(root, "main_file_cache.dat2");
		if (!data.exists())
			throw new FileNotFoundException();
		RandomAccessFile raf = new RandomAccessFile(data, "rw");
		FileChannel dataChannel = raf.getChannel();

		List<FileChannel> indexChannels = new ArrayList<FileChannel>();
		for (int i = 0; i < 254; i++) {
			File index = new File(root, "main_file_cache.idx" + i);
			if (!index.exists())
				break;

			raf = new RandomAccessFile(index, "rw");
			FileChannel indexChannel = raf.getChannel();
			indexChannels.add(indexChannel);
		}

		if (indexChannels.isEmpty())
			throw new FileNotFoundException();

		File meta = new File(root, "main_file_cache.idx255");
		if (!meta.exists())
			throw new FileNotFoundException();

		raf = new RandomAccessFile(meta, "rw");
		FileChannel metaChannel = raf.getChannel();

		return new FileStore(dataChannel, indexChannels.toArray(new FileChannel[0]), metaChannel);
	}

	/**
	 * Opens the file store stored in the specified directory.
	 * 
	 * @param root
	 *            The directory containing the index and data files.
	 * @return The file store.
	 * @throws FileNotFoundException
	 *             if any of the {@code main_file_cache.*} files could not be
	 *             found.
	 */
	public static FileStore open(String root) throws FileNotFoundException {
		return open(new File(root));
	}

	/**
	 * The data file.
	 */
	private final FileChannel dataChannel;

	/**
	 * The index files.
	 */
	private final FileChannel[] indexChannels;

	/**
	 * The 'meta' index files.
	 */
	private final FileChannel metaChannel;

	/**
	 * Creates a new file store.
	 * 
	 * @param data
	 *            The data file.
	 * @param indexes
	 *            The index files.
	 * @param meta
	 *            The 'meta' index file.
	 */
	public FileStore(FileChannel data, FileChannel[] indexes, FileChannel meta) {
		this.dataChannel = data;
		this.indexChannels = indexes;
		this.metaChannel = meta;
	}

	public void close() throws IOException {
		dataChannel.close();

		for (FileChannel channel : indexChannels)
			channel.close();

		metaChannel.close();
	}

	public boolean hasData() throws IOException {
		return dataChannel.size() > 0;
	}

	/**
	 * Gets the number of files of the specified type.
	 * 
	 * @param type
	 *            The type.
	 * @return The number of files.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public int getFileCount(int type) throws IOException {
		if ((type < 0 || type >= indexChannels.length) && type != 255)
			throw new FileNotFoundException();

		if (type == 255)
			return (int) (metaChannel.size() / Index.SIZE);
		return (int) (indexChannels[type].size() / Index.SIZE);
	}

	/**
	 * Gets the number of index files, not including the meta index file.
	 * 
	 * @return The number of index files.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public int getTypeCount() throws IOException {
		return indexChannels.length;
	}

	/**
	 * Reads a file.
	 * 
	 * @param index
	 *            The type of the file.
	 * @param archive
	 *            The id of the file.
	 * @return A {@link ByteBuffer} containing the contents of the file.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public ByteBuffer read(ArchiveType index, ArchiveType archive) throws IOException {
		return read(index.getId(), archive.getId());
	}

	/**
	 * Reads a file.
	 * 
	 * @param type
	 *            The type of the file.
	 * @param id
	 *            The id of the file.
	 * @return A {@link ByteBuffer} containing the contents of the file.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	@SuppressWarnings("resource")
	public ByteBuffer read(int type, int id) throws IOException {
		if ((type < 0 || type >= indexChannels.length) && type != 255)
			throw new FileNotFoundException();

		FileChannel indexChannel = type == 255 ? metaChannel : indexChannels[type];

		long ptr = (long) id * (long) Index.SIZE;
		if (ptr < 0 || ptr >= indexChannel.size()) {
			throw new FileNotFoundException();
		}

		ByteBuffer buf = ByteBuffer.allocate(Index.SIZE);
		FileChannelUtils.readFully(indexChannel, buf, ptr);

		Index index = Index.decode((ByteBuffer) buf.flip());

		ByteBuffer data = ByteBuffer.allocate(index.getSize());
		buf = ByteBuffer.allocate(Page.SIZE);

		int chunk = 0, remaining = index.getSize();
		ptr = (long) index.getPage() * (long) Page.SIZE;
		do {
			buf.clear();
			FileChannelUtils.readFully(dataChannel, buf, ptr);
			boolean extended = id > 0xFFFF;
			Page page = extended ? Page.decodeExtended((ByteBuffer) buf.flip())
					: Page.decode((ByteBuffer) buf.flip());
			int dataSize = extended ? Page.EXTENDED_DATA_SIZE : Page.DATA_SIZE;
			if (remaining > dataSize) {
				data.put(page.getData(), 0, dataSize);
				remaining -= dataSize;

				if (page.getType() != type)
					throw new IOException("File type mismatch.");

				if (page.getId() != id)
					throw new IOException("File id mismatch.");

				if (page.getChunk() != chunk++)
					throw new IOException("Chunk mismatch.");

				ptr = (long) page.getNextSector() * (long) Page.SIZE;
			} else {
				data.put(page.getData(), 0, remaining);
				remaining = 0;
			}
		} while (remaining > 0);
		return (ByteBuffer) data.flip();
	}

	/**
	 * Writes a file.
	 * 
	 * @param type
	 *            The type of the file.
	 * @param id
	 *            The id of the file.
	 * @param data
	 *            A {@link ByteBuffer} containing the contents of the file.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void write(int type, int id, ByteBuffer data) throws IOException {
		data.mark();
		if (!write(type, id, data, true)) {
			data.reset();
			write(type, id, data, false);
		}
	}

	/**
	 * Writes a file.
	 * 
	 * @param type
	 *            The type of the file.
	 * @param id
	 *            The id of the file.
	 * @param data
	 *            A {@link ByteBuffer} containing the contents of the file.
	 * @param overwrite
	 *            A flag indicating if the existing file should be overwritten.
	 * @return A flag indicating if the file was written successfully.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	@SuppressWarnings("resource")
	private boolean write(int type, int id, ByteBuffer data, boolean overwrite) throws IOException {
		if ((type < 0 || type >= indexChannels.length) && type != 255)
			throw new FileNotFoundException();

		FileChannel indexChannel = type == 255 ? metaChannel : indexChannels[type];

		int nextPage = 0;
		long ptr = (long) id * (long) Index.SIZE;
		if (overwrite) {
			if (ptr < 0)
				throw new IOException();
			else if (ptr >= indexChannel.size())
				return false;

			ByteBuffer buf = ByteBuffer.allocate(Index.SIZE);
			FileChannelUtils.readFully(indexChannel, buf, ptr);

			Index index = Index.decode((ByteBuffer) buf.flip());
			nextPage = index.getPage();
			if (nextPage <= 0 || nextPage > dataChannel.size() * (long) Page.SIZE)
				return false;
		} else {
			nextPage = (int) ((dataChannel.size() + Page.SIZE - 1) / (long) Page.SIZE);
			if (nextPage == 0)
				nextPage = 1;
		}

		boolean extended = id > 0xFFFF;
		Index index = new Index(data.remaining(), nextPage);
		indexChannel.write(index.encode(), ptr);

		ByteBuffer buf = ByteBuffer.allocate(Page.SIZE);

		int chunk = 0, remaining = index.getSize();
		do {
			int curSector = nextPage;
			ptr = (long) curSector * (long) Page.SIZE;
			nextPage = 0;

			if (overwrite) {
				buf.clear();
				FileChannelUtils.readFully(dataChannel, buf, ptr);

				Page sector = extended ? Page.decodeExtended((ByteBuffer) buf.flip())
						: Page.decode((ByteBuffer) buf.flip());

				if (sector.getType() != type)
					return false;

				if (sector.getId() != id)
					return false;

				if (sector.getChunk() != chunk)
					return false;

				nextPage = sector.getNextSector();
				if (nextPage < 0 || nextPage > dataChannel.size() / (long) Page.SIZE)
					return false;
			}

			if (nextPage == 0) {
				overwrite = false;
				nextPage = (int) ((dataChannel.size() + Page.SIZE - 1) / (long) Page.SIZE);
				if (nextPage == 0)
					nextPage++;
				if (nextPage == curSector)
					nextPage++;
			}
			int dataSize = extended ? Page.EXTENDED_DATA_SIZE : Page.DATA_SIZE;
			byte[] bytes = new byte[dataSize];
			if (remaining < dataSize) {
				data.get(bytes, 0, remaining);
				nextPage = 0; // mark as EOF
				remaining = 0;
			} else {
				remaining -= dataSize;
				data.get(bytes, 0, dataSize);
			}

			Page sector = new Page(type, id, chunk++, nextPage, bytes);
			dataChannel.write(sector.encode(), ptr);
		} while (remaining > 0);

		return true;
	}

}

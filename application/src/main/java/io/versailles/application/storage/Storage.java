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
package io.versailles.application.storage;

import io.versailles.application.storage.util.ByteBufferUtils;
import io.versailles.application.storage.util.crypto.Djb2;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.CRC32;

/**
 * The {@link Storage} class provides a unified, high-level API for modifying the
 * cache of a Jagex game.
 *
 * @author Graham
 * @author `Discardedx2
 * @author Sino
 */
public final class Storage implements Closeable {
    /**
     * The {@link FileBundle} that backs this storage.
     */
    private final FileBundle store;

    /**
     * The collection of {@link ArchiveManifest}s.
     */
    private ArchiveManifest[] archiveManifests;

    /**
     * Contains mappings of file names to file id's.
     */
    private final Map<String, Integer> identifiers = new HashMap<>();

    /**
     * Creates a new {@link Storage} backed by the specified {@link FileBundle}.
     * @param store The {@link FileBundle} that backs this {@link Storage}.
     */
    public Storage(FileBundle store) throws IOException {
        this.store = store;
        this.archiveManifests = new ArchiveManifest[store.getArchiveCount()];

        for (int type = 0; type < store.getArchiveCount(); type++) {
            ByteBuffer buf = store.readPages(255, type);
            if (buf.limit() > 0) {
                archiveManifests[type] = ArchiveManifest.decode(Container.decode(buf).getData());
            }
        }
    }

    /**
     * Computes the {@link ReleaseManifest} for this {@link Storage}.
     * The manifest forms part of the so-called "update keys".
     *
     * @return The {@link ReleaseManifest}.
     * @throws IOException if an I/O error occurs.
     */
    public ReleaseManifest createReleaseManifest() throws IOException {
        /* create the checksum table */
        int size = store.getArchiveCount();
        ReleaseManifest manifest = new ReleaseManifest(size);

        /*
         * loop through all the archive manifests and get their CRC and versions
         */
        for (int i = 0; i < size; i++) {
            int crc = 0;
            int version = 0;

            if (store.hasData()) {
                /*
                 * if there is actually an archive manifest, calculate the CRC,
                 * version and whirlpool hash
                 */
                ByteBuffer buf = store.readPages(255, i);
                if (buf.limit() > 0) {
                    ArchiveManifest archiveManifest = archiveManifests[i];

                    crc = ByteBufferUtils.getCrcChecksum(buf);
                    version = archiveManifest.getVersion();
                }
            }

            manifest.setEntry(i, new ReleaseManifest.Entry(crc, version));
        }

        /* return the table */
        return manifest;
    }

    /**
     * Gets the number of files of the specified type.
     *
     * @param type The type.
     * @return The number of files.
     * @throws IOException if an I/O error occurs.
     */
    public int getFileCount(int type) throws IOException {
        return store.getFileCount(type);
    }

    /**
     * Gets the {@link FileBundle} that backs this {@link Storage}.
     *
     * @return The underlying {@link FileBundle}.
     */
    public FileBundle getBundle() {
        return store;
    }

    /**
     * Gets the number of index files, not including the meta index file.
     *
     * @return The number of index files.
     */
    public int getArchiveCount() {
        return store.getArchiveCount();
    }

    /**
     * Reads a file.
     *
     * @param archiveType The type of file.
     * @param folderType The file id.
     * @return The file.
     * @throws IOException if an I/O error occurred.
     */
    public Container read(ArchiveType archiveType, FolderType folderType) throws IOException {
        return read(archiveType.getId(), folderType.getId());
    }

    /**
     * Reads a file.
     *
     * @param archiveType The type of file.
     * @param folderId The file id.
     * @return The file.
     * @throws IOException if an I/O error occurred.
     */
    public Container read(ArchiveType archiveType, int folderId) throws IOException {
        return read(archiveType.getId(), folderId);
    }

    /**
     * Reads a file.
     *
     * @param archiveId The type of file.
     * @param folderId The file id.
     * @return The file.
     * @throws IOException if an I/O error occurred.
     */
    public Container read(int archiveId, int folderId) throws IOException {
        /* we don't want people reading/manipulating these manually */
        if (archiveId == 255) {
            throw new IOException("Archive manifests can only be read with the low level FileStore API!");
        }

        /* delegate the call to the file store then decode the container */
        return Container.decode(store.readPages(archiveId, folderId));
    }

    /**
     * Reads a file.
     *
     * @param archiveId The type of file.
     * @param folderId The file id.
     * @param keySet The decryption keys.
     * @return The file.
     * @throws IOException if an I/O error occurred.
     */
    public Container read(int archiveId, int folderId, int[] keySet) throws IOException {
        /* we don't want people reading/manipulating these manually */
        if (archiveId == 255) {
            throw new IOException("Archive manifests can only be read with the low level FileStore API!");
        }

        /* delegate the call to the file store then decode the container */
        return Container.decode(store.readPages(archiveId, folderId), keySet);
    }

    /**
     * Reads a file contained in an archive.
     *
     * @param archiveId The type of the file.
     * @param folderId The archive id.
     * @param packId The file within the archive.
     * @return The file.
     * @throws IOException if an I/O error occurred.
     */
    public ByteBuffer read(int archiveId, int folderId, int packId) throws IOException {
        /* grab the container and the archive manifest */
        Container container = read(archiveId, folderId);
        Container tableContainer = Container.decode(store.readPages(255, archiveId));
        ArchiveManifest archiveManifest = ArchiveManifest.decode(tableContainer.getData());

        /* check if the folder/pack are valid */
        ArchiveManifest.FolderManifest folderManifest = archiveManifest.getFolderManifest(folderId);
        if (folderManifest == null || packId < 0 || packId >= folderManifest.capacity())
            throw new FileNotFoundException();

        /* extract the pack from the folder */
        Folder folder = Folder.decode(container.getData(), folderManifest.capacity());
        return folder.getPack(packId);
    }

    public final ArchiveManifest getArchiveManifest(int type) {
        return archiveManifests[type];
    }

    public final ArchiveManifest getArchiveManifest(ArchiveType index) {
        return archiveManifests[index.getId()];
    }

    /**
     * Looks up and returns the id of a file that has the given name.
     */
    public int getFileId(int type, String name) {
        if (!identifiers.containsKey(name)) {
            ArchiveManifest table = archiveManifests[type];
            identifiers.put(name, table.getLabels().getFile(Djb2.hash(name)));
        }

        Integer i = identifiers.get(name);
        return i == null ? -1 : i.intValue();
    }

    /**
     * Writes a file to the file bundle and updates the {@link ArchiveManifest} that it
     * is associated with.
     *
     * @param archiveId      The type of file.
     * @param folderId      The file id.
     * @param container The {@link Container} to write.
     * @throws IOException if an I/O error occurs.
     */
    public void write(int archiveId, int folderId, Container container) throws IOException {
        write(archiveId, folderId, container, new int[4]);
    }

    /**
     * Writes a file and updates the {@link ArchiveManifest} that it
     * is associated with.
     *
     * @param archiveId      The type of file.
     * @param folderId      The file id.
     * @param container The {@link Container} to write.
     * @param keySet      The encryption keys.
     * @throws IOException if an I/O error occurs.
     */
    public void write(int archiveId, int folderId, Container container, int[] keySet) throws IOException {
        /* we don't want people reading/manipulating these manually */
        if (archiveId == 255) {
            throw new IOException("Archive manifests can only be modified with the low level FileStore API!");
        }

        /* increment the container's version */
        container.setVersion(container.getVersion()/* + 1 */);

        /* decode the manifest for the specified archive. */
        Container manifestContainer = Container.decode(store.readPages(255, archiveId));
        ArchiveManifest archiveManifest = ArchiveManifest.decode(manifestContainer.getData());

        /* grab the bytes we need for the checksum */
        ByteBuffer buffer = container.encode(keySet);

        /* last two bytes are the version and shouldn't be included */
        byte[] bytes = new byte[buffer.limit() - 2];
        buffer.mark();
        try {
            buffer.position(0);
            buffer.get(bytes, 0, bytes.length);
        } finally {
            buffer.reset();
        }

        /* calculate the new CRC checksum */
        CRC32 crc = new CRC32();
        crc.update(bytes, 0, bytes.length);

        /* update the version and checksum for this file */
        ArchiveManifest.FolderManifest folderManifest = archiveManifest.getFolderManifest(folderId);
        if (folderManifest == null) {
            /* create a new entry for the file */
            folderManifest = new ArchiveManifest.FolderManifest(folderId);
            archiveManifest.putFolderManifest(folderId, folderManifest);
        }

        folderManifest.setVersion(container.getVersion());
        folderManifest.setCrc((int) crc.getValue());

        /* update the version of the archive manifest */
        archiveManifest.setVersion(archiveManifest.getVersion()/* + 1 */);

        /* save the archive manifest */
        manifestContainer = new Container(manifestContainer.getType(), archiveManifest.encode());
        store.writePages(255, archiveId, manifestContainer.encode());

        /* save the file itself */
        store.writePages(archiveId, folderId, buffer);
    }

    /**
     * Writes a file contained in an archive.
     *
     * @param archive   The type of file.
     * @param folder   The id of the archive.
     * @param pack The file within the archive.
     * @param data   The data to write.
     * @throws IOException if an I/O error occurs.
     */
    public void write(int archive, int folder, int pack, ByteBuffer data) throws IOException {
        write(archive, folder, pack, data, new int[4]);
    }

    /**
     * Writes a file contained in an archive.
     *
     * @param archiveId   The type of file.
     * @param folderId   The id of the archive.
     * @param packId The file within the archive.
     * @param data   The data to write.
     * @param keySet   The encryption keys.
     * @throws IOException if an I/O error occurs.
     */
    public void write(int archiveId, int folderId, int packId, ByteBuffer data, int[] keySet) throws IOException {
        /* grab the archive manifests */
        Container manifestContainer = Container.decode(store.readPages(255, archiveId));
        ArchiveManifest archiveManifest = ArchiveManifest.decode(manifestContainer.getData());

        /* create a new entry if necessary */
        ArchiveManifest.FolderManifest folderManifest = archiveManifest.getFolderManifest(folderId);
        int oldArchiveSize = -1;
        if (folderManifest == null) {
            folderManifest = new ArchiveManifest.FolderManifest(folderId);
            archiveManifest.putFolderManifest(folderId, folderManifest);
        } else {
            oldArchiveSize = folderManifest.capacity();
        }

        /* add a pack manifest if one does not exist */
        ArchiveManifest.PackManifest packManifest = folderManifest.getPackManifest(packId);
        if (packManifest == null) {
            packManifest = new ArchiveManifest.PackManifest(packId);
            folderManifest.putPack(packId, packManifest);
        }

        /* extract the current folder into memory so we can modify it */
        Folder folder;
        int containerType, containerVersion;
        if (folderId < store.getFileCount(archiveId) && oldArchiveSize != -1) {
            Container container = read(archiveId, folderId);
            containerType = container.getType();
            containerVersion = container.getVersion();
            folder = Folder.decode(container.getData(), oldArchiveSize);
        } else {
            containerType = Container.COMPRESSION_GZIP;
            containerVersion = 1;
            folder = new Folder(packId + 1);
        }

        /* expand the folder if it is not large enough */
        if (packId >= folder.size()) {
            Folder newFolder = new Folder(packId + 1);
            for (int id = 0; id < folder.size(); id++) {
                newFolder.putPack(id, folder.getPack(id));
            }
            folder = newFolder;
        }

        /* put the pack into the folder */
        folder.putPack(packId, data);

        /* create 'dummy' packs */
        for (int id = 0; id < folder.size(); id++) {
            if (folder.getPack(id) == null) {
                folderManifest.putPack(id, new ArchiveManifest.PackManifest(id));
                folder.putPack(id, ByteBuffer.allocate(1));
            }
        }

        /* write the archive manifest out again */
        manifestContainer = new Container(manifestContainer.getType(), archiveManifest.encode());
        store.writePages(255, archiveId, manifestContainer.encode());

        /* and write the folder back to memory */
        Container container = new Container(containerType, folder.encode(), containerVersion);
        write(archiveId, folderId, container, keySet);
    }

    @Override
    public void close() throws IOException {
        store.close();
    }
}

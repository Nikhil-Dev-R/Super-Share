package com.rudraksha.supershare.core.domain.repository

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import com.rudraksha.supershare.core.domain.model.AppItem
import com.rudraksha.supershare.core.domain.model.AudioItem
import com.rudraksha.supershare.core.domain.model.ContactItem
import com.rudraksha.supershare.core.domain.model.FileItem
import com.rudraksha.supershare.core.domain.model.MediaItem
import com.rudraksha.supershare.core.domain.model.ShareableType
import com.rudraksha.supershare.core.ui.screens.history.HistoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class PickerRepositoryImpl(private val context: Context) : PickerRepository {

    //–– Generic query helper ––//
    private fun <T> queryContent(
        uri: Uri,
        projection: Array<String>,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        sortOrder: String? = null,
        mapper: (Cursor) -> T
    ): List<T> {
        val items = mutableListOf<T>()
        context.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
            ?.use { cursor ->
                while (cursor.moveToNext()) {
                    items += mapper(cursor)
                }
            }
        return items
    }

    override suspend fun getInstalledApps(): List<AppItem> = withContext(Dispatchers.IO) {
        val pm = context.packageManager
        Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER).let { intent ->
            pm.queryIntentActivities(intent, 0)
                .mapNotNull { info ->
                    val ai = info.activityInfo.applicationInfo
                    runCatching { pm.getApplicationIcon(ai.packageName) }.getOrNull()?.let { icon ->
                        AppItem(
                            packageName = ai.packageName,
                            name = pm.getApplicationLabel(ai).toString(),
                            icon = icon
                        )
                    }
                }
        }
    }

    override suspend fun getFiles(): List<FileItem> = withContext(Dispatchers.IO) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Only non-media files (MEDIA_TYPE_NONE == 0)
            val uri = MediaStore.Files.getContentUri("external")
            val selection =
                "${MediaStore.Files.FileColumns.MEDIA_TYPE} = ${MediaStore.Files.FileColumns.MEDIA_TYPE_NONE}"
            queryContent(
                uri = uri,
                projection = arrayOf(
                    MediaStore.Files.FileColumns.DISPLAY_NAME,
                    MediaStore.Files.FileColumns.DATA,
                    MediaStore.Files.FileColumns.SIZE
                ),
                selection = null,
                mapper = { c ->
                    FileItem(
                        name = c.getString(c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)),
                        path = c.getString(c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)),
                        size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE))
                    )
                }
            )
        } else {
            // Walk storage but skip any file whose extension is image/audio/video
            Environment.getExternalStorageDirectory()
                .takeIf { it.exists() && it.canRead() }
                ?.walkTopDown()
                ?.filter { it.isFile && !it.isMediaByExtension() }
                ?.map { FileItem(it.name, it.absolutePath, it.length()) }
                ?.toList()
                .orEmpty()
        }
    }

    /** Helper to detect image|audio|video by file extension */
    private fun File.isMediaByExtension(): Boolean {
        val ext = extension.lowercase().takeIf { it.isNotEmpty() } ?: return false
        val mime = android.webkit.MimeTypeMap
            .getSingleton()
            .getMimeTypeFromExtension(ext)
            ?.lowercase()
            ?: return false

        return mime.startsWith("image/")
                || mime.startsWith("video/")
                || mime.startsWith("audio/")
    }


    private suspend fun getMedia(type: ShareableType, uri: Uri) = withContext(Dispatchers.IO) {
        queryContent(
            uri = uri,
            projection = arrayOf(
                MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.SIZE,
                MediaStore.Video.Media.DURATION
            ),
            mapper = { c ->
                val id = c.getLong(c.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                val contentUri = ContentUris.withAppendedId(uri, id).toString()
                MediaItem(
                    name = c.getString(c.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)),
                    uri = contentUri,
                    size = c.getLong(c.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)),
                    duration = c.getColumnIndex(MediaStore.Video.Media.DURATION)
                        .takeIf { it >= 0 }
                        ?.let { c.getLong(it) }
                        ?: 0L,
                    mediaType = type
                )
            }
        )
    }

    override suspend fun getVideos(): List<MediaItem> =
        getMedia(ShareableType.VIDEO, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)

    override suspend fun getPhotos(): List<MediaItem> =
        getMedia(ShareableType.PHOTO, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

    override suspend fun getSongs(): List<AudioItem> = withContext(Dispatchers.IO) {
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        queryContent(
            uri = uri,
            projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.SIZE
            ),
            mapper = { c ->
                val id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                AudioItem(
                    name = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)),
                    artist = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)),
                    size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)),
                    uri = ContentUris.withAppendedId(uri, id).toString()
                )
            }
        )
    }

    override suspend fun getContacts(): List<ContactItem> = withContext(Dispatchers.IO) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) return@withContext emptyList()

        queryContent(
            uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection = arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            mapper = { c ->
                ContactItem(
                    name = c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                    number = c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                )
            }
        )
    }/*
    override suspend fun getContacts(): List<ContactItem> {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return emptyList()
        }

        val contacts = mutableListOf<ContactItem>()
        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        ) ?: return emptyList()

        cursor.use {
            val nameIndex =
                it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex =
                it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (cursor.moveToNext()) {
                val name = cursor.getString(nameIndex)
                val number = cursor.getString(numberIndex)
                contacts.add(ContactItem(name, number))
            }
        }
        return contacts
    }*/

    override suspend fun getHistory(): List<HistoryItem> {
        // TODO: implement real history retrieval
        return emptyList()
    }
}

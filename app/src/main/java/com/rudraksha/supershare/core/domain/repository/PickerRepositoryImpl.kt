package com.rudraksha.supershare.core.domain.repository

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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

class PickerRepositoryImpl(private val context: Context) : PickerRepository {

    override suspend fun getInstalledApps(): List<AppItem> {
        val pm = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val apps = pm.queryIntentActivities(intent, 0)
        return apps.mapNotNull { resolveInfo ->
            val appInfo = resolveInfo.activityInfo.applicationInfo
            val packageName = appInfo.packageName
            val appName = pm.getApplicationLabel(appInfo).toString()
            val icon = try {
                pm.getApplicationIcon(packageName)
            } catch (e: Exception) {
                null
            }
            AppItem(
                packageName = packageName,
                name = appName,
                icon = icon
            )
        }
    }

    override suspend fun getFiles(): List<FileItem> {
        // Android 11+ requires MANAGE_EXTERNAL_STORAGE or use MediaStore/SAF
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return emptyList() // SAF recommended instead
        }

        val directory = Environment.getExternalStorageDirectory()
        val files = directory.walkTopDown().filter { it.isFile }.toList()
        return files.map {
            FileItem(
                name = it.name,
                path = it.absolutePath,
                size = it.length()
            )
        }
    }

    override suspend fun getVideos(): List<MediaItem> {
        return queryMedia(
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            type = ShareableType.VIDEO
        )
    }

    override suspend fun getPhotos(): List<MediaItem> {
        return queryMedia(
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            type = ShareableType.PHOTO
        )
    }

    override suspend fun getSongs(): List<AudioItem> {
        val contentResolver = context.contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.SIZE
        )

        val songs = mutableListOf<AudioItem>()
        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val artistIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val sizeIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
            val idIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idIndex)
                val name = cursor.getString(nameIndex)
                val artist = cursor.getString(artistIndex)
                val size = cursor.getLong(sizeIndex)
                val contentUri = ContentUris.withAppendedId(uri, id).toString()

                songs.add(
                    AudioItem(
                        name = name,
                        uri = contentUri,
                        artist = artist,
                        size = size
                    )
                )
            }
        }

        return songs
    }

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
    }

    override suspend fun getHistory(): List<HistoryItem> {
        // Example placeholder; implement with Room or file-based history
        return emptyList()
    }

    private fun queryMedia(uri: Uri, type: ShareableType): List<MediaItem> {
        val contentResolver = context.contentResolver
        val projection = arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.SIZE,
            MediaStore.Video.Media.DURATION // Works for both video and image (may be null)
        )

        val mediaList = mutableListOf<MediaItem>()
        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val idIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            val nameIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)
            val durationIndex = cursor.getColumnIndex(MediaStore.Video.Media.DURATION)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idIndex)
                val name = cursor.getString(nameIndex)
                val size = cursor.getLong(sizeIndex)
                val duration = if (durationIndex >= 0) cursor.getLong(durationIndex) else 0L
                val contentUri = ContentUris.withAppendedId(uri, id).toString()

                mediaList.add(
                    MediaItem(
                        name = name,
                        uri = contentUri,
                        size = size,
                        duration = duration,
                        mediaType = type
                    )
                )
            }
        }
        return mediaList
    }
}

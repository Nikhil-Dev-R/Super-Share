package com.rudraksha.supershare.core.domain.model

import android.graphics.drawable.Drawable

open class ShareableItem(
    open val name: String,
    open val id: String,
    open val type: ShareableType,
    open val size: Long? = null,
    open val icon: Drawable? = null,
    open val date: Long = System.currentTimeMillis(),
)

enum class ShareableType {
    APP, FILE, VIDEO, PHOTO, AUDIO, CONTACT
}

data class AppItem(
    val packageName: String,
    override val name: String,
    override val icon: Drawable? = null,
) : ShareableItem(
    name = name,
    id = packageName,
    type = ShareableType.APP,
    size = null,
    icon = icon
)

data class FileItem(
    val path: String,
    override val name: String,
    override val size: Long,
    override val icon: Drawable? = null,
) : ShareableItem(
    name = name,
    id = name,
    type = ShareableType.FILE,
    size = size,
    icon = icon
)

data class MediaItem(
    val uri: String,
    override val name: String,
    override val size: Long,
    val duration: Long?,
    val mediaType: ShareableType, // VIDEO or PHOTO
    override val icon: Drawable? = null,
) : ShareableItem(
    name, uri, mediaType, size, icon
)

data class AudioItem(
    val uri: String,
    override val name: String,
    val artist: String,
    override val size: Long,
    override val icon: Drawable? = null,
) : ShareableItem(name, uri, ShareableType.AUDIO, size, icon)

data class ContactItem(
    val number: String,
    override val name: String,
    override val icon: Drawable? = null,
) : ShareableItem(name, number, ShareableType.CONTACT, null, icon)

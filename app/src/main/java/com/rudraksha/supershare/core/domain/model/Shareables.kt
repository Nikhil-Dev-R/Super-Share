package com.rudraksha.supershare.core.domain.model

open class ShareableItem(
    open val name: String,
    open val id: String,
    open val type: ShareableType,
    open val date: Long = System.currentTimeMillis(),
    open val size: Long? = null
)

enum class ShareableType {
    APP, FILE, VIDEO, PHOTO, AUDIO, CONTACT
}

data class AppItem(
    val packageName: String,
    override val name: String,
    val icon: Any? = null, // Later mapped to UI-specific type in ViewModel/Mapper
) : ShareableItem(
    name = name,
    id = packageName,
    type = ShareableType.APP,
    size = null
)

data class FileItem(
    val path: String,
    override val name: String,
    override val size: Long
) : ShareableItem(name, path, ShareableType.FILE, size)

data class MediaItem(
    val uri: String,
    override val name: String,
    override val size: Long,
    val duration: Long?,
    val mediaType: ShareableType // VIDEO or PHOTO
) : ShareableItem(name, uri, mediaType, size)

data class AudioItem(
    val uri: String,
    override val name: String,
    val artist: String,
    override val size: Long
) : ShareableItem(name, uri, ShareableType.AUDIO, size)

data class ContactItem(
    val number: String,
    override val name: String
) : ShareableItem(name, number, ShareableType.CONTACT)
